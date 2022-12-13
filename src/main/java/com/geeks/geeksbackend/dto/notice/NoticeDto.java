package com.geeks.geeksbackend.dto.notice;

import com.geeks.geeksbackend.entity.Notice;
import com.geeks.geeksbackend.enumeration.MessageObject;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDto {

    private Long id;
    private String object;
    private String title;
    private String content;
    private String value1;
    private String value2;
    private String createdAt;

    public static NoticeDto from(Notice notice) {
        if (notice == null) return null;

        return NoticeDto.builder()
                .id(notice.getId())
                .object(notice.getObject().getTitle())
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }
}
