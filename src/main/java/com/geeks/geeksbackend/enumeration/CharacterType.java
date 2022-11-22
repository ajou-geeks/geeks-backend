package com.geeks.geeksbackend.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CharacterType {
    EXTRO("Extrovert"),
    INTRO("Introvert"),
    REAL("Realistic"),
    IMAGINE("Imaginative");

    private final String value;

}
