package com.geeks.geeksbackend.dto.notice;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeListDto {

    private Long totalCount;
    private List<NoticeDto> elements;
}
