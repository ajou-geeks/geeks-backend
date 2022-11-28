package com.geeks.geeksbackend.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CoBuyStatus {
    OPEN("Opened"),
    CLOSE("Closed"),
    CANCEL("Cancelled"),
    SETTLE("Settled"),
    COMPLETE("Completed"),
    ERROR("Error");

    private final String value;

}
