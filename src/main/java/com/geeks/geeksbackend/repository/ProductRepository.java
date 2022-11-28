package com.geeks.geeksbackend.repository;

import com.geeks.geeksbackend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
