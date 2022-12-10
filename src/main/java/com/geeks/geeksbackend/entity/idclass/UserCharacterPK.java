package com.geeks.geeksbackend.entity.idclass;

import com.geeks.geeksbackend.enumeration.CharacterType;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Data
public class UserCharacterPK implements Serializable {
    private long id;
    @Enumerated(EnumType.STRING)
    private CharacterType type;
}
