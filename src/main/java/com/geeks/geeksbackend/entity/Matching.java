package com.geeks.geeksbackend.entity;

import com.geeks.geeksbackend.enumeration.CharacterType;
import com.geeks.geeksbackend.enumeration.LifeType;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "matching")
public class Matching extends BaseEntity {

    @Id
    private long id;
    private CharacterType characterType;
    private LifeType lifeType;
}
