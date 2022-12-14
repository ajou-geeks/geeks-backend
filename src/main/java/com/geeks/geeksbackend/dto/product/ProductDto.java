package com.geeks.geeksbackend.dto.product;

import com.geeks.geeksbackend.entity.Product;
import com.geeks.geeksbackend.entity.User;
import lombok.*;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private String type1;
    private int price;
    private String startTime;
    private String endTime;
    private int maxParticipant;
    private int curParticipant;
    private String destination;
    private String thumbnailUrl;
    private String status;
    private User userInfo;

    public static ProductDto from(Product product) {
        if (product == null) return null;

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .type1(product.getType1().title())
                .price(product.getPrice())
                .startTime(product.getStartTime().format(ISO_LOCAL_DATE_TIME))
                .endTime(product.getEndTime().format(ISO_LOCAL_DATE_TIME))
                .maxParticipant(product.getMaxParticipant())
                .destination(product.getDestination())
                .thumbnailUrl(product.getThumbnailUrl())
                .status(product.getStatus().name())
                .userInfo(User.builder()
                        .id(product.getUser().getId())
                        .nickname(product.getUser().getNickname())
                        .dormitory(product.getUser().getDormitory())
                        .build())
                .build();
    }
}
