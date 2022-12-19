package com.toppings.server.domain.user.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.toppings.server.domain.likes.entity.Likes;
import com.toppings.server.domain.recent.entity.Recent;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.review.entity.Review;
import com.toppings.server.domain.scrap.entity.Scrap;
import com.toppings.server.domain.user.constant.Auth;
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
@Table(name = "t_user")
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "user_username", columnDefinition = "varchar(100)", unique = true)
	private String username;

	@Column(name = "user_name", columnDefinition = "varchar(100)")
	private String name;

	@Column(name = "user_email", columnDefinition = "varchar(255)")
	private String email;

	@Column(name = "user_pw", columnDefinition = "varchar(200)")
	private String password;

	@Column(name = "user_country", columnDefinition = "varchar(200)")
	private String country;

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<UserHabit> habits;

	@Column(name = "user_role", columnDefinition = "varchar(20)")
	@Enumerated(EnumType.STRING)
	private Auth role;

	@Column(name = "delete_yn", columnDefinition = "varchar(1) default 'N'")
	private String deleteYn;

	@Column(name = "user_profile", columnDefinition = "longtext")
	private String profile;

	// 좋아요
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Likes> likes;

	// 음식점
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Restaurant> restaurants;

	// 스크랩
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Scrap> scraps;

	// 리뷰
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Review> reviews;

	// 최근 검색
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Recent> recents;

	@Column(name = "user_habit_contents", columnDefinition = "varchar(255)")
	private String habitContents;
}
