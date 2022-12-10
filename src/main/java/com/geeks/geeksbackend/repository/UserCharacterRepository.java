package com.geeks.geeksbackend.repository;

import com.geeks.geeksbackend.entity.UserCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCharacterRepository extends JpaRepository<UserCharacter, Long> {
    List<UserCharacter> findAllById(long id);
}
