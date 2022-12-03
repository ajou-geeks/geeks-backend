package com.geeks.geeksbackend.dto.delivery;

import lombok.Getter;

@Getter
public class DeliveryReceiveDto {

    private Long id;
    private String pickupLocation;
    private String pickupDatetime;
}
