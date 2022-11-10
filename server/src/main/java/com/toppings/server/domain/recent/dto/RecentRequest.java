package com.toppings.server.domain.recent.dto;

import com.toppings.server.domain.recent.constant.RecentType;
import com.toppings.server.domain.recent.entity.Recent;
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

    private Long id;

    private String keyword;

    private String content;

    private RecentType type;

    private SearchCategory category;

    public static Recent dtoToEntity(RecentRequest request) {
        return Recent.builder()
                .keyword(request.getKeyword())
                .content(request.getContent())
                .type(request.getType())
                .category(request.getCategory())
                .build();
    }
}
