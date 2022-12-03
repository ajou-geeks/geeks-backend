package com.geeks.geeksbackend.dto.delivery;

import com.geeks.geeksbackend.entity.Delivery;
import com.geeks.geeksbackend.entity.User;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDto {

    private Long id;
    private String name;
    private String type1;
    private int minAmount;
    private int amount;
    private String startTime;
    private String endTime;
    private String destination;
    private String thumbnailUrl;
    private String status;
    private User userInfo;

    public static DeliveryDto from(Delivery delivery) {
        if (delivery == null) return null;

        return DeliveryDto.builder()
                .id(delivery.getId())
                .name(delivery.getName())
                .type1(delivery.getType1().title())
                .minAmount(delivery.getMinAmount())
                .startTime(delivery.getStartTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .endTime(delivery.getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .destination(delivery.getDestination())
                .thumbnailUrl(delivery.getThumbnailUrl())
                .status(delivery.getStatus().name())
                .userInfo(User.builder()
                        .id(delivery.getUser().getId())
                        .nickname(delivery.getUser().getNickname())
                        .dormitory(delivery.getUser().getDormitory())
                        .build())
                .build();
    }
}
