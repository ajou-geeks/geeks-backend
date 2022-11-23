package com.geeks.geeksbackend.entity;

import com.geeks.geeksbackend.enumeration.CharacterType;
import com.geeks.geeksbackend.enumeration.LifeType;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tbl_matching")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Matching extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private CharacterType characterType;
    private LifeType lifeType;
}
