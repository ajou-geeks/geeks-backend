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
}
