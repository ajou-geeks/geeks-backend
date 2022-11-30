package com.geeks.geeksbackend.dto.member;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMemberDto {
    private String detail;
    private String pattern;
    private String patternDetail;
}
