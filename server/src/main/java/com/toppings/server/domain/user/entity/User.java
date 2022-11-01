package com.toppings.server.domain.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.toppings.common.constants.Auth;
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
	private Long userId;

	@Column(name = "user_username", columnDefinition = "varchar(100)", unique = true)
	private String username;

	@Column(name = "user_name", columnDefinition = "varchar(100)")
	private String name;

	@Column(name = "user_pw", columnDefinition = "varchar(200)")
	private String password;

	@Column(name = "user_country", columnDefinition = "varchar(200)")
	private String country;

	@Column(name = "user_habit", columnDefinition = "varchar(200)")
	private String habit;

	@Column(name = "user_role", columnDefinition = "varchar(20)")
	@Enumerated(EnumType.STRING)
	private Auth role;

	@Column(name = "refresh_token", columnDefinition = "varchar(200)")
	private String refreshToken;

	@Column(name = "delete_yn", columnDefinition = "varchar(1) default 'N'")
	private String deleteYn;
}
