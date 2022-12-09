package com.toppings.server.domain.recent.entity;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.toppings.server.domain.recent.constant.RecentType;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain_global.constants.SearchCategory;
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
@Table(name = "t_recent_search")
public class Recent extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "recent_id")
	private Long id;

	@Column(name = "restaurant_id")
	private Long restaurantId;

	@Column(name = "recent_keyword", columnDefinition = "varchar(200)")
	private String keyword;

	@Column(name = "recent_content", columnDefinition = "text")
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(name = "recent_type", columnDefinition = "varchar(50)")
	private RecentType type;

	@Enumerated(EnumType.STRING)
	@Column(name = "recent_category", columnDefinition = "varchar(50)")
	private SearchCategory category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@JsonIgnoreProperties({"recents"})
	@ToString.Exclude
	@JsonIgnore
	private User user;
}
