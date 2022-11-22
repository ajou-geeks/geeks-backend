package com.geeks.geeksbackend.entity;

import com.geeks.geeksbackend.enumeration.CoBuyStatus;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "product")
public class Product extends BaseEntity {

    @Id
    private long id;
    private String name;
    private String type1;
    private int price;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int maxParticipant;
    private String destination;
    private CoBuyStatus status;
    private Post post;
}
