package com.geeks.geeksbackend.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CoBuyUserType {
    MANAGER("MANAGER"),
    MEMBER("MEMBER");

    private final String value;
}
