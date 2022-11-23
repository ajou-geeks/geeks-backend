package com.geeks.geeksbackend.entity;

import com.geeks.geeksbackend.enumeration.CoBuyStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_product")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type1;
    private int price;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int maxParticipant;
    private String destination;
    private CoBuyStatus status;
    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
