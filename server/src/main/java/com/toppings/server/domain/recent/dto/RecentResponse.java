package com.toppings.server.domain.recent.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.toppings.server.domain.recent.constant.RecentType;
import com.toppings.server.domain.recent.entity.Recent;
import com.toppings.server.domain_global.constants.SearchCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecentResponse {

    private Long id;

    private String keyword;

    private String content;

    private RecentType type;

    private SearchCategory category;

    public static RecentResponse entityToDto(Recent recent) {
        return RecentResponse.builder()
            .id(recent.getId())
            .keyword(recent.getKeyword())
            .content(recent.getContent())
            .type(recent.getType())
            .category(recent.getCategory())
            .build();
    }
}
