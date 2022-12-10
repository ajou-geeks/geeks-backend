package com.geeks.geeksbackend.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum CharacterType {
    EXTROVERT("외향적인"),
    INTROVERT("내향적인"),
    FUNNY("재미있는"),
    EASYGOING("느긋한"),
    HARDWORKING("부지런한"),
    PLAYFUL("쾌활한"),
    METICULOUS("꼼꼼한"),
    UNRESTRAINED("거리낌없는"),
    ENERGETIC("에너지있는"),
    FUN_LOVING("잘노는"),
    SHY("수줍은"),
    TALKATIVE("수다스러운"),
    MESSY("칠칠맞은"),
    SENSITIVE("예민한");

    private final String title;

    public String title() {
        return title;
    }

    private static final Map<String, CharacterType> BY_TITLE =
            Stream.of(values()).collect(Collectors.toMap(CharacterType::title, e -> e));

    public static CharacterType valueOfTitle(String title) {
        return BY_TITLE.get(title);
    }
}
