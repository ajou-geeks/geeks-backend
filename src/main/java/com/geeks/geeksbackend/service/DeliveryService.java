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
}
