package com.geeks.geeksbackend.entity;

import com.geeks.geeksbackend.enumeration.GroupBuyingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_taxi")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Taxi extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;                    // 공동구매 생성 유저
    private int price;                      // 예상 가격
    @Column(name = "start_time")
    private LocalDateTime startTime;        // 출발 예정 시간
    @Column(name = "end_time")
    private LocalDateTime endTime;          // 공동구매 마감 시간
    @Column(name = "max_participant")
    private int maxParticipant;             // 최대 인원
    private String source;                  // 출발지
    private String destination;             // 도착지
    private GroupBuyingStatus status;       // 공동구매 상태
}
