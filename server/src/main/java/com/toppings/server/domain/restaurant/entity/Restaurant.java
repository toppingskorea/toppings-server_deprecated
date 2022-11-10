package com.toppings.server.domain.restaurant.entity;

import com.toppings.server.domain.restaurant.constant.FoodType;
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
@Table(name = "t_restaurant")
public class Restaurant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    @Column(name = "restaurant_name", columnDefinition = "varchar(100)")
    private String name;

    @Column(name = "restaurant_description", columnDefinition = "text")
    private String description;

    @Column(name = "restaurant_address", columnDefinition = "varchar(150)")
    private String address;

    @Column(name = "restaurant_zipcode", columnDefinition = "varchar(50)")
    private String zipcode;

    @Column(name = "restaurant_latitude")
    private Double latitude;

    @Column(name = "restaurant_longitude")
    private Double longitude;

    @Column(name = "delete_yn", columnDefinition = "varchar(1) default 'N'")
    private String deleteYn;

    @Column(name = "restaurant_type", columnDefinition = "varchar(50)")
    private String type;

    @Column(name = "restaurant_like_count")
    private Integer likeCount;

    @Column(name = "restaurant_scrap_count")
    private Integer scrapCount;
    
    // 사진 경로랑 주소
    private String images;

    // 식습관 / 국가

    // 리뷰
}
