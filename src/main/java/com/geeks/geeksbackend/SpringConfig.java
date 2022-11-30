package com.geeks.geeksbackend;

import com.geeks.geeksbackend.repository.MemberPatternRepository;
import com.geeks.geeksbackend.repository.MemberRepository;
import com.geeks.geeksbackend.repository.TaxiMemberRepository;
import com.geeks.geeksbackend.repository.TaxiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SpringConfig {

    private final MemberRepository memberRepository;
    private final MemberPatternRepository memberPatternRepository;
    private final TaxiRepository taxiRepository;
    private final TaxiMemberRepository taxiMemberRepository;
}
