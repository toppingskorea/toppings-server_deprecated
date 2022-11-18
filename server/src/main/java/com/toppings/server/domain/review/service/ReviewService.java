package com.toppings.server.domain.review.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.restaurant.entity.RestaurantAttach;
import com.toppings.server.domain.restaurant.repository.RestaurantRepository;
import com.toppings.server.domain.review.dto.ReviewAttachRequest;
import com.toppings.server.domain.review.dto.ReviewAttachResponse;
import com.toppings.server.domain.review.dto.ReviewModifyRequest;
import com.toppings.server.domain.review.dto.ReviewRequest;
import com.toppings.server.domain.review.dto.ReviewResponse;
import com.toppings.server.domain.review.entity.Review;
import com.toppings.server.domain.review.entity.ReviewAttach;
import com.toppings.server.domain.review.repository.ReviewAttachRepository;
import com.toppings.server.domain.review.repository.ReviewRepository;
import com.toppings.server.domain.user.constant.Auth;
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

	private final ReviewAttachRepository reviewAttachRepository;

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
		List<ReviewAttachResponse> reviewAttachResponses = registerReviewAttach(request, saveReview);

		ReviewResponse reviewResponse = ReviewResponse.entityToDto(saveReview);
		reviewResponse.setImages(reviewAttachResponses);
		return reviewResponse;
	}

	private List<ReviewAttachResponse> registerReviewAttach(
		ReviewRequest request,
		Review review
	) {
		List<ReviewAttach> reviewAttaches = new ArrayList<>();
		for (ReviewAttachRequest reviewAttachRequest : request.getImages())
			reviewAttaches.add(ReviewAttachRequest.dtoToEntity(reviewAttachRequest, review));
		reviewAttachRepository.saveAll(reviewAttaches);

		return reviewAttaches.stream()
			.map(ReviewAttachResponse::entityToDto)
			.collect(Collectors.toList());
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
		if (verifyReviewAndUser(review, userId))
			throw new GeneralException(ResponseCode.BAD_REQUEST);

		ReviewModifyRequest.modifyReviewInfo(review, request);

		List<ReviewAttachResponse> reviewAttachResponses = modifyReviewAttach(request, review);
		ReviewResponse reviewResponse = ReviewResponse.entityToDto(review);
		reviewResponse.setImages(reviewAttachResponses);
		return reviewResponse;
	}

	private List<ReviewAttachResponse> modifyReviewAttach(
		ReviewModifyRequest request,
		Review review
	) {
		List<ReviewAttach> reviewAttaches = new ArrayList<>();
		if (request.getImages() != null && !request.getImages().isEmpty()) {
			// 기존 이미지 제거
			reviewAttachRepository.deleteAllByIdInBatch(
				review.getImages().stream().map(ReviewAttach::getId).collect(Collectors.toList()));

			// 신규 이미지 등록
			for (ReviewAttachRequest reviewAttachRequest : request.getImages())
				reviewAttaches.add(ReviewAttachRequest.dtoToEntity(reviewAttachRequest, review));
			reviewAttachRepository.saveAll(reviewAttaches);
		} else {
			throw new GeneralException(ResponseCode.BAD_REQUEST);
		}

		return reviewAttaches.stream()
			.map(ReviewAttachResponse::entityToDto)
			.collect(Collectors.toList());
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
		return !user.getRole().equals(Auth.ROLE_ADMIN) && !review.getUser().getId().equals(user.getId());
	}

	private boolean verifyReviewAndUser(
		Review review,
		Long userId
	) {
		return !review.getUser().getId().equals(userId);
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
