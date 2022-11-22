package com.geeks.geeksbackend.entity;

import lombok.*;

import javax.persistence.Id;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post extends BaseEntity {

    @Id
    private long id;
    private String title;
    private String content;
}
