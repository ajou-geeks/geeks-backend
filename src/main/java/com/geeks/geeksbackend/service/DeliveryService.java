package com.geeks.geeksbackend.service;

import com.geeks.geeksbackend.dto.delivery.DeliveryDto;
import com.geeks.geeksbackend.entity.Delivery;
import com.geeks.geeksbackend.entity.DeliveryUser;
import com.geeks.geeksbackend.entity.User;
import com.geeks.geeksbackend.enumeration.CoBuyStatus;
import com.geeks.geeksbackend.enumeration.CoBuyUserType;
import com.geeks.geeksbackend.enumeration.DeliveryType;
import com.geeks.geeksbackend.repository.DeliveryRepository;
import com.geeks.geeksbackend.repository.DeliveryUserRepository;
import com.geeks.geeksbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

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
}
