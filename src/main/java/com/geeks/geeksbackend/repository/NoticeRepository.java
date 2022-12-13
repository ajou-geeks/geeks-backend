package com.geeks.geeksbackend.repository;

import com.geeks.geeksbackend.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findByUserId(Long userId, Pageable pageable);
}
