package com.geeks.geeksbackend.entity;

import com.geeks.geeksbackend.dto.product.ProductDto;
import com.geeks.geeksbackend.enumeration.GroupBuyingStatus;
import com.geeks.geeksbackend.enumeration.ProductType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

@Entity
@Table(name = "tbl_product")
@SQLDelete(sql = "update tbl_product set is_deleted = true, deleted_at = now() where id = ?")
@Where(clause = "is_deleted = false")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ProductType type1;

    private int price;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "max_participant")
    private int maxParticipant;

    private String destination;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "total_amount")
    private int totalAmount;

    private int amount;

    @Column(name = "pickup_location")
    private String pickupLocation;

    @Column(name = "pickup_datetime")
    private LocalDateTime pickupDatetime;

    @Enumerated(EnumType.STRING)
    private GroupBuyingStatus status;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static Product createProduct(User user, ProductDto input) {
        return Product.builder()
                .user(user)
                .name(input.getName())
                .type1(ProductType.valueOfTitle(input.getType1()))
                .price(input.getPrice())
                .startTime(LocalDateTime.parse(input.getStartTime(), ISO_DATE_TIME))
                .endTime(LocalDateTime.parse(input.getEndTime(), ISO_DATE_TIME))
                .maxParticipant(input.getMaxParticipant())
                .destination(input.getDestination())
                .thumbnailUrl(input.getThumbnailUrl())
                .status(GroupBuyingStatus.OPEN)
                .createdBy(user.getId())
                .updatedBy(user.getId())
                .build();
    }
}
