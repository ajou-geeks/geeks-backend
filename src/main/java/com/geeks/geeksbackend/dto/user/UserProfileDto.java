package com.geeks.geeksbackend.dto.user;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    private String bio;
    private List<String> characterType;
    private String pattern;
    private String patternDetail;
}
