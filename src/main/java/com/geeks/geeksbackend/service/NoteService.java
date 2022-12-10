package com.geeks.geeksbackend.service;

import com.geeks.geeksbackend.dto.note.*;
import com.geeks.geeksbackend.entity.Note;
import com.geeks.geeksbackend.entity.Room;
import com.geeks.geeksbackend.entity.User;
import com.geeks.geeksbackend.repository.NoteRepository;
import com.geeks.geeksbackend.repository.RoomRepository;
import com.geeks.geeksbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
@Transactional
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public NoteListDto sendNote(SendNoteDto input, Long userId) {
        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        User receiver = userRepository.findById(input.getReceiverId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        List<Long> ids = List.of(sender.getId(), receiver.getId());
        Room room = roomRepository.findByInviterIdInAndInviteeIdIn(ids, ids).orElse(null);

        if (room == null) {
            room = Room.builder()
                    .inviter(sender)
                    .invitee(receiver)
                    .createdBy(sender.getId())
                    .updatedBy(sender.getId())
                    .build();

            roomRepository.save(room);
        }

        Note note = Note.builder()
                .room(room)
                .sender(sender)
                .receiver(receiver)
                .content(input.getContent())
                .createdBy(sender.getId())
                .updatedBy(sender.getId())
                .build();

        noteRepository.save(note);

        List<Note> notes = noteRepository.findAllByRoomIdOrderByIdDesc(room.getId());

        return NoteListDto.builder()
                .totalCount((long) notes.size())
                .elements(notes.stream()
                        .map(n -> NoteDto.from(n, userId))
                        .collect(Collectors.toList()))
                .build();
    }

    public NoteListDto getNoteList(Long roomId, Long userId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 대화방입니다."));

        List<Note> notes = noteRepository.findAllByRoomIdOrderByIdDesc(room.getId());

        return NoteListDto.builder()
                .totalCount((long) notes.size())
                .elements(notes.stream()
                        .map(n -> NoteDto.from(n, userId))
                        .collect(Collectors.toList()))
                .build();
    }

    public RoomListDto getRoomList(Long userId) {
        List<Note> notes = noteRepository.findAllBySenderIdOrReceiverId(userId, userId);
        List<RoomDto> RoomDtos = new ArrayList<>();

        Map<Room, List<Note>> map = notes.stream()
                .sorted(Comparator.comparing(Note::getId, Comparator.reverseOrder()))
                .collect(groupingBy(Note::getRoom));

        for (Room room : map.keySet()) {
            RoomDto roomDto = RoomDto.from(map.get(room).get(0), userId);
            RoomDtos.add(roomDto);
        }

        return RoomListDto.builder()
                .totalCount((long) RoomDtos.size())
                .elements(RoomDtos)
                .build();
    }
}
