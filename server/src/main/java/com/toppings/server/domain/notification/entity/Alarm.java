package com.toppings.server.domain.notification.entity;

import javax.persistence.Column;
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

import com.toppings.server.domain.notification.constant.AlarmType;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.review.entity.Review;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain_global.entity.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@DynamicUpdate
@DynamicInsert
@Table(name = "t_alarm")
public class Alarm extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "alarm_id")
	private Long id;

	@Column(name = "alarm_content", columnDefinition = "text")
	private String content;

	// 알람 종류 (좋아요 / 리뷰 / 스크랩)
	@Enumerated(EnumType.STRING)
	private AlarmType alarmType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "review_id")
	private Review review;

	public static Alarm of(
		User user,
		Restaurant restaurant,
		String content,
		AlarmType type
	) {
		return Alarm.builder()
			.alarmType(type)
			.content(content)
			.user(user)
			.restaurant(restaurant)
			.build();
	}

	public static Alarm of(
		User user,
		Review review,
		String content,
		AlarmType type
	) {
		return Alarm.builder()
			.alarmType(type)
			.content(content)
			.user(user)
			.review(review)
			.build();
	}
}
