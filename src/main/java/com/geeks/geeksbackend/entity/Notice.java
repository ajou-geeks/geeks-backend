package com.geeks.geeksbackend.entity;

import com.geeks.geeksbackend.enumeration.MessageObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "tbl_notice")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MessageObject object;

    private String title;

    private String content;

    private String value1;

    private String value2;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
