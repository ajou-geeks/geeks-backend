package com.geeks.geeksbackend.repository;

import com.geeks.geeksbackend.entity.Product;
import com.geeks.geeksbackend.entity.ProductUser;
import com.geeks.geeksbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductUserRepository extends JpaRepository<ProductUser, Long> {

    boolean existsByProductAndUser(Product product, User user);
    Optional<ProductUser> findByProductIdAndUserId(Long productId, Long userId);
}
