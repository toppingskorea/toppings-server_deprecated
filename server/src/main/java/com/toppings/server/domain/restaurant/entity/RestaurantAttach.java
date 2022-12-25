package com.toppings.server.domain.restaurant.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import com.toppings.server.domain_global.utils.s3.S3Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "t_restaurant_attach")
public class RestaurantAttach {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "restaurant_attach_id")
	private Long id;

	@Column(name = "restaurant_image", columnDefinition = "longtext")
	private String image;

	@Column(name = "restaurant_image_path", columnDefinition = "varchar(200)")
	private String path;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	@JsonIgnoreProperties({"images"})
	@ToString.Exclude
	@JsonIgnore
	private Restaurant restaurant;

	public static RestaurantAttach of(
		S3Response s3Response,
		Restaurant restaurant
	) {
		return RestaurantAttach.builder()
			.image(s3Response.getImageUrl())
			.path(s3Response.getImagePath())
			.restaurant(restaurant)
			.build();
	}
}
