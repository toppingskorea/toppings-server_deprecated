package com.toppings.server.domain.restaurant.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.toppings.server.domain.restaurant.constant.FoodType;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantResponse {

	private Long id;

	private String name;

	private String address;

	private Double longitude;

	private Double latitude;

	private String description;

	private FoodType type;

	private List<String> images;

	private String code;

	// 좋아요 갯수
	private Integer likeCount;

	// 작성자
	private String writer;

	private String country;

	private boolean isLike;

	private boolean isScrap;

	private boolean isMine;

	private String publicYn;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd a HH:mm")
	private LocalDateTime createDate;

	private String cause;

	private boolean isAdmin;

	private String instagramId;

	public static RestaurantResponse entityToDto(Restaurant restaurant) {
		return RestaurantResponse.builder()
			.id(restaurant.getId())
			.name(restaurant.getName())
			.type(restaurant.getType())
			.address(restaurant.getAddress())
			.description(restaurant.getDescription())
			.likeCount(restaurant.getLikeCount())
			.latitude(restaurant.getLatitude())
			.longitude(restaurant.getLongitude())
			.code(restaurant.getCode())
			.createDate(restaurant.getCreateDate())
			.publicYn(restaurant.getPublicYn())
			.instagramId(restaurant.getInstagramId())
			.build();
	}

	public void updateImages(List<String> images) {
		if (images != null && !images.isEmpty())
			this.images = images;
	}

	public void updateIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public void updateIsLike(boolean isLike) {
		this.isLike = isLike;
	}

	public void updateIsScrap(boolean isScrap) {
		this.isScrap = isScrap;
	}

	public void updateUserInfo(User user) {
		this.writer = user.getName();
		this.country = user.getCountry();
	}

	public void updateIsMine(boolean isMine) {
		this.isMine = isMine;
	}

	public void updateCause(String cause) {
		this.cause = cause;
	}
}
