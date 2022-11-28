package com.geeks.geeksbackend.service;

import com.geeks.geeksbackend.dto.ProductDto;
import com.geeks.geeksbackend.dto.UserDto;
import com.geeks.geeksbackend.entity.Product;
import com.geeks.geeksbackend.enumeration.CoBuyStatus;
import com.geeks.geeksbackend.enumeration.ProductType;
import com.geeks.geeksbackend.repository.ProductRepository;
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

    public ProductDto createProduct(ProductDto productDto, UserDto userDto) {
        Product product = Product.builder()
                .name(productDto.getName())
                .type1(ProductType.valueOfTitle(productDto.getType1()))
                .price(productDto.getPrice())
                .startTime(LocalDateTime.parse(productDto.getStartTime(), DateTimeFormatter.ISO_DATE_TIME))
                .endTime(LocalDateTime.parse(productDto.getEndTime(), DateTimeFormatter.ISO_DATE_TIME))
                .maxParticipant(productDto.getMaxParticipant())
                .destination(productDto.getDestination())
                .thumbnailUrl(productDto.getThumbnailUrl())
                .status(CoBuyStatus.OPEN)
                .createdBy(userDto.getId())
                .updatedBy(userDto.getId())
                .build();

        return ProductDto.from(productRepository.save(product));
    }

    @Transactional
    public ProductDto updateProduct(Long id, ProductDto input, UserDto userDto) {
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
        product.setUpdatedBy(userDto.getId());

        return ProductDto.from(product);
    }
}
