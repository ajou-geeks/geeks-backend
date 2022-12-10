package com.geeks.geeksbackend.dto.note;

import lombok.Getter;

@Getter
public class SendNoteDto {

    private Long receiverId;
    private String content;
}
