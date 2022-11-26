package com.geeks.geeksbackend.repository;

import com.geeks.geeksbackend.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
