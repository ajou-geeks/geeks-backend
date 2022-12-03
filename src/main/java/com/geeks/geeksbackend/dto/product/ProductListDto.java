package com.geeks.geeksbackend.dto.product;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductListDto {

    private Long totalCount;
    private List<ProductDto> elements;
}
