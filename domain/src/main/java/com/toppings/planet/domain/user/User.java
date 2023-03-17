package com.toppings.planet.domain.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.toppings.planet.domain.user.constants.Auth;
import com.toppings.planet.supports.domain.BaseEntity;
import com.toppings.planet.supports.domain.embedded.Image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(callSuper = true)
@DynamicUpdate
@DynamicInsert
@Table(name = "t_user",
	uniqueConstraints = {@UniqueConstraint(name = "t_user_username_uk", columnNames = "user_username")})
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "user_username", columnDefinition = "varchar(100)")
	private String username;

	@Column(name = "user_password", columnDefinition = "varchar(255)")
	private String password;

	@Column(name = "user_name", columnDefinition = "varchar(50)")
	private String name;

	@Column(name = "user_email", columnDefinition = "varchar(100)")
	private String email;

	@Column(name = "user_country", columnDefinition = "varchar(100)")
	private String country;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserHabit> habits = new ArrayList<>();

	@Column(name = "user_role", columnDefinition = "varchar(20)")
	@Enumerated(EnumType.STRING)
	private Auth role;

	@Embedded
	@AttributeOverride(name = "url", column = @Column(name = "user_profile_url"))
	@AttributeOverride(name = "path", column = @Column(name = "user_profile_path"))
	private Image profile;

	@Column(name = "delete_yn", columnDefinition = "varchar(1) default 'N'")
	private String deleteYn;
}
