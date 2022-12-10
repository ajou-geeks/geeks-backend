package com.geeks.geeksbackend.dto.delivery;

import lombok.Getter;

@Getter
public class ReceiveDeliveryDto {

    private Long id;
    private String pickupLocation;
    private String pickupDatetime;
}
