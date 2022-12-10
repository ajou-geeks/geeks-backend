package com.geeks.geeksbackend.entity;

import com.geeks.geeksbackend.entity.idclass.UserCharacterPK;
import com.geeks.geeksbackend.enumeration.CharacterType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "tbl_user_character")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UserCharacterPK.class)
public class UserCharacter extends BaseEntity {
    @Id
    private long id;
    @Id
    @Enumerated(EnumType.STRING)
    private CharacterType type;
}
