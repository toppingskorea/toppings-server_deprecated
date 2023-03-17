package com.toppings.planet.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.toppings.planet.domain.user.constants.Habit;
import com.toppings.planet.domain.user.constants.HabitTitle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "t_user_habit")
public class UserHabit {

	@Id
	@Column(name = "user_habit_id")
	private String id;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_habit_title", columnDefinition = "varchar(50)")
	private HabitTitle title;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_habit_content", columnDefinition = "varchar(100)")
	private Habit content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
}
