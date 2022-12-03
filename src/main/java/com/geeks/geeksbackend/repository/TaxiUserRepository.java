package com.geeks.geeksbackend.repository;

import com.geeks.geeksbackend.entity.TaxiUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaxiUserRepository extends JpaRepository<TaxiUser, Long> {
    List<TaxiUser> findByTaxiId(long taxiId);
    TaxiUser findByTaxiIdAndUserId(long taxiId, long userId);
}
