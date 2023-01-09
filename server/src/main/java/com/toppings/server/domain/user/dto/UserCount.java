package com.toppings.server.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCount {

	private Integer postCount;

	private Integer scrapCount;

	private Integer reviewCount;
}
