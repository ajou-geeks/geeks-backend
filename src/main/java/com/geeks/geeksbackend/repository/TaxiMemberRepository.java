package com.geeks.geeksbackend.repository;

import com.geeks.geeksbackend.entity.TaxiMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaxiMemberRepository extends JpaRepository<TaxiMember, Long> {
    List<TaxiMember> findByTaxiId(long taxiId);
    TaxiMember findByTaxiIdAndUserId(long taxiId, long userId);
}
