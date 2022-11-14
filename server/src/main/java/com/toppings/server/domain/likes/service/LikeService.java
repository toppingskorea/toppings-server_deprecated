package com.toppings.server.domain.likes.service;

import com.toppings.server.domain.likes.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;

	public Object register(
		Long restaurantId,
		Long userId
	) {

		return null;
	}

	public Object remove(
		Long restaurantId,
		Long userId
	) {

		return null;
	}
}
