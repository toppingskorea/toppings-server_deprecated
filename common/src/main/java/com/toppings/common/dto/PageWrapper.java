package com.toppings.common.dto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true, value = {"pageable"})
public class PageWrapper<T> extends PageImpl<T> {

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public PageWrapper(
		@JsonProperty("content") List<T> content,
		@JsonProperty("number") int page,
		@JsonProperty("size") int size,
		@JsonProperty("totalElements") long total
	) {
		super(content, PageRequest.of(page, size), total);
	}

	public PageWrapper(Page<T> page) {
		super(page.getContent(), page.getPageable(), page.getTotalElements());
	}
}
