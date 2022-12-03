package com.geeks.geeksbackend.repository;

import com.geeks.geeksbackend.entity.DeliveryUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryUserRepository extends JpaRepository<DeliveryUser, Long> {

    Optional<DeliveryUser> findByDeliveryIdAndUserId(Long deliveryId, Long userId);
}
