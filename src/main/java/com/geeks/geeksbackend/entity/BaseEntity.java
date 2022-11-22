package com.geeks.geeksbackend.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseEntity {
    private long createdBy;
    private LocalDateTime createdAt;
    private long updatedBy;
    private LocalDateTime updatedAt;
    private long deletedBy;
    private LocalDateTime deletedAt;
}
