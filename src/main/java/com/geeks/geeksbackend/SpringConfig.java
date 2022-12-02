package com.geeks.geeksbackend;

import com.geeks.geeksbackend.repository.UserPatternRepository;
import com.geeks.geeksbackend.repository.UserRepository;
import com.geeks.geeksbackend.repository.TaxiUserRepository;
import com.geeks.geeksbackend.repository.TaxiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SpringConfig {

    private final UserRepository userRepository;
    private final UserPatternRepository userPatternRepository;
    private final TaxiRepository taxiRepository;
    private final TaxiUserRepository taxiUserRepository;
}
