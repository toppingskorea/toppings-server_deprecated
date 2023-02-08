package com.toppings.server.domain.user.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Habit {

    // Diet
    Vegan("Vegan"),
    Vegetarian("Vegetarian"),
    Lowcarb("Low carb"),
    Nosugar("No sugar"),
    Muslimfriendly("Muslim friendly"),
    Halal("Halal"),

    // Religion
    Buddhism("Buddhism"),
    Hinduism("Hinduism"),
    ;

    private final String name;
}
