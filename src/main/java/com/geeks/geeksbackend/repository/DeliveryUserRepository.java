package com.geeks.geeksbackend.repository;

import com.geeks.geeksbackend.entity.Delivery;
import com.geeks.geeksbackend.entity.DeliveryUser;
import com.geeks.geeksbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryUserRepository extends JpaRepository<DeliveryUser, Long> {

    boolean existsByDeliveryAndUser(Delivery delivery, User user);
    Optional<DeliveryUser> findByDeliveryIdAndUserId(Long deliveryId, Long userId);
}
