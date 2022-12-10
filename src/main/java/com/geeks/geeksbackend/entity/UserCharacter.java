package com.geeks.geeksbackend.entity;

import com.geeks.geeksbackend.entity.idclass.UserCharacterPK;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tbl_user_character")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UserCharacterPK.class)
public class UserCharacter {
    @Id
    private long id;
    @Id
    private String type;
}
