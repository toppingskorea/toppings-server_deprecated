package com.toppings.server.domain.restaurant.service;

import static org.springframework.util.StringUtils.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.dto.PubRequest;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain.likes.dto.LikesPercent;
import com.toppings.server.domain.likes.dto.LikesPercentResponse;
import com.toppings.server.domain.likes.repository.LikeRepository;
import com.toppings.server.domain.notification.constant.AlarmMessage;
import com.toppings.server.domain.notification.constant.AlarmType;
import com.toppings.server.domain.notification.entity.Alarm;
import com.toppings.server.domain.notification.repository.AlarmRepository;
import com.toppings.server.domain.restaurant.dto.RestaurantAttachRequest;
import com.toppings.server.domain.restaurant.dto.RestaurantFilterSearchRequest;
import com.toppings.server.domain.restaurant.dto.RestaurantListResponse;
import com.toppings.server.domain.restaurant.dto.RestaurantMapSearchRequest;
import com.toppings.server.domain.restaurant.dto.RestaurantModifyRequest;
import com.toppings.server.domain.restaurant.dto.RestaurantRequest;
import com.toppings.server.domain.restaurant.dto.RestaurantResponse;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.restaurant.entity.RestaurantAttach;
import com.toppings.server.domain.restaurant.repository.RestaurantAttachRepository;
import com.toppings.server.domain.restaurant.repository.RestaurantRepository;
import com.toppings.server.domain.review.repository.ReviewRepository;
import com.toppings.server.domain.scrap.repository.ScrapRepository;
import com.toppings.server.domain.user.constant.Auth;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain.user.repository.UserHabitRepository;
import com.toppings.server.domain.user.repository.UserRepository;
import com.toppings.server.domain_global.utils.notification.AlarmSender;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantService {

	private final RestaurantRepository restaurantRepository;

	private final UserRepository userRepository;

	private final RestaurantAttachRepository restaurantAttachRepository;

	private final LikeRepository likeRepository;

	private final UserHabitRepository userHabitRepository;

	private final ScrapRepository scrapRepository;

	private final ReviewRepository reviewRepository;

	private final AlarmRepository alarmRepository;

	private final AlarmSender alarmSender;

	/**
	 * 음식점 등록하기
	 */
	@Transactional
	public RestaurantResponse register(
		RestaurantRequest request,
		Long userId
	) {
		final User user = getUserById(userId);
		final Restaurant restaurant = restaurantRepository.findRestaurantByCode(request.getCode()).orElse(null);
		if (restaurant != null)
			throw new GeneralException(ResponseCode.DUPLICATED_ITEM);

		final Restaurant saveRestaurant = restaurantRepository.save(RestaurantRequest.dtoToEntity(request, user));
		List<String> images = registerRestaurantAttach(request, saveRestaurant);

		final RestaurantResponse restaurantResponse = RestaurantResponse.entityToDto(saveRestaurant);
		restaurantResponse.setImages(images);
		return restaurantResponse;
	}

	private List<String> registerRestaurantAttach(
		RestaurantRequest request,
		Restaurant restaurant
	) {
		final List<RestaurantAttach> restaurantAttaches = new ArrayList<>();
		for (String image : request.getImages())
			restaurantAttaches.add(RestaurantAttachRequest.dtoToEntity(image, restaurant));
		restaurantAttachRepository.saveAll(restaurantAttaches);

		return restaurantAttaches.stream()
			.map(RestaurantAttach::getImage)
			.collect(Collectors.toList());
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
		Long restaurantId,
		Long userId
	) {
		final Restaurant restaurant = getRestaurantById(restaurantId);
		if (verifyRestaurantAndUser(userId, restaurant))
			throw new GeneralException(ResponseCode.BAD_REQUEST);

		final List<String> images = modifyRestaurantAttach(request, restaurant);
		final RestaurantResponse restaurantResponse = RestaurantResponse.entityToDto(restaurant);
		restaurantResponse.setImages(images);

		RestaurantModifyRequest.setRestaurantInfo(request, restaurant, images.get(0));
		RestaurantModifyRequest.setMapInfo(request, restaurant);
		return restaurantResponse;
	}

	private List<String> modifyRestaurantAttach(
		RestaurantModifyRequest request,
		Restaurant restaurant
	) {
		final List<RestaurantAttach> restaurantAttaches = new ArrayList<>();
		if (request.getImages() != null && !request.getImages().isEmpty()) {
			// 기존 이미지 제거
			restaurantAttachRepository.deleteAllByIdInBatch(
				restaurant.getImages().stream().map(RestaurantAttach::getId).collect(Collectors.toList()));

			// 신규 이미지 등록
			for (String image : request.getImages())
				restaurantAttaches.add(RestaurantAttachRequest.dtoToEntity(image, restaurant));
			restaurantAttachRepository.saveAll(restaurantAttaches);
		} else {
			throw new GeneralException(ResponseCode.BAD_REQUEST);
		}

		return restaurantAttaches.stream()
			.map(RestaurantAttach::getImage)
			.collect(Collectors.toList());
	}

	private boolean verifyRestaurantAndUser(
		User user,
		Restaurant restaurant
	) {
		return !user.getRole().equals(Auth.ROLE_ADMIN) && !restaurant.getUser().getId().equals(user.getId());
	}

	private boolean verifyRestaurantAndUser(
		Long userId,
		Restaurant restaurant
	) {
		return !restaurant.getUser().getId().equals(userId);
	}

	// TODO: public yn
	private Restaurant getRestaurantById(Long id) {
		return restaurantRepository.findById(id)
			.orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));
	}

	/**
	 * 음식점 삭제하기
	 */
	@Transactional
	public Long remove(
		Long restaurantId,
		Long userId
	) {
		final Restaurant restaurant = getRestaurantById(restaurantId);
		final User user = getUserById(userId);
		if (verifyRestaurantAndUser(user, restaurant))
			throw new GeneralException(ResponseCode.BAD_REQUEST);

		reviewRepository.deleteBatchByRestaurant(restaurant);
		likeRepository.deleteBatchByRestaurant(restaurant);
		scrapRepository.deleteBatchByRestaurant(restaurant);
		restaurantRepository.delete(restaurant);
		return restaurantId;
	}

	/**
	 *	음식점 목록 검색 (필터)
	 */
	public List<RestaurantListResponse> findAllForFilter(
		RestaurantFilterSearchRequest searchRequest,
		Long userId
	) {
		final List<RestaurantListResponse> restaurantListResponses;
		switch (searchRequest.getType()) {
			case Name:
				restaurantListResponses = getRestaurantListResponsesForName(searchRequest);
				break;

			case Habit:
				restaurantListResponses = getRestaurantListResponsesForHabit(searchRequest);
				break;

			case Country:
				restaurantListResponses = getRestaurantListResponsesforCountry(searchRequest);
				break;

			default:
				restaurantListResponses = Collections.emptyList();
		}

		if (userId != null)
			setIsLike(restaurantListResponses, userId);
		return restaurantListResponses;
	}

	/**
	 *	음식점 목록 검색 (지도)
	 */
	public List<RestaurantListResponse> findAllForMap(
		RestaurantMapSearchRequest searchRequest,
		Long userId
	) {
		final List<RestaurantListResponse> restaurantListResponses
			= restaurantRepository.findAllBySearchForMap(searchRequest);
		if (userId != null)
			setIsLike(restaurantListResponses, userId);
		return restaurantListResponses;
	}

	private void setIsLike(
		List<RestaurantListResponse> restaurantListResponses,
		Long userId
	) {
		final List<Long> likesIds = getMyLikesIds(getUserById(userId));
		restaurantListResponses.forEach(restaurant -> restaurant.setLike(likesIds.contains(restaurant.getId())));
	}

	private List<RestaurantListResponse> getRestaurantListResponsesforCountry(RestaurantFilterSearchRequest searchRequest) {
		if (isNullCountry(searchRequest))
			throw new GeneralException(ResponseCode.BAD_REQUEST);

		return likeRepository.findRestaurantIdByUserCountry(searchRequest.getCountry());
	}

	private List<RestaurantListResponse> getRestaurantListResponsesForHabit(RestaurantFilterSearchRequest searchRequest) {
		if (isNullHabit(searchRequest))
			throw new GeneralException(ResponseCode.BAD_REQUEST);

		List<Long> ids = userHabitRepository.findUserIdByHabit(searchRequest);
		return likeRepository.findRestaurantIdByUserHabit(ids);
	}

	private List<RestaurantListResponse> getRestaurantListResponsesForName(RestaurantFilterSearchRequest searchRequest) {
		if (isNullName(searchRequest))
			throw new GeneralException(ResponseCode.BAD_REQUEST);

		return restaurantRepository.findAllByRestaurantName(searchRequest.getName());
	}

	private boolean isNullCountry(RestaurantFilterSearchRequest searchRequest) {
		return searchRequest.getCountry() == null || !hasText(searchRequest.getCountry());
	}

	private boolean isNullHabit(RestaurantFilterSearchRequest searchRequest) {
		return searchRequest.getHabit() == null;
	}

	private boolean isNullName(RestaurantFilterSearchRequest searchRequest) {
		return searchRequest.getName() == null || !hasText(searchRequest.getName());
	}

	private List<Long> getMyLikesIds(User user) {
		return likeRepository.findLikesByUser(user)
			.stream()
			.map(likes -> likes.getRestaurant().getId())
			.collect(Collectors.toList());
	}

	/**
	 *	음식점 상세 조회
	 */
	public RestaurantResponse findOne(
		Long restaurantId,
		Long userId
	) {
		final Restaurant restaurant = getRestaurantById(restaurantId);
		final RestaurantResponse restaurantResponse = RestaurantResponse.entityToDto(restaurant);

		final List<String> images = getRestaurantImages(restaurant);
		restaurantResponse.setImages(images);
		restaurantResponse.setWriter(restaurant.getUser().getName());
		restaurantResponse.setCountry(restaurant.getUser().getCountry());

		if (userId != null) {
			User user = getUserById(userId);
			restaurantResponse.setLike(likeRepository.findLikesByRestaurantAndUser(restaurant, user).isPresent());
			restaurantResponse.setScrap(scrapRepository.findScrapByRestaurantAndUser(restaurant, user).isPresent());
		}
		return restaurantResponse;
	}

	private List<String> getRestaurantImages(Restaurant restaurant) {
		return restaurant.getImages()
			.stream()
			.map(RestaurantAttach::getImage)
			.collect(Collectors.toList());
	}

	/**
	 *	좋아요 퍼센트 조회
	 */
	public LikesPercentResponse getLikesPercent(Long restaurantId) {
		final Restaurant restaurant = getRestaurantById(restaurantId);
		final Long totalCount = likeRepository.countByRestaurant(restaurant);

		final List<LikesPercent> countryLikePercents = likeRepository.findLikesPercentForCountry(restaurantId);
		final List<LikesPercent> habitLikePercents = likeRepository.findLikesPercentForHabit(restaurantId);

		setCountryLikesPercent(totalCount, countryLikePercents);
		setHabitLikesPercent(totalCount, habitLikePercents);

		return LikesPercentResponse.builder()
			.countryPercent(countryLikePercents)
			.habitPercent(habitLikePercents)
			.build();
	}

	private void setHabitLikesPercent(
		Long totalCount,
		List<LikesPercent> habitLikePercents
	) {
		habitLikePercents.forEach(habitLikes -> {
			double divisionValue = habitLikes.getCount() / (double)totalCount;
			habitLikes.setPercent(Math.toIntExact(Math.round(divisionValue * 100)));
		});
	}

	private void setCountryLikesPercent(
		Long totalCount,
		List<LikesPercent> countryLikePercents
	) {
		countryLikePercents.forEach(countryLikes -> {
			double divisionValue = countryLikes.getCount() / (double)totalCount;
			countryLikes.setPercent(Math.toIntExact(Math.round(divisionValue * 100)));
		});
	}

	/**
	 * 음식점 공개여부 수정 (관리자용)
	 */
	@Transactional
	public Long modifyPub(
		PubRequest pubRequest,
		Long restaurantId
	) {
		final Restaurant restaurant = getRestaurantById(restaurantId);
		restaurant.setPublicYn(pubRequest.getIsPub());

		final User alarmUser = restaurant.getUser();
		saveAndSendAlarm(restaurant, alarmUser);

		return restaurantId;
	}

	private void saveAndSendAlarm(
		Restaurant restaurant,
		User alarmUser
	) {
		final Alarm alarm = Alarm.of(alarmUser, restaurant, null, AlarmType.Reject);
		final Alarm savedAlarm = alarmRepository.save(alarm);
		alarmSender.send(restaurant, alarmUser, savedAlarm, AlarmMessage.RejectMessage.getMessage());
	}

	/**
	 * 음식점 목록 조회 (관리자용)
	 */
	public Page<RestaurantListResponse> findAllForAdmin(Pageable pageable) {
		return restaurantRepository.findAllForAdmin(pageable);
	}

	/**
	 * 음식점 상세 조회 (관리자용)
	 */
	public RestaurantResponse findOneForAdmin(Long restaurantId) {
		final Restaurant restaurant = getRestaurantById(restaurantId);
		final RestaurantResponse restaurantResponse = RestaurantResponse.entityToDto(restaurant);

		final List<String> images = getRestaurantImages(restaurant);
		restaurantResponse.setImages(images);
		return restaurantResponse;
	}
}
