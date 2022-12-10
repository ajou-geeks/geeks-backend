package com.geeks.geeksbackend.service;

import com.geeks.geeksbackend.dto.user.UserInfoDto;
import com.geeks.geeksbackend.dto.user.UserInfoListDto;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RoommateService {

    private final UserRepository userRepository;
    private final UserCharacterRepository userCharacterRepository;

    private static final int SCORE_WITH_MATCHING_PATTERN = 50;
    private static final int SCORE_WITH_MATCHING_CHARACTER = 10;
    private static final int MAX_SCORE = 100;

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

    public UserInfoListDto getProfileList(Long userId) {
        User sourceUser = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다."));

        List<UserCharacter> sourceUserCharacters = userCharacterRepository.findAllById(userId);
        if (sourceUserCharacters.isEmpty()) {
            throw new RuntimeException("프로필을 등록하지 않은 사용자입니다.");
        }

        List<CharacterType> sourceCharacterTypes = sourceUserCharacters.stream()
                .map(e -> e.getType())
                .collect(Collectors.toList());

        List<User> targetUsers = userRepository.findByPatternAndIdNot(sourceUser.getPattern(), userId);
        List<UserInfoDto> toBeSortedTargetUsers = new ArrayList<>();

        for (User targetUser : targetUsers) {
            List<UserCharacter> targetUserCharacters = userCharacterRepository.findAllById(targetUser.getId());
            int score = SCORE_WITH_MATCHING_PATTERN;

            if (!targetUserCharacters.isEmpty()) {
                Set<CharacterType> commonCharacters = new HashSet<>(sourceCharacterTypes);
                commonCharacters.retainAll(targetUserCharacters.stream()
                        .map(e -> e.getType())
                        .collect(Collectors.toList()));
                score = Math.min(MAX_SCORE, score + SCORE_WITH_MATCHING_CHARACTER * commonCharacters.size());
            }

            toBeSortedTargetUsers.add(UserInfoDto.from(targetUser, targetUserCharacters, score));
        }

        List<UserInfoDto> sortedTargetUsers = toBeSortedTargetUsers.stream()
                .sorted((a, b) -> b.getScore() - a.getScore())
                .limit(10)
                .collect(Collectors.toList());

        return UserInfoListDto.builder()
                .totalCount((long) sortedTargetUsers.size())
                .elements(sortedTargetUsers)
                .build();
    }
}
