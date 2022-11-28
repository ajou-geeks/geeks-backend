package com.geeks.geeksbackend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileDto {
    private String filename;
    private String dormitory;
    private String ho;
}
