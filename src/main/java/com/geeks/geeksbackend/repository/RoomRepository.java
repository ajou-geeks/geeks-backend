package com.geeks.geeksbackend.repository;

import com.geeks.geeksbackend.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByInviterIdInAndInviteeIdIn(Iterable<? extends Long> inviterIds, Iterable<? extends Long> inviteeIds);
}
