package com.geeks.geeksbackend.dto.Member;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.geeks.geeksbackend.entity.Member;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;

    @NotNull
    @Size(min = 3, max = 100)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    @NotNull
    private MultipartFile file;

    private Set<AuthorityDto> authorityDtoSet;

    public static UserDto from(Member member) {
        if(member == null) return null;

        return UserDto.builder()
                .email(member.getEmail())
                .id(member.getId())
                .authorityDtoSet(member.getAuthorities().stream()
                        .map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
                        .collect(Collectors.toSet()))
                .build();
    }
}
