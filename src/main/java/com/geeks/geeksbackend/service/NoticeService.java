package com.geeks.geeksbackend.service;

import com.geeks.geeksbackend.dto.notice.NoticeDto;
import com.geeks.geeksbackend.dto.notice.NoticeListDto;
import com.geeks.geeksbackend.entity.Notice;
import com.geeks.geeksbackend.entity.User;
import com.geeks.geeksbackend.enumeration.MessageObject;
import com.geeks.geeksbackend.repository.NoticeRepository;
import com.geeks.geeksbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    public void sendNotice(NoticeDto message, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        Notice notice = Notice.builder()
                .user(user)
                .object(MessageObject.valueOf(message.getObject()))
                .title(String.format(message.getTitle()))
                .content(String.format(message.getContent(), message.getValue1(), message.getValue2()))
//                .createdBy(0L)
//                .updatedBy(0L)
                .build();

        noticeRepository.save(notice);
    }

    public NoticeListDto getNoticeList(Long userId, Pageable pageable) {
        Page<Notice> page = noticeRepository.findByUserId(userId, pageable);
        List<Notice> notices = page.getContent();

        return NoticeListDto.builder()
                .totalCount(page.getTotalElements())
                .elements(notices.stream()
                        .map(NoticeDto::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
