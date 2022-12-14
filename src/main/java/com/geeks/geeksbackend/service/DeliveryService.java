package com.geeks.geeksbackend.service;

import com.geeks.geeksbackend.dto.delivery.*;
import com.geeks.geeksbackend.dto.notice.NoticeDto;
import com.geeks.geeksbackend.entity.*;
import com.geeks.geeksbackend.enumeration.GroupBuyingStatus;
import com.geeks.geeksbackend.enumeration.GroupBuyingUserType;
import com.geeks.geeksbackend.enumeration.DeliveryType;
import com.geeks.geeksbackend.repository.DeliveryRepository;
import com.geeks.geeksbackend.repository.DeliveryUserRepository;
import com.geeks.geeksbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.geeks.geeksbackend.enumeration.MessageTemplate.*;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;
    private final DeliveryUserRepository deliveryUserRepository;

    private final NoticeService noticeService;

    public DeliveryDto createDelivery(DeliveryDto input, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        Delivery delivery = Delivery.builder()
                .user(user)
                .name(input.getName())
                .type1(DeliveryType.valueOfTitle(input.getType1()))
                .minAmount(input.getMinAmount())
                .startTime(LocalDateTime.parse(input.getStartTime(), DateTimeFormatter.ISO_DATE_TIME))
                .endTime(LocalDateTime.parse(input.getEndTime(), DateTimeFormatter.ISO_DATE_TIME))
                .destination(input.getDestination())
                .thumbnailUrl(input.getThumbnailUrl())
                .status(GroupBuyingStatus.OPEN)
                .createdBy(userId)
                .updatedBy(userId)
                .build();

        deliveryRepository.save(delivery);

        DeliveryUser deliveryUser = DeliveryUser.builder()
                .delivery(delivery)
                .user(user)
                .type(GroupBuyingUserType.MANAGER)
                .amount(input.getAmount())
                .createdBy(userId)
                .updatedBy(userId)
                .build();

        deliveryUserRepository.save(deliveryUser);

        return DeliveryDto.from(delivery);
    }

    public DeliveryDto updateDelivery(Long id, DeliveryDto input, Long userId) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공동구매입니다."));
        DeliveryUser deliveryUser = deliveryUserRepository.findByDeliveryIdAndUserId(id, userId)
                .orElseThrow(() -> new NoSuchElementException("참여하지 않은 공동구매입니다."));

        // TODO: 공동구매에 이미 참여한 사용자가 있으면 수정 불가
        // ...

        delivery.setName(input.getName());
        delivery.setType1(DeliveryType.valueOfTitle(input.getType1()));
        delivery.setMinAmount(input.getMinAmount());
        delivery.setStartTime(LocalDateTime.parse(input.getStartTime(), DateTimeFormatter.ISO_DATE_TIME));
        delivery.setEndTime(LocalDateTime.parse(input.getEndTime(), DateTimeFormatter.ISO_DATE_TIME));
        delivery.setDestination(input.getDestination());
        delivery.setThumbnailUrl(input.getThumbnailUrl());
        delivery.setUpdatedBy(userId);

        deliveryUser.setAmount(input.getAmount());

        return DeliveryDto.from(delivery);
    }

    public void deleteDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공동구매입니다."));

        deliveryRepository.delete(delivery);
    }

    public DeliveryDto getDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공동구매입니다."));

        if (delivery.getEndTime().isBefore(LocalDateTime.now())) {
            delivery.setStatus(GroupBuyingStatus.EXPIRE);
        }

        return DeliveryDto.from(delivery);
    }

    public DeliveryListDto getDeliveryList(String query, Pageable pageable) {
        Page<Delivery> page = deliveryRepository.findByNameContains(query, pageable);
        List<Delivery> deliveries = new ArrayList<>();

        for (Delivery delivery : page.getContent()) {
            if (delivery.getEndTime().isBefore(LocalDateTime.now())) {
                delivery.setStatus(GroupBuyingStatus.EXPIRE);
            }
            deliveries.add(delivery);
        }

        return DeliveryListDto.builder()
                .totalCount(page.getTotalElements())
                .elements(deliveries.stream()
                        .map(DeliveryDto::from)
                        .collect(Collectors.toList()))
                .build();
    }

    public DeliveryDto joinDelivery(JoinDeliveryDto input, Long userId) {
        Delivery delivery = deliveryRepository.findById(input.getId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공동구매입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        if (deliveryUserRepository.existsByDeliveryAndUser(delivery, user)) {
            throw new RuntimeException("이미 참여한 공동구매입니다.");
        }

        if (delivery.getStatus() == GroupBuyingStatus.EXPIRE ||
                delivery.getEndTime().isBefore(LocalDateTime.now())) {
            delivery.setStatus(GroupBuyingStatus.EXPIRE); // 동작안함
            throw new RuntimeException("만료된 공동구매입니다.");
        }

        if (delivery.getStatus() != GroupBuyingStatus.OPEN) {
            throw new RuntimeException("참여할 수 없는 공동구매입니다.");
        }

        DeliveryUser deliveryUser = DeliveryUser.builder()
                .delivery(delivery)
                .user(user)
                .type(GroupBuyingUserType.MEMBER)
                .amount(input.getAmount())
                .description(input.getDescription())
                .createdBy(userId)
                .updatedBy(userId)
                .build();

        deliveryUserRepository.save(deliveryUser);

        // 진행자에게 [공동구매 참여] 알림 전송
        NoticeDto message = NoticeDto.builder()
                .object("DELIVERY")
                .title(GROUP_BUYING_JOIN_01.getTitle())
                .content(GROUP_BUYING_JOIN_01.getContent())
                .value1(user.getName())
                .value2(delivery.getName())
                .build();

        noticeService.sendNotice(message, delivery.getUser().getId());

        return DeliveryDto.from(delivery);
    }

    public DeliveryDto cancelDelivery(Long deliveryId, Long userId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공동구매입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        DeliveryUser deliveryUser = deliveryUserRepository.findByDeliveryIdAndUserId(delivery.getId(), user.getId())
                .orElseThrow(() -> new NoSuchElementException("참여하지 않은 공동구매입니다."));

        if (deliveryUser.getType() == GroupBuyingUserType.MANAGER) {
            throw new RuntimeException("공동구매 진행자는 취소할 수 없습니다.");
        }

        if (delivery.getStatus() != GroupBuyingStatus.OPEN) {
            throw new RuntimeException("취소할 수 없는 공동구매입니다.");
        }

        deliveryUserRepository.delete(deliveryUser);

        return DeliveryDto.from(delivery);
    }

    public DeliveryDto closeDelivery(Long deliveryId, Long userId) {
        List<DeliveryUser> deliveryUsers = deliveryUserRepository.findAllByDeliveryId(deliveryId);
        Delivery delivery = deliveryUsers.get(0).getDelivery();

        if (delivery.getStatus() != GroupBuyingStatus.OPEN) {
            throw new RuntimeException("마감할 수 없는 공동구매입니다.");
        }

//        int curAmount = deliveryUsers.stream().mapToInt(DeliveryUser::getAmount).sum();
//        if (curAmount < delivery.getMinAmount()) {
//            throw new RuntimeException("충분한 인원이 모집되지 않았습니다.");
//        }

        delivery.setStatus(GroupBuyingStatus.CLOSE);

        // TODO: 공동구매 참여자들에게 마감 알림 전송
        // ...

        return DeliveryDto.from(delivery);
    }

    public DeliveryDto settleDelivery(SettleDeliveryDto input, Long userId) {
        DeliveryUser deliveryUser = deliveryUserRepository.findByDeliveryIdAndUserId(input.getId(), userId)
                .orElseThrow(() -> new NoSuchElementException());

        Delivery delivery = deliveryUser.getDelivery();

        if (deliveryUser.getType() != GroupBuyingUserType.MANAGER) {
            throw new RuntimeException("공동구매 진행자만 정산을 요청할 수 있습니다.");
        }

        if (delivery.getStatus() != GroupBuyingStatus.CLOSE) {
            throw new RuntimeException("정산할 수 없는 공동구매입니다.");
        }

        delivery.setBankName(input.getBankName());
        delivery.setAccountNumber(input.getAccountNumber());
        delivery.setTotalAmount(input.getTotalAmount());
        delivery.setStatus(GroupBuyingStatus.SETTLE);

        // TODO: 공동구매 참여자들에게 정산 알림 전송 (배달비 고려)
        // ...

        return DeliveryDto.from(delivery);
    }

    public DeliveryDto receiveDelivery(ReceiveDeliveryDto input, Long userId) {
        DeliveryUser deliveryUser = deliveryUserRepository.findByDeliveryIdAndUserId(input.getId(), userId)
                .orElseThrow(() -> new NoSuchElementException());

        Delivery delivery = deliveryUser.getDelivery();

        if (deliveryUser.getType() != GroupBuyingUserType.MANAGER) {
            throw new RuntimeException("공동구매 진행자만 수령을 요청할 수 있습니다.");
        }

        if (delivery.getStatus() != GroupBuyingStatus.SETTLE) {
            throw new RuntimeException("수령할 수 없는 공동구매입니다.");
        }

        delivery.setPickupLocation(input.getPickupLocation());
        delivery.setPickupDatetime(LocalDateTime.parse(input.getPickupDatetime(), DateTimeFormatter.ISO_DATE_TIME));
        delivery.setStatus(GroupBuyingStatus.RECEIVE);

        // TODO: 공동구매 참여자들에게 수령 알림 전송
        // ...

        return DeliveryDto.from(delivery);
    }

    public DeliveryDto confirmDelivery(Long deliveryId, Long userId) {
        DeliveryUser deliveryUser = deliveryUserRepository.findByDeliveryIdAndUserId(deliveryId, userId)
                .orElseThrow(() -> new NoSuchElementException());

        Delivery delivery = deliveryUser.getDelivery();

        if (deliveryUser.getType() != GroupBuyingUserType.MANAGER) {
            throw new RuntimeException("공동구매 진행자만 완료를 요청할 수 있습니다.");
        }

        if (delivery.getStatus() != GroupBuyingStatus.RECEIVE) {
            throw new RuntimeException("완료할 수 없는 공동구매입니다.");
        }

        delivery.setStatus(GroupBuyingStatus.COMPLETE);

        return DeliveryDto.from(delivery);
    }
}
