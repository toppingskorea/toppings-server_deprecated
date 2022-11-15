package com.toppings.server.domain.review.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.restaurant.repository.RestaurantRepository;
import com.toppings.server.domain.review.dto.ReviewModifyRequest;
import com.toppings.server.domain.review.dto.ReviewRequest;
import com.toppings.server.domain.review.dto.ReviewResponse;
import com.toppings.server.domain.review.entity.Review;
import com.toppings.server.domain.review.repository.ReviewRepository;
import com.toppings.server.domain.user.dto.UserResponse;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

	private final ReviewRepository reviewRepository;

	private final UserRepository userRepository;

	private final RestaurantRepository restaurantRepository;

	/**
	 * 댓글 등록하기
	 */
	@Transactional
	public ReviewResponse register(
		ReviewRequest request,
		Long restaurantId,
		Long userId
	) {
		User user = getUserById(userId);
		Restaurant restaurant = getRestaurantById(restaurantId);
		Review review = ReviewRequest.dtoToEntity(request);
		review.setUser(user);
		review.setRestaurant(restaurant);

		Review saveReview = reviewRepository.save(review);
		return ReviewResponse.entityToDto(saveReview);
	}

	private Restaurant getRestaurantById(Long id) {
		return restaurantRepository.findById(id)
			.orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));
	}

	private User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));
	}

	/**
	 * 댓글 수정하기
	 */
	@Transactional
	public ReviewResponse modify(
		ReviewModifyRequest request,
		Long reviewId,
		Long userId
	) {
		Review review = getReviewById(reviewId);
		User user = getUserById(userId);
		if (verifyReviewAndUser(review, user))
			throw new GeneralException(ResponseCode.BAD_REQUEST);

		ReviewModifyRequest.modifyReviewInfo(review, request);
		return ReviewResponse.entityToDto(review);
	}

	/**
	 * 댓글 삭제하기
	 */
	@Transactional
	public Long remove(
		Long reviewId,
		Long userId
	) {
		Review review = getReviewById(reviewId);
		User user = getUserById(userId);
		if (verifyReviewAndUser(review, user))
			throw new GeneralException(ResponseCode.BAD_REQUEST);

		reviewRepository.deleteById(reviewId);
		return reviewId;
	}

	private Review getReviewById(Long reviewId) {
		return reviewRepository.findById(reviewId)
			.orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));
	}

	private boolean verifyReviewAndUser(
		Review review,
		User user
	) {
		return !review.getUser().getId().equals(user.getId());
	}

	/**
	 * 음석점 댓글 목록 조회
	 */
	public List<ReviewResponse> findAll(
		Long restaurantId,
		Long userId
	) {
		return reviewRepository.findReviewByRestaurantId(restaurantId, userId);
	}

	/**
	 * 댓글 상세 조회
	 */
	public ReviewResponse findOne(
		Long reviewId,
		Long userId
	) {
		Review review = getReviewById(reviewId);
		ReviewResponse reviewResponse = ReviewResponse.entityToDto(review, review.getUser());
		reviewResponse.setIsMine(review.getUser().getId().equals(userId));
		return reviewResponse;
	}
}
