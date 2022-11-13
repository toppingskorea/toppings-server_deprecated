package com.toppings.server.domain.restaurant.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain.restaurant.dto.RestaurantModifyRequest;
import com.toppings.server.domain.restaurant.dto.RestaurantRequest;
import com.toppings.server.domain.restaurant.dto.RestaurantResponse;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.restaurant.repository.RestaurantRepository;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

	private final RestaurantRepository restaurantRepository;

	private final UserRepository userRepository;

	/**
	 * 음식점 등록하기
	 */
	@Transactional
	public RestaurantResponse register(
		RestaurantRequest request,
		Long id
	) {
		User user = getUserById(id);
		Restaurant restaurant = restaurantRepository.findRestaurantByCode(request.getCode()).orElse(null);
		if (restaurant != null)
			throw new GeneralException(ResponseCode.DUPLICATED_ITEM);

		Restaurant saveRestaurant = restaurantRepository.save(RestaurantRequest.dtoToEntity(request, user));
		return RestaurantResponse.entityToDto(saveRestaurant);
	}

	private User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));
	}

	/**
	 * 음식점 수정하기
	 */
	@Transactional
	public RestaurantResponse modify(
		RestaurantModifyRequest request,
		Long id,
		Long userId
	) {
		Restaurant restaurant = getRestaurantById(id);
		if (verifyRestaurantAndUser(userId, restaurant))
			throw new GeneralException(ResponseCode.BAD_REQUEST);
		RestaurantModifyRequest.setRestaurantInfo(request, restaurant);
		RestaurantModifyRequest.setMapInfo(request, restaurant);
		return RestaurantResponse.entityToDto(restaurant);
	}

	private boolean verifyRestaurantAndUser(
		Long userId,
		Restaurant restaurant
	) {
		return !restaurant.getUser().getId().equals(userId);
	}

	private Restaurant getRestaurantById(Long id) {
		return restaurantRepository.findById(id)
			.orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));
	}

	/**
	 * 음식점 삭제하기
	 */
	public Long remove(Long id, Long userId) {
		Restaurant restaurant = getRestaurantById(id);
		if (verifyRestaurantAndUser(userId, restaurant))
			throw new GeneralException(ResponseCode.BAD_REQUEST);
		restaurantRepository.deleteById(id);
		return id;
	}
}
