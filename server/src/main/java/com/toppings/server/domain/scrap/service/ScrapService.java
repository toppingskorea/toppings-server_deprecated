package com.toppings.server.domain.scrap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.server.domain.scrap.repository.ScrapRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScrapService {

	private final ScrapRepository scrapRepository;

	public Object register(
		Long restaurantId,
		Long userId
	) {

		return null;
	}

	public Object remove(
		Long restaurantId,
		Long userId
	) {

		return null;
	}
}
