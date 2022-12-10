package com.geeks.geeksbackend.dto.notice;

import com.geeks.geeksbackend.entity.Notice;
import lombok.*;

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
        return null;
    }
}
