package com.geeks.geeksbackend.service;

import com.geeks.geeksbackend.dto.delivery.DeliveryDto;
import com.geeks.geeksbackend.entity.Delivery;
import com.geeks.geeksbackend.entity.DeliveryUser;
import com.geeks.geeksbackend.entity.User;
import com.geeks.geeksbackend.dto.delivery.DeliveryJoinDto;
import com.geeks.geeksbackend.dto.delivery.DeliveryListDto;
import com.geeks.geeksbackend.enumeration.CoBuyStatus;
import com.geeks.geeksbackend.enumeration.CoBuyUserType;
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

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;
    private final DeliveryUserRepository deliveryUserRepository;

    public DeliveryDto createDelivery(DeliveryDto input, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자 입니다."));

        Delivery delivery = Delivery.builder()
                .user(user)
                .name(input.getName())
                .type1(DeliveryType.valueOfTitle(input.getType1()))
                .minAmount(input.getMinAmount())
                .startTime(LocalDateTime.parse(input.getStartTime(), DateTimeFormatter.ISO_DATE_TIME))
                .endTime(LocalDateTime.parse(input.getEndTime(), DateTimeFormatter.ISO_DATE_TIME))
                .destination(input.getDestination())
                .thumbnailUrl(input.getThumbnailUrl())
                .status(CoBuyStatus.OPEN)
                .createdBy(userId)
                .updatedBy(userId)
                .build();

        deliveryRepository.save(delivery);

        DeliveryUser deliveryUser = DeliveryUser.builder()
                .delivery(delivery)
                .user(user)
                .type(CoBuyUserType.MANAGER)
                .amount(input.getAmount())
                .createdBy(userId)
                .updatedBy(userId)
                .build();

        deliveryUserRepository.save(deliveryUser);

        return DeliveryDto.from(delivery);
    }

    public DeliveryDto updateDelivery(Long id, DeliveryDto input, Long userId) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공동구매 입니다."));
        DeliveryUser deliveryUser = deliveryUserRepository.findByDeliveryIdAndUserId(id, userId)
                .orElseThrow(() -> new NoSuchElementException("참여하지 않은 공동구매 입니다."));

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
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공동구매 입니다."));

        deliveryRepository.delete(delivery);
    }

    public DeliveryDto getDelivery(Long id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공동구매 입니다."));

        if (delivery.getEndTime().isBefore(LocalDateTime.now())) {
            delivery.setStatus(CoBuyStatus.EXPIRE);
        }

        return DeliveryDto.from(delivery);
    }

    public DeliveryListDto getDeliveryList(String query, Pageable pageable) {
        Page<Delivery> page = deliveryRepository.findByNameContains(query, pageable);
        List<Delivery> deliveries = new ArrayList<>();

        for (Delivery delivery : page.getContent()) {
            if (delivery.getEndTime().isBefore(LocalDateTime.now())) {
                delivery.setStatus(CoBuyStatus.EXPIRE);
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

    public DeliveryDto joinDelivery(DeliveryJoinDto input, Long userId) {
        Delivery delivery = deliveryRepository.findById(input.getId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공동구매 입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자 입니다."));

        if (deliveryUserRepository.existsByDeliveryAndUser(delivery, user)) {
            throw new RuntimeException("이미 참여한 공동구매 입니다.");
        }

        if (delivery.getStatus() == CoBuyStatus.EXPIRE ||
                delivery.getEndTime().isBefore(LocalDateTime.now())) {
            delivery.setStatus(CoBuyStatus.EXPIRE);
            throw new RuntimeException("만료된 공동구매 입니다.");
        }

        if (delivery.getStatus() != CoBuyStatus.OPEN) {
            throw new RuntimeException("참여할 수 없는 공동구매 입니다.");
        }

        DeliveryUser deliveryUser = DeliveryUser.builder()
                .delivery(delivery)
                .user(user)
                .type(CoBuyUserType.MEMBER)
                .amount(input.getAmount())
                .description(input.getDescription())
                .createdBy(userId)
                .updatedBy(userId)
                .build();

        deliveryUserRepository.save(deliveryUser);

        return DeliveryDto.from(delivery);
    }

    public DeliveryDto cancelDelivery(Long deliveryId, Long userId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공동구매 입니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자 입니다."));

        DeliveryUser deliveryUser = deliveryUserRepository.findByDeliveryIdAndUserId(delivery.getId(), user.getId())
                .orElseThrow(() -> new NoSuchElementException("참여하지 않은 공동구매 입니다."));

        if (deliveryUser.getType() == CoBuyUserType.MANAGER) {
            throw new RuntimeException("공동구매 진행자는 취소할 수 없습니다.");
        }

        if (delivery.getStatus() != CoBuyStatus.OPEN) {
            throw new RuntimeException("취소할 수 없는 공동구매 입니다.");
        }

        deliveryUserRepository.delete(deliveryUser);

        return DeliveryDto.from(delivery);
    }

    public DeliveryDto settleDelivery(DeliverySettleDto input, Long userId) {
        DeliveryUser deliveryUser = deliveryUserRepository.findByDeliveryIdAndUserId(input.getId(), userId)
                .orElseThrow(() -> new NoSuchElementException());

        Delivery delivery = deliveryUser.getDelivery();

        if (deliveryUser.getType() != CoBuyUserType.MANAGER) {
            throw new RuntimeException("공동구매 진행자만 정산을 요청할 수 있습니다.");
        }

        if (delivery.getStatus() != CoBuyStatus.CLOSE) {
            throw new RuntimeException("정산할 수 없는 공동구매 입니다.");
        }

        delivery.setBankName(input.getBankName());
        delivery.setAccountNumber(input.getAccountNumber());
        delivery.setTotalAmount(input.getTotalAmount());
        delivery.setStatus(CoBuyStatus.SETTLE);

        // TODO: 공동구매 참여자들에게 정산 알림 전송 (배달비 고려)
        // ...

        return DeliveryDto.from(delivery);
    }
}
