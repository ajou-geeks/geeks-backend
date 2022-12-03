package com.geeks.geeksbackend.repository;

import com.geeks.geeksbackend.entity.UserPattern;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPatternRepository extends JpaRepository<UserPattern, Long> {
    List<UserPattern> findAllById(long id);
}
