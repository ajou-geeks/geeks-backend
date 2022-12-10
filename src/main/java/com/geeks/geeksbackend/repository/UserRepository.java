package com.geeks.geeksbackend.repository;

import com.geeks.geeksbackend.entity.User;
import com.geeks.geeksbackend.enumeration.LifePattern;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByEmail(String email);
    User findByEmail(String email);
    User findById(long id);
    List<User> findByPatternAndIdNot(LifePattern pattern, Long id);
}
