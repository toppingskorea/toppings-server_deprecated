package com.toppings.server.domain.recent.entity;

import com.toppings.server.domain.recent.constant.RecentType;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain_global.constants.SearchCategory;
import com.toppings.server.domain_global.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@DynamicUpdate
@DynamicInsert
@Table(name = "t_recent_search")
public class Recent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recent_id")
    private Long id;

    @Column(name = "recent_keyword", columnDefinition = "varchar(200)")
    private String keyword;

    @Column(name = "recent_content", columnDefinition = "text")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "recent_type", columnDefinition = "varchar(50)")
    private RecentType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "recent_category", columnDefinition = "varchar(50)")
    private SearchCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long restaurantId;
}
