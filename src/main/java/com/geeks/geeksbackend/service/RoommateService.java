package com.geeks.geeksbackend.service;

import com.geeks.geeksbackend.dto.user.UserInfoDto;
import com.geeks.geeksbackend.dto.user.UserProfileDto;
import com.geeks.geeksbackend.entity.User;
import com.geeks.geeksbackend.entity.UserCharacter;
import com.geeks.geeksbackend.enumeration.CharacterType;
import com.geeks.geeksbackend.enumeration.LifePattern;
import com.geeks.geeksbackend.repository.UserCharacterRepository;
import com.geeks.geeksbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class RoommateService {

    private final UserRepository userRepository;
    private final UserCharacterRepository userCharacterRepository;

    public UserInfoDto createProfile(UserProfileDto input, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자 입니다."));

        user.setBio(input.getBio());
        user.setPattern(LifePattern.valueOfTitle(input.getPattern()));
        user.setPatternDetail(input.getPatternDetail());

        List<UserCharacter> userCharacters = userCharacterRepository.findAllById(userId);
        if (!userCharacters.isEmpty()) {
            userCharacterRepository.deleteAllInBatch(userCharacters);
        }

        for (String type : input.getCharacterType()) {
            UserCharacter userCharacter = UserCharacter.builder()
                    .id(userId)
                    .type(CharacterType.valueOfTitle(type))
                    .createdBy(userId)
                    .updatedBy(userId)
                    .build();

            userCharacterRepository.save(userCharacter);
        }

        return UserInfoDto.from(user, input.getCharacterType());
    }
}
