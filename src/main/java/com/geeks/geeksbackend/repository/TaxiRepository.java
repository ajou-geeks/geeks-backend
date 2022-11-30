package com.geeks.geeksbackend.repository;

import com.geeks.geeksbackend.entity.Member;
import com.geeks.geeksbackend.entity.Taxi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxiRepository extends JpaRepository<Taxi, Long> {
    Taxi findOneById(long id);
}
