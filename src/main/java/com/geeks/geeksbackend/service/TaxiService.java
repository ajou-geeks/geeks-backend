package com.geeks.geeksbackend.service;

import com.geeks.geeksbackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TaxiService {

    private final MemberRepository memberRepository;
}
