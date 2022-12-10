package com.geeks.geeksbackend.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum LifePattern {
    DOLPHIN("돌고래형"),
    LION("사자형"),
    BEAR("곰형"),
    WOLF("늑대형");

    private final String title;

    public String title() {
        return title;
    }

    private static final Map<String, LifePattern> BY_TITLE =
            Stream.of(values()).collect(Collectors.toMap(LifePattern::title, e -> e));

    public static LifePattern valueOfTitle(String title) {
        return BY_TITLE.get(title);
    }
}
