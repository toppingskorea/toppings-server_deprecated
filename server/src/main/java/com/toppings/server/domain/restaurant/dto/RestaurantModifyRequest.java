package com.toppings.server.domain.restaurant.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
public class RestaurantModifyRequest {

	private String name;

	private String description;

	private String address;

	private String zipcode;

	private String code;

	private Double latitude;

	private Double longitude;

	private FoodType type;

	private List<String> images;

	public static void setRestaurantInfo(
		RestaurantModifyRequest request,
		Restaurant restaurant
	) {
		restaurant.setName(request.getName() != null ? request.getName() : restaurant.getName());
		restaurant.setAddress(request.getAddress() != null ? request.getAddress() : restaurant.getAddress());
		restaurant.setDescription(
			request.getDescription() != null ? request.getDescription() : restaurant.getDescription());
		restaurant.setType(request.getType() != null ? request.getType() : restaurant.getType());
		restaurant.setImages(request.getImages() != null && !request.getImages().isEmpty() ? request.getImages() :
			restaurant.getImages());
	}

	public static void setMapInfo(
		RestaurantModifyRequest request,
		Restaurant restaurant
	) {
		restaurant.setZipcode(request.getZipcode() != null ? request.getZipcode() : restaurant.getZipcode());
		restaurant.setCode(request.getCode() != null ? request.getCode() : restaurant.getCode());
		restaurant.setLatitude(request.getLatitude() != null ? request.getLatitude() : restaurant.getLatitude());
		restaurant.setLongitude(request.getLongitude() != null ? request.getLongitude() : restaurant.getLongitude());
	}
}
