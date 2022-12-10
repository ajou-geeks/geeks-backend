package com.geeks.geeksbackend.repository;

import com.geeks.geeksbackend.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByRoomIdOrderByIdDesc(Long roomId);
    List<Note> findAllBySenderIdOrReceiverId(Long senderId, Long receiverId);
}
