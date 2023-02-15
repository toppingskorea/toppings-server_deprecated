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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
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

	@Column(name = "alarm_code", unique = true)
	private String code;

	@Column(name = "alarm_content", columnDefinition = "text")
	private String content;

	// 알람 종류 (좋아요 / 리뷰 / 스크랩)
	@Enumerated(EnumType.STRING)
	private AlarmType alarmType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@ToString.Exclude
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_user_id")
	@ToString.Exclude
	private User toUser;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	@ToString.Exclude
	private Restaurant restaurant;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "review_id")
	@ToString.Exclude
	private Review review;

	public static Alarm of(
		User user,
		User toUser,
		Restaurant restaurant,
		String content,
		AlarmType type
	) {
		return Alarm.builder()
			.alarmType(type)
			.content(content)
			.user(user.getId() != null ? user : null)
			.toUser(toUser)
			.restaurant(restaurant)
			.build();
	}

	public static Alarm of(
		User user,
		User toUser,
		Restaurant restaurant,
		Review review,
		String content,
		AlarmType type
	) {
		return Alarm.builder()
			.alarmType(type)
			.content(content)
			.user(user.getId() != null ? user : null)
			.toUser(toUser)
			.review(review)
			.build();
	}

	public void updateCode(String code) {
		this.code = code;
	}
}
