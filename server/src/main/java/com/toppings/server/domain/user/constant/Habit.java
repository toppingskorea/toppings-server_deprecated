package com.toppings.server.domain.user.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Habit {

    // Diet
    Vegan("Vegan"),
    Vegetarian("Vegetarian"),
    Lowcarb("Low Carb"),
    Nosugar("No Sugar"),

    // Religion
    Buddhism("Buddhism"),
    Hinduism("Hinduism"),
    Islam("Islam")
    ;

    private final String name;
}
