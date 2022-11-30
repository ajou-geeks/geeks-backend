package com.geeks.geeksbackend.dto.taxi;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeDto {
    private long id;
    private long userId;
}
