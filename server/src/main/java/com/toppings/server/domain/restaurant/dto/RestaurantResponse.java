package com.toppings.server.domain.restaurant.dto;

import java.util.List;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantResponse {

	private Long id;

	private String name;

	private String address;

	private Double longitude;

	private Double latitude;

	private String description;

	private List<String> images;

	// 좋아요 갯수

	// 스크랩 갯수

	// 작성자

	// 국적

	// 식습관
}
