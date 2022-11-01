package com.toppings.common.dto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PageResultResponse<T> {

	private final List<T> items;
	private final int totalPage;
	private int page;
	private int size;
	private int start;
	private int end;
	private boolean prev;
	private boolean next;
	private final long totalElements;
	private List<Integer> pageList;

	public PageResultResponse(Page<T> result) {
		totalElements = result.getTotalElements();
		totalPage = result.getTotalPages();
		items = result.getContent();
		makePageList(result.getPageable(), 5);
	}

	public PageResultResponse(
		Page<T> result,
		int pageSize
	) {
		totalElements = result.getTotalElements();
		totalPage = result.getTotalPages();
		items = result.getContent();
		makePageList(result.getPageable(), pageSize);
	}

	public static <T> PageResultResponse<T> of(Page<T> data) {
		return new PageResultResponse<>(data);
	}

	private void makePageList(
		Pageable pageable,
		int pageSize
	) {
		this.page = pageable.getPageNumber() + 1;
		this.size = pageable.getPageSize();

		int tempEnd = (int)(Math.ceil(page / (pageSize * 1.0))) * pageSize;
		start = tempEnd - (pageSize - 1);
		prev = start > 1;
		end = Math.min(totalPage, tempEnd);
		next = totalPage > tempEnd;
		pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
	}
}
