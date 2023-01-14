package com.toppings.server.domain.review.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain.notification.constant.AlarmType;
import com.toppings.server.domain.notification.dto.AlarmRequest;
import com.toppings.server.domain.notification.service.AlarmService;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.restaurant.repository.RestaurantRepository;
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
import com.toppings.server.domain_global.utils.s3.S3Response;
import com.toppings.server.domain_global.utils.s3.S3Uploader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

	private final ReviewRepository reviewRepository;

	private final UserRepository userRepository;

	private final RestaurantRepository restaurantRepository;

	private final ReviewAttachRepository reviewAttachRepository;

	private final AlarmService alarmService;

	private final S3Uploader s3Uploader;

	private final String imagePath = "review/";

	/**
	 * 댓글 등록하기
	 */
	@Transactional
	public Long register(
		ReviewRequest request,
		Long restaurantId,
		Long userId
	) {
		final Restaurant restaurant = getRestaurantById(restaurantId);
		final User user = getUserById(userId);

		final Review review = ReviewRequest.dtoToEntity(request);
		review.updateUserAndRestaurant(user, restaurant);

		final List<ReviewAttach> images = getReviewAttaches(request.getImages(), restaurantId, review);

		review.updateThumbnail(images.get(0).getImage());
		reviewRepository.save(review);
		reviewAttachRepository.saveAll(images);

		final AlarmRequest alarmRequest = AlarmRequest.of(user, restaurant, AlarmType.Like, review.getDescription());
		alarmService.registerAndSend(alarmRequest);

		return review.getId();
	}

	private List<ReviewAttach> getReviewAttaches(
		List<String> base64Images,
		Long restaurantId,
		Review review
	) {
		final List<ReviewAttach> images = new ArrayList<>();
		for (String image : base64Images) {
			byte[] decodedFile = DatatypeConverter.parseBase64Binary(image.substring(image.indexOf(",") + 1));
			S3Response s3Response = s3Uploader.uploadBase64(decodedFile, imagePath + restaurantId + "/");
			images.add(ReviewAttach.of(s3Response, review));
		}
		return images;
	}

	private Restaurant getRestaurantById(Long id) {
		return restaurantRepository.findRestaurantByIdAndPublicYnNot(id, "N")
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_FOUND));
	}

	private User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));
	}

	/**
	 * 댓글 수정하기
	 */
	@Transactional
	public Long modify(
		ReviewModifyRequest request,
		Long reviewId,
		Long userId
	) {
		final Review review = getReviewById(reviewId);
		if (verifyReviewAndUser(review, userId))
			throw new GeneralException(ResponseCode.BAD_REQUEST);

		final List<ReviewAttach> images = modifyReviewAttach(request, review);

		// TODO: Refactoring Pick
		ReviewModifyRequest.modifyReviewInfo(review, request, images.get(0).getImage());
		return review.getId();
	}

	private List<ReviewAttach> modifyReviewAttach(
		ReviewModifyRequest request,
		Review review
	) {
		if (isNotNullImage(request.getImages())) {
			// 기존 이미지 제거
			reviewAttachRepository.deleteAllByIdInBatch(
				review.getImages().stream().map(ReviewAttach::getId).collect(Collectors.toList()));

			// 신규 이미지 등록
			final List<ReviewAttach> reviewAttaches
				= getReviewAttaches(request.getImages(), review.getRestaurant().getId(), review);
			reviewAttachRepository.saveAll(reviewAttaches);
			return reviewAttaches;
		} else {
			throw new GeneralException(ResponseCode.BAD_REQUEST);
		}
	}

	private boolean isNotNullImage(List<String> images) {
		return images != null && !images.isEmpty();
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

		// TODO: Refactoring Pick
		reviewResponse.setImages(review.getImages().stream().map(ReviewAttach::getImage).collect(Collectors.toList()));
		reviewResponse.setIsMine(review.getUser().getId().equals(userId));
		reviewResponse.setHabits(Arrays.asList(review.getUser().getHabitContents().split(",")));
		return reviewResponse;
	}
}
