package com.geeks.geeksbackend.dto.product;

import lombok.Getter;

@Getter
public class SettleProductDto {

    private Long id;
    private String bankName;
    private String accountNumber;
    private int totalAmount;
    private int amount;
}
