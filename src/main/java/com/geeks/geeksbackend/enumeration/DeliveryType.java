package com.geeks.geeksbackend.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum DeliveryType {
    KOREAN("한식"),
    WESTERN("양식"),
    CHINESE("중식"),
    JAPANESE("일식"),
    ASIAN("아시안"),
    FLOUR("분식"),
    MIDNIGHT("야식"),
    DESSERT("디저트");

    private final String title;

    public String title() {
        return title;
    }

    private static final Map<String, DeliveryType> BY_TITLE =
            Stream.of(values()).collect(Collectors.toMap(DeliveryType::title, e -> e));

    public static DeliveryType valueOfTitle(String title) {
        return BY_TITLE.get(title);
    }
}
