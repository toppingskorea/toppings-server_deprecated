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
public class RestaurantRequest {

	@NotBlank(message = "이름을 확인해주세요.")
	private String name;

	@NotBlank(message = "설명을 확인해주세요.")
	private String description;

	@NotBlank(message = "주소를 확인해주세요.")
	private String address;

	@NotBlank(message = "우편번호를 확인해주세요.")
	private String zipcode;

	@NotBlank(message = "고유코드를 확인해주세요.")
	private String code;

	@NotNull(message = "위도를 확인해주세요.")
	private Double latitude;

	@NotNull(message = "경도를 확인해주세요.")
	private Double longitude;

	@NotNull(message = "타입을 확인해주세요.")
	private FoodType type;

	@NotNull(message = "이미지를 확인해주세요")
	@Size(min = 1, message = "이미지를 확인해주세요")
	private List<String> images;

	public static Restaurant dtoToEntity(
		RestaurantRequest request,
		User user
	) {
		return Restaurant.builder()
			.name(request.getName())
			.description(request.getDescription())
			.address(request.getAddress())
			.zipcode(request.getZipcode())
			.latitude(request.getLatitude())
			.longitude(request.getLongitude())
			.type(request.getType())
			.code(request.getCode())
			.images(request.getImages())
			.user(user)
			.build();
	}
}
