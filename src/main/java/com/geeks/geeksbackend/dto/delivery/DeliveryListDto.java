package com.geeks.geeksbackend.dto.delivery;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryListDto {

    private Long totalCount;
    private List<DeliveryDto> elements;
}
