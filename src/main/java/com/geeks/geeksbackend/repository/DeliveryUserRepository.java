package com.geeks.geeksbackend.repository;

import com.geeks.geeksbackend.entity.DeliveryUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryUserRepository extends JpaRepository<DeliveryUser, Long> {
}
