package com.geeks.geeksbackend.repository;

import com.geeks.geeksbackend.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
