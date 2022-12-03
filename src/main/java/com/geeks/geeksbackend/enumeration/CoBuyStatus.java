package com.geeks.geeksbackend.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum CoBuyStatus {
    OPEN("진행중"),
    CLOSE("모집완료"),
    EXPIRE("모집만료"),
    SETTLE("정산중"),
    COMPLETE("완료"),
    ERROR("에러");

    private final String title;

    public String title() {
        return title;
    }

    private static final Map<String, CoBuyStatus> BY_TITLE =
            Stream.of(values()).collect(Collectors.toMap(CoBuyStatus::title, e -> e));

    public static CoBuyStatus valueOfTitle(String title) {
        return BY_TITLE.get(title);
    }
}
