package com.toppings.server.domain.review.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain.notification.constant.AlarmMessage;
import com.toppings.server.domain.notification.constant.AlarmType;
import com.toppings.server.domain.notification.dto.AlarmResponse;
import com.toppings.server.domain.notification.entity.Alarm;
import com.toppings.server.domain.notification.repository.AlarmRepository;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.restaurant.repository.RestaurantRepository;
import com.toppings.server.domain.review.dto.ReviewAttachRequest;
import com.toppings.server.domain.review.dto.ReviewListResponse;
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
import com.toppings.server.domain_global.utils.notification.AlarmSender;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

	private final ReviewRepository reviewRepository;

	private final UserRepository userRepository;

	private final RestaurantRepository restaurantRepository;

	private final ReviewAttachRepository reviewAttachRepository;

	private final AlarmRepository alarmRepository;

	private final AlarmSender alarmSender;

	/**
	 * 댓글 등록하기
	 */
	@Transactional
	public ReviewResponse register(
		ReviewRequest request,
		Long restaurantId,
		Long userId
	) {
		final Restaurant restaurant = getRestaurantById(restaurantId);
		final User user = getUserById(userId);

		final Review review = ReviewRequest.dtoToEntity(request);
		review.setUser(user);
		review.setRestaurant(restaurant);

		final Review saveReview = reviewRepository.save(review);
		final List<String> images = registerReviewAttach(request, saveReview);
		ReviewResponse reviewResponse = ReviewResponse.entityToDto(saveReview);
		reviewResponse.setImages(images);

		final User alarmUser = restaurant.getUser();
		saveAndSendAlarm(restaurant, saveReview, alarmUser);

		return reviewResponse;
	}

	private void saveAndSendAlarm(
		Restaurant restaurant,
		Review saveReview,
		User alarmUser
	) {
		final Alarm alarm = Alarm.of(alarmUser, restaurant, saveReview.getDescription(), AlarmType.Review);
		final Alarm savedAlarm = alarmRepository.save(alarm);
		alarmSender.send(restaurant, alarmUser, savedAlarm, AlarmMessage.ReviewMessage.getMessage());
	}

	private List<String> registerReviewAttach(
		ReviewRequest request,
		Review review
	) {
		final List<ReviewAttach> reviewAttaches = new ArrayList<>();
		for (String image : request.getImages())
			reviewAttaches.add(ReviewAttachRequest.dtoToEntity(image, review));
		reviewAttachRepository.saveAll(reviewAttaches);

		return reviewAttaches.stream()
			.map(ReviewAttach::getImage)
			.collect(Collectors.toList());
	}

	// TODO: public yn
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
		final Review review = getReviewById(reviewId);
		if (verifyReviewAndUser(review, userId))
			throw new GeneralException(ResponseCode.BAD_REQUEST);

		final List<String> images = modifyReviewAttach(request, review);
		final ReviewResponse reviewResponse = ReviewResponse.entityToDto(review);
		reviewResponse.setImages(images);

		ReviewModifyRequest.modifyReviewInfo(review, request, images.get(0));
		return reviewResponse;
	}

	private List<String> modifyReviewAttach(
		ReviewModifyRequest request,
		Review review
	) {
		final List<ReviewAttach> reviewAttaches = new ArrayList<>();
		if (request.getImages() != null && !request.getImages().isEmpty()) {
			// 기존 이미지 제거
			reviewAttachRepository.deleteAllByIdInBatch(
				review.getImages().stream().map(ReviewAttach::getId).collect(Collectors.toList()));

			// 신규 이미지 등록
			for (String image : request.getImages())
				reviewAttaches.add(ReviewAttachRequest.dtoToEntity(image, review));
			reviewAttachRepository.saveAll(reviewAttaches);
		} else {
			throw new GeneralException(ResponseCode.BAD_REQUEST);
		}

		return reviewAttaches.stream()
			.map(ReviewAttach::getImage)
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
		final Review review = getReviewById(reviewId);
		final User user = getUserById(userId);
		if (verifyReviewAndUser(review, user))
			throw new GeneralException(ResponseCode.BAD_REQUEST);

		reviewRepository.delete(review);
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
	public List<ReviewListResponse> findAll(
		Long restaurantId,
		Long userId
	) {
		final Restaurant restaurant = getRestaurantById(restaurantId);
		final List<ReviewListResponse> reviewListResponses
			= reviewRepository.findReviewByRestaurantId(restaurant.getId(), userId);
		reviewListResponses.forEach(en -> {
			en.setHabits(en.getHabitContents() != null ?
				Arrays.asList(en.getHabitContents().split(",")) :
				Collections.emptyList());
			en.setHabitContents(null);
		});
		return reviewListResponses;
	}

	/**
	 * 댓글 상세 조회
	 */
	public ReviewResponse findOne(
		Long reviewId,
		Long userId
	) {
		final Review review = getReviewById(reviewId);
		final ReviewResponse reviewResponse = ReviewResponse.entityToDto(review, review.getUser());
		reviewResponse.setIsMine(review.getUser().getId().equals(userId));
		reviewResponse.setHabits(Arrays.asList(review.getUser().getHabitContents().split(",")));
		return reviewResponse;
	}
}
