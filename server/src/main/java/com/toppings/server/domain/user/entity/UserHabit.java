package com.toppings.server.domain.user.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.toppings.server.domain.user.constant.Habit;
import com.toppings.server.domain.user.constant.HabitTitle;
import com.toppings.server.domain.user.constant.converter.HabitConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "t_user_habit")
public class UserHabit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_habit_id")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_habit_title", columnDefinition = "varchar(100)")
	private HabitTitle title;

	// @Enumerated(EnumType.STRING)
	@Convert(converter = HabitConverter.class)
	@Column(name = "user_habit_content", columnDefinition = "varchar(100)")
	private Habit content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@JsonIgnoreProperties({"images"})
	@ToString.Exclude
	@JsonIgnore
	private User user;

	public static Habit from(String value) {
		return Habit.valueOf(value.toUpperCase());
	}
}
