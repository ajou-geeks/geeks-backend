package com.geeks.geeksbackend.dto.product;

import lombok.Getter;

@Getter
public class ReceiveProductDto {

    private Long id;
    private String pickupLocation;
    private String pickupDatetime;
}
