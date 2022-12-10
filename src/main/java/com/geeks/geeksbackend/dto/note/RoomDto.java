package com.geeks.geeksbackend.dto.note;

import com.geeks.geeksbackend.dto.user.UserInfoDto;
import com.geeks.geeksbackend.entity.Note;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {

    private Long id;
    private String recentNoteContent;
    private UserInfoDto otherInfo;

    public static RoomDto from(Note note, Long userId) {
        if (note == null) return null;

        return RoomDto.builder()
                .id(note.getRoom().getId())
                .recentNoteContent(note.getContent())
                .otherInfo(note.getSender().getId() != userId ?
                        UserInfoDto.builder()
                                .id(note.getSender().getId())
                                .nickname(note.getSender().getNickname())
                                .profileImage(note.getSender().getProfileImage()).build() :
                        UserInfoDto.builder()
                                .id(note.getReceiver().getId())
                                .nickname(note.getReceiver().getNickname())
                                .profileImage(note.getReceiver().getProfileImage()).build())
                .build();
    }
}
