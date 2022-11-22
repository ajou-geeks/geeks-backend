package com.geeks.geeksbackend;

import com.geeks.geeksbackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SpringConfig {

    private final MemberRepository memberRepository;
}
