package com.toppings.server.domain.restaurant.service;

import static org.springframework.util.StringUtils.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.DatatypeConverter;

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
import com.toppings.server.domain.notification.constant.AlarmType;
import com.toppings.server.domain.notification.dto.AlarmRequest;
import com.toppings.server.domain.notification.service.AlarmService;
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
import com.toppings.server.domain_global.utils.s3.S3Response;
import com.toppings.server.domain_global.utils.s3.S3Uploader;

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

	private final AlarmService alarmService;

	private final S3Uploader s3Uploader;

	private final String imagePath = "restaurant/";


	/**
	 * 음식점 등록하기
	 */
	@Transactional
	public Long register(
		RestaurantRequest request,
		Long userId
	) {
		final User user = getUserById(userId);
		final Restaurant restaurant = restaurantRepository.findRestaurantByCode(request.getCode()).orElse(null);
		if (restaurant != null)
			throw new GeneralException(ResponseCode.DUPLICATED_ITEM);

		final Restaurant saveRestaurant = RestaurantRequest.dtoToEntity(request, user);
		final List<RestaurantAttach> images = getRestaurantAttaches(request.getImages(), request.getCode(), saveRestaurant);

		saveRestaurant.updateThumbnail(images.get(0).getImage());
		restaurantRepository.save(saveRestaurant);
		restaurantAttachRepository.saveAll(images);
		return saveRestaurant.getId();
	}

	private List<RestaurantAttach> getRestaurantAttaches(
		List<String> base64Images,
		String restaurantCode,
		Restaurant restaurant
	) {
		final List<RestaurantAttach> images = new ArrayList<>();
		for (String image : base64Images) {
			byte[] decodedFile = DatatypeConverter.parseBase64Binary(image.substring(image.indexOf(",") + 1));
			S3Response s3Response = s3Uploader.uploadBase64(decodedFile, imagePath + restaurantCode + "/");
			images.add(RestaurantAttach.of(s3Response, restaurant));
		}
		return images;
	}

	private User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));
	}

	/**
	 * 음식점 수정하기
	 */
	@Transactional
	public Long modify(
		RestaurantModifyRequest request,
		Long restaurantId,
		Long userId
	) {
		final Restaurant restaurant = getRestaurantById(restaurantId);
		if (verifyRestaurantAndUser(userId, restaurant))
			throw new GeneralException(ResponseCode.BAD_REQUEST);

		final List<RestaurantAttach> images = modifyRestaurantAttach(request, restaurant);

		RestaurantModifyRequest.setRestaurantInfo(request, restaurant, images.get(0).getImage());
		RestaurantModifyRequest.setMapInfo(request, restaurant);
		return restaurant.getId();
	}

	private List<RestaurantAttach> modifyRestaurantAttach(
		RestaurantModifyRequest request,
		Restaurant restaurant
	) {
		if (isNotNullImage(request.getImages())) {
			// 기존 이미지 제거
			restaurantAttachRepository.deleteAllByIdInBatch(
				restaurant.getImages().stream().map(RestaurantAttach::getId).collect(Collectors.toList()));

			// 신규 이미지 등록
			final List<RestaurantAttach> images
				= getRestaurantAttaches(request.getImages(), request.getCode(), restaurant);
			restaurantAttachRepository.saveAll(images);
			return images;
		} else {
			throw new GeneralException(ResponseCode.BAD_REQUEST);
		}
	}

	private boolean isNotNullImage(List<String> images) {
		return images != null && !images.isEmpty();
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
		alarmService.removeAlarm(restaurant);
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
		restaurant.setPublicYn(pubRequest.getIsPub() ? "Y" : "N");

		final AlarmType type = pubRequest.getIsPub() ? AlarmType.Pass : AlarmType.Reject;
		final AlarmRequest alarmRequest = AlarmRequest.of(restaurant, type);
		alarmService.registerAndSend(alarmRequest);

		return restaurantId;
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
