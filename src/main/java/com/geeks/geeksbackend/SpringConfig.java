package com.geeks.geeksbackend;

import com.geeks.geeksbackend.repository.UserCharacterRepository;
import com.geeks.geeksbackend.repository.UserRepository;
import com.geeks.geeksbackend.repository.TaxiUserRepository;
import com.geeks.geeksbackend.repository.TaxiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SpringConfig {

    private final UserRepository userRepository;
    private final UserCharacterRepository userCharacterRepository;
    private final TaxiRepository taxiRepository;
    private final TaxiUserRepository taxiUserRepository;
}
