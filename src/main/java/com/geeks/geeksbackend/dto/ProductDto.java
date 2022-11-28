package com.geeks.geeksbackend.dto;

import com.geeks.geeksbackend.entity.Product;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    @NotNull
    private String type1;

    @NotNull
    private int price;

    @NotNull
    private String startTime;

    @NotNull
    private String endTime;

    @NotNull
    private int maxParticipant;

    private String destination;

    private String thumbnailUrl;

    private String status;

    public static ProductDto from(Product product) {
        if (product == null) return null;

        return ProductDto.builder()
                .name(product.getName())
                .type1(product.getType1().title())
                .price(product.getPrice())
                .startTime(product.getStartTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .endTime(product.getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .maxParticipant(product.getMaxParticipant())
                .destination(product.getDestination())
                .thumbnailUrl(product.getThumbnailUrl())
                .status(product.getStatus().name())
                .build();
    }
}
