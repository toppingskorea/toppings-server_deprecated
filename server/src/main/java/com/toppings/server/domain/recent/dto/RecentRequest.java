package com.toppings.server.domain.recent.dto;

import static org.springframework.util.StringUtils.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.toppings.server.domain.recent.constant.RecentType;
import com.toppings.server.domain.recent.entity.Recent;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain_global.constants.SearchCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecentRequest {

	private Long restaurantId;

	@NotBlank(message = "검색어를 확인해주세요.")
	private String keyword;

	private String content;

	@NotNull(message = "타입을 확인해주세요.")
	private RecentType type;

	private SearchCategory category;

	public static Recent dtoToEntity(
		RecentRequest request,
		User user
	) {
		return Recent.builder()
			.keyword(request.getKeyword())
			.content(request.getContent())
			.type(request.getType())
			.category(request.getCategory())
			.user(user)
			.restaurantId(request.getRestaurantId())
			.build();
	}

	public static boolean verifyRestaurantType(RecentRequest recentRequest) {
		return recentRequest.getType().equals(RecentType.Restaurant) && recentRequest.getCategory() == null;
	}

	public static boolean verifySearchCategory(RecentRequest recentRequest) {
		return recentRequest.getCategory().equals(SearchCategory.Name) && recentRequest.getRestaurantId() == null
			&& hasText(recentRequest.getContent());
	}
}
