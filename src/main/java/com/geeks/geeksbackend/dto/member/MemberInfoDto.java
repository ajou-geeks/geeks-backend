package com.geeks.geeksbackend.dto.member;

import com.geeks.geeksbackend.entity.Authority;
import com.geeks.geeksbackend.entity.MemberPattern;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoDto {

    private Long id;
    private String email;
    private String password;
    private String profileImage;
    private String filename;
    private String dormitory;
    private String ho;
    private String detail;
    private String pattern;
    private String patternDetail;
    private Set<Authority> authorities;
    private List<MemberPattern> memberPatterns;
}
