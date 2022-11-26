package com.geeks.geeksbackend.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private String name;
    private String password;
    private String nickname;
//    private String email;
//    private LocalDate birthday;
//    @Column(name = "profile_image")
//    private String profileImage;
//    private String bio;
//    private String dormitory;

    @ManyToMany
    @JoinTable(
            name = "tbl_user_authority",
            joinColumns = {@JoinColumn(name = "id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;
}
