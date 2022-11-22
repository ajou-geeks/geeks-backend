package com.geeks.geeksbackend.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "member")
public class Member extends BaseEntity {

    @Id
    private long id;
    private String name;
    private String nickname;
    private String email;
    private LocalDate birthday;
    private String profileImage;
    private String bio;
    private String dormitory;
}
