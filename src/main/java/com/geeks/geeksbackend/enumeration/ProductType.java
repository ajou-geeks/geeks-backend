package com.geeks.geeksbackend.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum ProductType {
    DRINK("음료"),
    SNACK("간식"),
    HEALTH_FOOD("건강식품"),
    HOUSEHOLD_GOODS("생활용품"),
    BEAUTY("뷰티");

    private final String title;

    public String title() {
        return title;
    }

    private static final Map<String, ProductType> BY_TITLE =
            Stream.of(values()).collect(Collectors.toMap(ProductType::title, e -> e));

    public static ProductType valueOfTitle(String title) {
        return BY_TITLE.get(title);
    }
}
