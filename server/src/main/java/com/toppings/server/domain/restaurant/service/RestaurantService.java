package com.toppings.server.domain.restaurant.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.server.domain.restaurant.dto.RestaurantRequest;
import com.toppings.server.domain.restaurant.dto.RestaurantResponse;
import com.toppings.server.domain.restaurant.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

	private final RestaurantRepository restaurantRepository;

	public RestaurantResponse register(RestaurantRequest request, Long id) {

		/*
			TODO:
				- 다른사람에 의해 이미 등록된 음식점일 경우 어떻게 처리하는지?
					- 이미 등록되어 있다면 이미 등록되었다는 예외를 던져주자.
		 */

		// restaurantRepository.findRestaurantByZipcode(request.getZipcode())
		// restaurantRepository.save()

		return null;
	}
}
