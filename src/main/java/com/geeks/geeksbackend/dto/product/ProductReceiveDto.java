package com.geeks.geeksbackend.dto.product;

import lombok.Getter;

@Getter
public class ProductReceiveDto {

    private Long id;
    private String pickupLocation;
    private String pickupDatetime;
}
