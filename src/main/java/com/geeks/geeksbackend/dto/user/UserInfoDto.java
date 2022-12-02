package com.geeks.geeksbackend.dto.user;

import com.geeks.geeksbackend.entity.Authority;
import com.geeks.geeksbackend.entity.UserPattern;
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
    private String detail;
    private String pattern;
    private String patternDetail;
    private Set<Authority> authorities;
    private List<UserPattern> userPatterns;
}
