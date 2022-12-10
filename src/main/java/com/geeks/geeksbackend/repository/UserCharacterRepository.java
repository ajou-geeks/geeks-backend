package com.geeks.geeksbackend.repository;

import com.geeks.geeksbackend.entity.UserCharacter;
import com.geeks.geeksbackend.entity.idclass.UserCharacterPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCharacterRepository extends JpaRepository<UserCharacter, UserCharacterPK> {
    List<UserCharacter> findAllById(long id);
}
