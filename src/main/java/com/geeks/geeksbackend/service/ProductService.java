package com.geeks.geeksbackend.service;

import com.geeks.geeksbackend.dto.product.ProductDto;
import com.geeks.geeksbackend.entity.Product;
import com.geeks.geeksbackend.entity.User;
import com.geeks.geeksbackend.enumeration.CoBuyStatus;
import com.geeks.geeksbackend.enumeration.ProductType;
import com.geeks.geeksbackend.repository.ProductRepository;
import com.geeks.geeksbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductDto createProduct(ProductDto productDto, Long userId) {
        User user = userRepository.findById(userId).get();

        Product product = Product.builder()
                .user(user)
                .name(productDto.getName())
                .type1(ProductType.valueOfTitle(productDto.getType1()))
                .price(productDto.getPrice())
                .startTime(LocalDateTime.parse(productDto.getStartTime(), DateTimeFormatter.ISO_DATE_TIME))
                .endTime(LocalDateTime.parse(productDto.getEndTime(), DateTimeFormatter.ISO_DATE_TIME))
                .maxParticipant(productDto.getMaxParticipant())
                .destination(productDto.getDestination())
                .thumbnailUrl(productDto.getThumbnailUrl())
                .status(CoBuyStatus.OPEN)
                .createdBy(userId)
                .updatedBy(userId)
                .build();

        return ProductDto.from(productRepository.save(product));
    }

    @Transactional
    public ProductDto updateProduct(Long id, ProductDto input, Long userId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공동구매 입니다."));

        // TODO: 공동구매에 이미 참여한 사용자가 있으면 수정 불가
        // ...

        // JPA 변경감지 사용
        product.setName(input.getName());
        product.setType1(ProductType.valueOfTitle(input.getType1()));
        product.setPrice(input.getPrice());
        product.setStartTime(LocalDateTime.parse(input.getStartTime(), DateTimeFormatter.ISO_DATE_TIME));
        product.setEndTime(LocalDateTime.parse(input.getEndTime(), DateTimeFormatter.ISO_DATE_TIME));
        product.setMaxParticipant(input.getMaxParticipant());
        product.setDestination(input.getDestination());
        product.setThumbnailUrl(input.getThumbnailUrl());
        product.setUpdatedBy(userId);

        return ProductDto.from(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공동구매 입니다."));

        productRepository.delete(product);
    }

    public ProductDto getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 공동구매 입니다."));

        return ProductDto.from(product);
    }
}
