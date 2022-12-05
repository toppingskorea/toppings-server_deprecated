package com.toppings.server.domain.recent.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain.recent.constant.RecentType;
import com.toppings.server.domain.recent.dto.RecentRequest;
import com.toppings.server.domain.recent.dto.RecentResponse;
import com.toppings.server.domain.recent.entity.Recent;
import com.toppings.server.domain.recent.repository.RecentRepository;
import com.toppings.server.domain.restaurant.repository.RestaurantRepository;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecentService {

	private final RecentRepository recentRepository;

	private final UserRepository userRepository;

	private final RestaurantRepository restaurantRepository;

	/**
	 * 최근 검색어 등록
	 */
	@Transactional
	public RecentResponse register(
		RecentRequest recentRequest,
		Long userId
	) {
		if (RecentRequest.verifyRestaurantType(recentRequest))
			throw new GeneralException(ResponseCode.BAD_REQUEST);

		if (RecentRequest.verifySearchCategory(recentRequest))
			throw new GeneralException(ResponseCode.BAD_REQUEST);

		if (recentRequest.getRestaurantId() != null)
			verifyRestaurantId(recentRequest);

		final User user = getUserById(userId);
		recentRepository
			.findRecentByUserAndKeywordAndContent(user, recentRequest.getKeyword(), recentRequest.getContent())
			.ifPresent(recentRepository::delete);
		final Recent recent = recentRepository.save(RecentRequest.dtoToEntity(recentRequest, user));
		return RecentResponse.entityToDto(recent);
	}

	// TODO: public yn
	private void verifyRestaurantId(RecentRequest recentRequest) {
		restaurantRepository.findById(recentRequest.getRestaurantId())
			.orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));
	}

	/**
	 * 최근 검색어 목록 조회
	 */
	public List<RecentResponse> findAll(
		RecentType type,
		Long userId
	) {
		return recentRepository.findRecents(type, userId);
	}

	/**
	 * 최근 검색어 삭제
	 */
	@Transactional
	public Long removeOne(Long recentId) {
		recentRepository.deleteById(recentId);
		return recentId;
	}

	/**
	 * 최근 검색어 전체 삭제
	 */
	@Transactional
	public List<Long> removeAll(Long userId) {
		final User user = getUserById(userId);
		final List<Long> recentIds = recentRepository.findRecentByUser(user)
			.stream()
			.map(Recent::getId)
			.collect(Collectors.toList());
		recentRepository.deleteAllByUser(user);
		return recentIds;
	}

	private User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));
	}
}
