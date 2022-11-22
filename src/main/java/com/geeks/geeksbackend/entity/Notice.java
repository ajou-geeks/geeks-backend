package com.geeks.geeksbackend.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "notice")
public class Notice extends BaseEntity {

    @Id
    private long id;
    private String content;
    private Member from;
    private Member to;
}
