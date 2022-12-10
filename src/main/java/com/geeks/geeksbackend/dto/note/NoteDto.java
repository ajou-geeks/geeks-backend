package com.geeks.geeksbackend.dto.note;

import com.geeks.geeksbackend.dto.user.UserInfoDto;
import com.geeks.geeksbackend.entity.Note;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoteDto {

    private Long id;
    private Long roomId;
    private String content;
    private UserInfoDto senderInfo;
    private UserInfoDto receiverInfo;
    private boolean isOwner;
    private String createdAt;

    public static NoteDto from(Note note, Long userId) {
        if (note == null) return null;

        return NoteDto.builder()
                .id(note.getId())
                .roomId(note.getRoom().getId())
                .content(note.getContent())
                .senderInfo(UserInfoDto.builder()
                        .id(note.getSender().getId())
                        .nickname(note.getSender().getNickname())
                        .profileImage(note.getSender().getProfileImage()).build())
                .receiverInfo(UserInfoDto.builder()
                        .id(note.getReceiver().getId())
                        .nickname(note.getReceiver().getNickname())
                        .profileImage(note.getReceiver().getProfileImage()).build())
                .isOwner(note.getSender().getId() == userId)
                .createdAt(note.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .build();
    }
}
