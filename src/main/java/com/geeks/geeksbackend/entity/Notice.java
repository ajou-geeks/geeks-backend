package com.geeks.geeksbackend.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tbl_notice")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne
    private Member from;
    @ManyToOne
    private Member to;
}
