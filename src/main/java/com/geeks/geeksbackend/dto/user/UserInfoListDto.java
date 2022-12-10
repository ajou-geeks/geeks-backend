package com.geeks.geeksbackend.dto.user;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoListDto {

    private Long totalCount;
    private List<UserInfoDto> elements;
}
