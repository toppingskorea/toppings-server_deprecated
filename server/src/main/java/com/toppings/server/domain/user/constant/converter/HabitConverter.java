package com.toppings.server.domain.user.constant.converter;

import java.util.EnumSet;
import java.util.NoSuchElementException;

import javax.persistence.AttributeConverter;

import com.toppings.server.domain.user.constant.Habit;

public class HabitConverter implements AttributeConverter<Habit, String> {

	@Override
	public String convertToDatabaseColumn(Habit attribute) {
		return attribute.getName();
	}

	@Override
	public Habit convertToEntityAttribute(String dbData) {
		return EnumSet.allOf(Habit.class).stream()
			.filter(e -> e.getName().equals(dbData))
			.findAny()
			.orElseThrow(NoSuchElementException::new);
	}
}
