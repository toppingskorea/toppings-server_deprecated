package com.toppings.server.domain.review.entity;

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
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.restaurant.entity.RestaurantAttach;
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
@Table(name = "t_review_attach")
public class ReviewAttach {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_attach_id")
	private Long id;

	@Column(name = "review_image", columnDefinition = "longtext")
	private String image;

	@Column(name = "review_image_path", columnDefinition = "varchar(200)")
	private String path;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "review_id")
	@JsonIgnoreProperties({"images"})
	@ToString.Exclude
	@JsonIgnore
	private Review review;

	public static ReviewAttach of(
		S3Response s3Response,
		Review review
	) {
		return ReviewAttach.builder()
			.image(s3Response.getImageUrl())
			.path(s3Response.getImagePath())
			.review(review)
			.build();
	}
}
