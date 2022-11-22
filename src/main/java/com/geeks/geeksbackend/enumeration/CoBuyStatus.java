package com.geeks.geeksbackend.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CoBuyStatus {
    OPEN("Opened"),
    CLOSE("Closed"),
    CANCLE("Cancelled"),
    SETTLE("Settleed"),
    COMPLETE("Completed"),
    ERR("Error");

    private final String value;

}
