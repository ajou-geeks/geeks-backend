package com.geeks.geeksbackend.service;

import java.util.Collections;
import java.util.Optional;

import com.geeks.geeksbackend.dto.UserDto;
import com.geeks.geeksbackend.entity.Authority;
import com.geeks.geeksbackend.entity.Member;
import com.geeks.geeksbackend.exception.DuplicateMemberException;
import com.geeks.geeksbackend.exception.NotFoundMemberException;
import com.geeks.geeksbackend.repository.MemberRepository;
import com.geeks.geeksbackend.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto signup(UserDto userDto, String savedName) {
        if (memberRepository.findOneWithAuthoritiesByEmail(userDto.getEmail()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        Member member = Member.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .filename(savedName)
                .authorities(Collections.singleton(authority))
                .build();

        return UserDto.from(memberRepository.save(member));
    }

    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String name) {
        return UserDto.from(memberRepository.findOneWithAuthoritiesByEmail(name).orElse(null));
    }

    @Transactional(readOnly = true)
    public UserDto getMyUserWithAuthorities() {
        return UserDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(memberRepository::findOneWithAuthoritiesByEmail)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }
}