package com.geeks.geeksbackend.dto.taxi;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateDto {
    private long userId;
    private int price;
    private String startTime;
    private String endTime;
    private int maxParticipant;
    private String source;
    private String destination;
}
