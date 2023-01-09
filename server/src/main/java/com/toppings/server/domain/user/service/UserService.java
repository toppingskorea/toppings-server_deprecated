package com.toppings.server.domain.user.service;

import static org.springframework.util.StringUtils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain.likes.repository.LikeRepository;
import com.toppings.server.domain.restaurant.dto.RestaurantListResponse;
import com.toppings.server.domain.restaurant.repository.RestaurantRepository;
import com.toppings.server.domain.review.repository.ReviewRepository;
import com.toppings.server.domain.scrap.repository.ScrapRepository;
import com.toppings.server.domain.user.constant.Auth;
import com.toppings.server.domain.user.dto.UserCount;
import com.toppings.server.domain.user.dto.UserHabitRequest;
import com.toppings.server.domain.user.dto.UserHabitResponse;
import com.toppings.server.domain.user.dto.UserModifyRequest;
import com.toppings.server.domain.user.dto.UserRegisterRequest;
import com.toppings.server.domain.user.dto.UserResponse;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain.user.entity.UserHabit;
import com.toppings.server.domain.user.repository.UserHabitRepository;
import com.toppings.server.domain.user.repository.UserRepository;
import com.toppings.server.domain_global.utils.s3.S3Response;
import com.toppings.server.domain_global.utils.s3.S3Uploader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;

	private final UserHabitRepository userHabitRepository;

	private final LikeRepository likeRepository;

	private final ScrapRepository scrapRepository;

	private final ReviewRepository reviewRepository;

	private final RestaurantRepository restaurantRepository;

	private final S3Uploader s3Uploader;

	private final String imagePath = "user/";

	/**
	 * 회원 가입
	 */
	@Transactional
	public Long register(
		UserRegisterRequest request,
		Long userId
	) {
		final User user = getUserById(userId);
		if (user.getCountry() != null)
			throw new GeneralException(ResponseCode.DUPLICATED_USER);

		user.registerUserInfo(request.getCountry(), getHabitContents(request.getHabit()));
		if (request.getHabit() != null)
			registerUserHabit(request, user);
		return user.getId();
	}

	private String getHabitContents(List<UserHabitRequest> habitRequests) {
		return habitRequests != null ? habitRequests.stream().map(en -> en.getContent().name())
			.collect(Collectors.joining(",", "", "")) : null;
	}

	private void registerUserHabit(
		UserRegisterRequest request,
		User user
	) {
		final List<UserHabit> userHabits = new ArrayList<>();
		for (UserHabitRequest habitRequest : request.getHabit())
			userHabits.add(UserHabitRequest.createUserHabit(habitRequest, user));
		userHabitRepository.saveAll(userHabits);
	}

	/**
	 * 회원 정보 수정
	 */
	@Transactional
	public Long modify(
		UserModifyRequest request,
		Long userId
	) {
		final User user = getUserById(userId);

		// TODO: image 삭제도 필요
		String profile = request.getProfile();
		if (hasText(profile)) {
			byte[] decodedFile = DatatypeConverter.parseBase64Binary(profile.substring(profile.indexOf(",") + 1));
			S3Response s3Response = s3Uploader.uploadBase64(decodedFile, imagePath + userId + "/");
			user.updateProfile(s3Response.getImageUrl(), s3Response.getImagePath());
		}
		user.updateUserInfo(request.getName(), request.getCountry());
		modifyUserHabit(request, user);
		return user.getId();
	}

	private void modifyUserHabit(
		UserModifyRequest request,
		User user
	) {
		if (request.notEmptyHabit()) {
			final List<UserHabit> userHabits = user.getHabits();
			// 기존 식습관 제거
			userHabitRepository.deleteAllByIdInBatch(getUserIdsFromHabits(user));
			userHabits.clear();

			// 신규 식습관 등록
			for (UserHabitRequest habitRequest : request.getHabit())
				userHabits.add(UserHabitRequest.createUserHabit(habitRequest, user));
			userHabitRepository.saveAll(userHabits);

			user.updateHabitContents(getHabitContents(request.getHabit()));
		}
	}

	private List<Long> getUserIdsFromHabits(User user) {
		return user.getHabits().stream().map(UserHabit::getId).collect(Collectors.toList());
	}

	private User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));
	}

	/**
	 * 회원 가입 검증
	 */
	public boolean verifyRegister(Long userId) {
		final User user = getUserById(userId);
		return user.getCountry() != null;
	}

	/**
	 * 회원 권한 조회
	 */
	public String findUserRole(Long userId) {
		final User user = userRepository.findById(userId).orElse(null);
		return user != null ? user.getRole().name() : Auth.ROLE_TEMP.name();
	}

	/**
	 * 회원 정보 조회
	 */
	public UserResponse findOne(Long userId) {
		final User user = getUserById(userId);
		final UserResponse userResponse = UserResponse.entityToDto(user);

		UserCount userCount = userRepository.getUserCount(userId);
		userCount.setReviewCount(reviewRepository.findRestaurantByUserForReview(userId).size());

		userResponse.updateCount(userCount);
		userResponse.updateHabits(getUserHabitResponses(user.getHabits()));
		return userResponse;
	}

	private List<UserHabitResponse> getUserHabitResponses(List<UserHabit> habits) {
		return habits != null && !habits.isEmpty() ? habits.stream()
			.map(UserHabitResponse::entityToDto)
			.collect(Collectors.toList()) : null;
	}

	/**
	 * 회원 스크랩 정보 조회
	 */
	public List<RestaurantListResponse> findScrapByUser(Long userId) {
		final User user = getUserById(userId);
		final List<RestaurantListResponse> restaurantListResponses
			= scrapRepository.findRestaurantByUserForScrap(user.getId());
		setIsLike(restaurantListResponses, user.getId());
		return restaurantListResponses;
	}

	/**
	 * 회원 게시물 정보 조회
	 */
	public List<RestaurantListResponse> findRestaurantByUser(Long userId) {
		final User user = getUserById(userId);
		final List<RestaurantListResponse> restaurantListResponses
			= restaurantRepository.findRestaurantByUser(user.getId());
		setIsLike(restaurantListResponses, user.getId());
		return restaurantListResponses;
	}

	/**
	 * 회원 리뷰단 게시물 정보 조회
	 */
	public List<RestaurantListResponse> findReviewByUser(Long userId) {
		final User user = getUserById(userId);
		final List<RestaurantListResponse> restaurantListResponses
			= reviewRepository.findRestaurantByUserForReview(user.getId());
		setIsLike(restaurantListResponses, user.getId());
		return restaurantListResponses;
	}

	private List<Long> getMyLikesIds(User user) {
		return likeRepository.findLikesByUser(user)
			.stream()
			.map(likes -> likes.getRestaurant().getId())
			.collect(Collectors.toList());
	}

	private void setIsLike(
		List<RestaurantListResponse> restaurantListResponses,
		Long userId
	) {
		final List<Long> likesIds = getMyLikesIds(getUserById(userId));
		restaurantListResponses.forEach(restaurant -> restaurant.setLike(likesIds.contains(restaurant.getId())));
	}

	@Transactional
	public Long removeUser(Long userId) {
		userRepository.findById(userId).ifPresent(userRepository::delete);
		return userId;
	}
}
