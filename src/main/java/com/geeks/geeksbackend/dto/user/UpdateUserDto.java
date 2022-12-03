package com.geeks.geeksbackend.dto.user;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    private String detail;
    private String pattern;
    private String patternDetail;
}
