package com.geeks.geeksbackend.dto.user;

import com.geeks.geeksbackend.entity.Authority;
import com.geeks.geeksbackend.entity.User;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String profileImage;
    private String filename;
    private String dormitory;
    private String ho;
    private String bio;
    private String pattern;
    private String patternDetail;
    private List<String> userCharacters;
    private Set<Authority> authorities;

    public static UserInfoDto from(User user, List<String> userCharacters) {
        if (user == null) return null;

        return UserInfoDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(null)
                .name(user.getName())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .filename(user.getFilename())
                .dormitory(user.getDormitory())
                .ho(user.getHo())
                .pattern(user.getPattern().title())
                .patternDetail(user.getPatternDetail())
                .userCharacters(userCharacters)
                .authorities(user.getAuthorities())
                .build();
    }
}
