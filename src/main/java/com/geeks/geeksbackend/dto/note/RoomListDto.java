package com.geeks.geeksbackend.dto.note;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomListDto {

    private Long totalCount;
    private List<RoomDto> elements;
}
