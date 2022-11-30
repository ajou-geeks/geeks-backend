package com.geeks.geeksbackend.repository;

import com.geeks.geeksbackend.entity.MemberPattern;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberPatternRepository extends JpaRepository<MemberPattern, Long> {
    List<MemberPattern> findAllById(long id);
}
