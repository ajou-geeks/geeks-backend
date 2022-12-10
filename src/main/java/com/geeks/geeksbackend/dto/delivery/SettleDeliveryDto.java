package com.geeks.geeksbackend.dto.delivery;

import lombok.Getter;

@Getter
public class SettleDeliveryDto {

    private Long id;
    private String bankName;
    private String accountNumber;
    private int totalAmount;
}
