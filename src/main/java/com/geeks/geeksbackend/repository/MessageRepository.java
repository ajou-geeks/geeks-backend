package com.geeks.geeksbackend.repository;

import com.geeks.geeksbackend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
