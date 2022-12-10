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

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RoommateService {

    private final UserRepository userRepository;
    private final UserCharacterRepository userCharacterRepository;

    public UserInfoDto createProfile(UserProfileDto input, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        user.setBio(input.getBio());
        user.setPattern(LifePattern.valueOfTitle(input.getPattern()));
        user.setPatternDetail(input.getPatternDetail());

        List<UserCharacter> toBeDeletedUserCharacters = userCharacterRepository.findAllById(userId);
        List<UserCharacter> userCharacters = new ArrayList<>();

        if (!toBeDeletedUserCharacters.isEmpty()) {
            userCharacterRepository.deleteAllInBatch(toBeDeletedUserCharacters);
        }

        if (!input.getCharacterType().isEmpty()) {
            for (String type : input.getCharacterType()) {
                UserCharacter userCharacter = UserCharacter.builder()
                        .id(userId)
                        .type(CharacterType.valueOfTitle(type))
                        .createdBy(userId)
                        .updatedBy(userId)
                        .build();

                userCharacters.add(userCharacter);
            }
            userCharacterRepository.saveAll(userCharacters);
        }

        return UserInfoDto.from(user, userCharacters);
    }

    public UserInfoDto getProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        List<UserCharacter> userCharacters = userCharacterRepository.findAllById(id);

        return UserInfoDto.from(user, userCharacters);
    }
}
