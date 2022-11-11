package com.toppings.server.domain.recent.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain.recent.constant.RecentType;
import com.toppings.server.domain.recent.dto.RecentRequest;
import com.toppings.server.domain.recent.dto.RecentResponse;
import com.toppings.server.domain.recent.entity.Recent;
import com.toppings.server.domain.recent.repository.RecentRepository;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecentService {

	private final RecentRepository recentRepository;

	private final UserRepository userRepository;

	@Transactional
	public Long register(
		RecentRequest recentRequest,
		Long id
	) {
		if (RecentRequest.verifyRestaurantType(recentRequest))
			throw new GeneralException(ResponseCode.BAD_REQUEST);

		User user = getUserById(id);
		recentRepository
			.findRecentByRecentRequest(recentRequest, user)
			.ifPresent(recentRepository::delete);

		Recent dbRecent = recentRepository.save(RecentRequest.dtoToEntity(recentRequest, user));
		return dbRecent.getId();
	}

	public List<RecentResponse> getRecents(
		RecentType type,
		Long id
	) {
		return recentRepository.findRecents(type, id);
	}

	@Transactional
	public Long removeOneRecent(Long id) {
		recentRepository.deleteById(id);
		return id;
	}

	@Transactional
	public Boolean removeAllRecent(Long id) {
		User user = getUserById(id);
		recentRepository.deleteAllByUser(user);
		return true;
	}

	private User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));
	}
}
