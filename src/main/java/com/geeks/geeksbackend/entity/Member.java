package com.geeks.geeksbackend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tbl_member")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member /*extends BaseEntity */{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    @Column(name = "profile_image")
    private String profileImage;    // 프로필 사진 파일 이름
    private String filename;        // 입주확인서 파일 이름
    private String dormitory;       // 기숙사
    private String ho;              // 호실
    private String detail;          // 자기소개
    private String pattern;         // 생활패턴
    @Column(name = "pattern_detail")
    private String patternDetail;   // 생활패턴 소개

    @ManyToMany
    @JoinTable(
            name = "tbl_user_authority",
            joinColumns = {@JoinColumn(name = "id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;
}
