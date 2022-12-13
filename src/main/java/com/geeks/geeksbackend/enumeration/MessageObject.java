package com.geeks.geeksbackend.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageObject {

    PRODUCT("생필품"),
    DELIVERY("배달음식"),
    TAXI("택시");

    private final String title;
}
