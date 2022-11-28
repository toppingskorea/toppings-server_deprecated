package com.toppings.server.domain.likes.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain_global.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@DynamicUpdate
@DynamicInsert
@Table(name = "t_likes")
public class Likes extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"likes"})
    @ToString.Exclude
    @JsonIgnore
    private User user;
}
