package com.geeks.geeksbackend.entity;

import com.geeks.geeksbackend.enumeration.CoBuyStatus;
import com.geeks.geeksbackend.enumeration.DeliveryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_delivery")
@SQLDelete(sql = "update tbl_delivery set deleted = true, deleted_at = now() where id = ?")
@Where(clause = "deleted = false")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private DeliveryType type1;

    @Column(name = "min_amount")
    private int minAmount;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    private String destination;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "total_amount")
    private int totalAmount;

    @Column(name = "pickup_location")
    private String pickupLocation;

    @Column(name = "pickup_datetime")
    private LocalDateTime pickupDatetime;

    @Enumerated(EnumType.STRING)
    private CoBuyStatus status;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
