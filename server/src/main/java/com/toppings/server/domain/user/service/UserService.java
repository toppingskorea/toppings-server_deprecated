package com.toppings.server.domain.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain.likes.repository.LikeRepository;
import com.toppings.server.domain.restaurant.dto.RestaurantResponse;
import com.toppings.server.domain.review.repository.ReviewRepository;
import com.toppings.server.domain.scrap.repository.ScrapRepository;
import com.toppings.server.domain.user.constant.Auth;
import com.toppings.server.domain.user.dto.UserHabitRequest;
import com.toppings.server.domain.user.dto.UserHabitResponse;
import com.toppings.server.domain.user.dto.UserModifyRequest;
import com.toppings.server.domain.user.dto.UserRegisterRequest;
import com.toppings.server.domain.user.dto.UserResponse;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain.user.entity.UserHabit;
import com.toppings.server.domain.user.repository.UserHabitRepository;
import com.toppings.server.domain.user.repository.UserRepository;

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

	/**
	 * 회원 가입
	 */
	@Transactional
	public UserResponse register(
		UserRegisterRequest request,
		Long userId
	) {
		User user = getUserById(userId);
		if (user.getCountry() != null)
			throw new GeneralException(ResponseCode.DUPLICATED_USER);

		user.setCountry(request.getCountry());
		user.setRole(Auth.ROLE_USER);
		List<UserHabitResponse> userHabitResponses = registerUserHabit(request, user);
		UserResponse userResponse = UserResponse.entityToDto(user);
		userResponse.setHabits(userHabitResponses);
		return userResponse;
	}

	private List<UserHabitResponse> registerUserHabit(
		UserRegisterRequest request,
		User user
	) {
		List<UserHabit> userHabits = new ArrayList<>();
		for (UserHabitRequest habitRequest : request.getHabit())
			userHabits.add(UserHabitRequest.createUserHabit(habitRequest, user));

		userHabitRepository.saveAll(userHabits);
		return userHabits.stream()
			.map(UserHabitResponse::entityToDto)
			.collect(Collectors.toList());
	}

	/**
	 * 회원 정보 수정
	 */
	@Transactional
	public UserResponse modify(
		UserModifyRequest request,
		Long userId
	) {
		User user = getUserById(userId);
		UserModifyRequest.modifyUserInfo(request, user);
		List<UserHabitResponse> userHabitResponses = modifyUserHabit(request, user);

		UserResponse userResponse = UserResponse.entityToDto(user);
		userResponse.setHabits(userHabitResponses);
		return userResponse;
	}

	private List<UserHabitResponse> modifyUserHabit(
		UserModifyRequest request,
		User user
	) {
		// 기존 식습관 제거
		userHabitRepository.deleteAllByIdInBatch(
			user.getHabits().stream().map(UserHabit::getId).collect(Collectors.toList()));

		// 신규 식습관 등록
		List<UserHabit> userHabits = new ArrayList<>();
		if (request.getHabit() != null && !request.getHabit().isEmpty()) {
			for (UserHabitRequest habitRequest : request.getHabit())
				userHabits.add(UserHabitRequest.createUserHabit(habitRequest, user));
			userHabitRepository.saveAll(userHabits);
		}

		return userHabits.stream()
			.map(UserHabitResponse::entityToDto)
			.collect(Collectors.toList());
	}

	private User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));
	}

	/**
	 * 회원 가입 검증
	 */
	public boolean verifyRegister(Long userId) {
		User user = getUserById(userId);
		return user.getCountry() != null;
	}

	/**
	 * 회원 권한 조회
	 */
	public String findUserRole(Long userId) {
		User user = getUserById(userId);
		return user.getRole().name();
	}


	/**
	 * 회원 정보 조회
	 */
	public UserResponse findOne(Long userId) {
		User user = getUserById(userId);
		List<UserHabit> userHabits = user.getHabits();
		UserResponse userResponse = UserResponse.entityToDto(user);
		userResponse.setHabits(userHabits.stream()
			.map(UserHabitResponse::entityToDto)
			.collect(Collectors.toList()));
		return userResponse;
	}

	/**
	 * 회원 스크랩 정보 조회
	 */
	public List<RestaurantResponse> findScrapByUser(Long userId) {


		return null;
	}

	/**
	 * 회원 게시물 정보 조회
	 */
	public List<RestaurantResponse> findRestaurantByUser(Long userId) {
		return null;
	}

	/**
	 * 회원 리뷰단 게시물 정보 조회
	 */
	public List<RestaurantResponse> findReviewByUser(Long userId) {
		return null;
	}

	@Transactional
	public Long removeUser(Long userId) {
		userRepository.deleteById(userId);
		return userId;
	}
}
