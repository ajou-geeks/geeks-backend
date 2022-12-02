package com.geeks.geeksbackend.repository;

import com.geeks.geeksbackend.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
