package com.geeks.geeksbackend.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tbl_post")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
}
