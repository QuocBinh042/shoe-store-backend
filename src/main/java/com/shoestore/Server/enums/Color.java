package com.shoestore.Server.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Color {
    RED("Red"),
    GREEN("Green"),
    BLUE("Blue"),
    YELLOW("Yellow"),
    BLACK("Black"),
    WHITE("White"),
    PINK("Pink"),
    GREY("Grey"),
    ORANGE("Orange"),
    BROWN("Brown"),
    PURPLE("Purple");

    private final String colorName;

    @Override
    public String toString() {
        return colorName;
    }
}
