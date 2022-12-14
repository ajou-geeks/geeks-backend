package com.geeks.geeksbackend.repository;

import com.geeks.geeksbackend.entity.Product;
import com.geeks.geeksbackend.entity.ProductUser;
import com.geeks.geeksbackend.entity.User;
import com.geeks.geeksbackend.enumeration.GroupBuyingUserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductUserRepository extends JpaRepository<ProductUser, Long> {

    boolean existsByProductAndUser(Product product, User user);
    Optional<ProductUser> findByProductIdAndUserId(Long productId, Long userId);
    Optional<ProductUser> findByProductIdAndUserIdAndType(Long productId, Long userId, GroupBuyingUserType type);
    List<ProductUser> findAllByProductId(Long productId);
    int countAllByProductId(Long productId);
}
