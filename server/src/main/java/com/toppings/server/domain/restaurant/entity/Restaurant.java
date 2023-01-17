package com.toppings.server.domain.restaurant.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.toppings.server.domain.restaurant.constant.FoodType;
import com.toppings.server.domain.restaurant.dto.RestaurantModifyRequest;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain_global.entity.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

	@Column(name = "restaurant_code", columnDefinition = "varchar(200)", unique = true)
	private String code;

	@Column(name = "delete_yn", columnDefinition = "varchar(1) default 'N'")
	private String deleteYn;

	@Enumerated(EnumType.STRING)
	@Column(name = "restaurant_type", columnDefinition = "varchar(50)")
	private FoodType type;

	@Column(name = "restaurant_like_count", columnDefinition = "int default 0")
	private Integer likeCount;

	@Column(name = "restaurant_scrap_count", columnDefinition = "int default 0")
	private Integer scrapCount;

	@Column(name = "public_yn", columnDefinition = "varchar(1) default 'P'")
	private String publicYn;

	// 사진 경로랑 주소
	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<RestaurantAttach> images = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@JsonIgnoreProperties({"restaurants"})
	@ToString.Exclude
	@JsonIgnore
	private User user;

	@Column(name = "restaurant_thumbnail", columnDefinition = "longtext")
	private String thumbnail;

	// business
	public void updateThumbnail(String image) {
		if (!image.equals(this.thumbnail))
			this.thumbnail = image;
	}

	public void upLikeCount() {
		this.likeCount += 1;
	}

	public void downLikeCount() {
		if (likeCount > 0)
			this.likeCount -= 1;
	}

	public void upScrapCount() {
		this.scrapCount += 1;
	}

	public void downScrapCount() {
		if (scrapCount > 0)
			this.scrapCount -= 1;
	}

	public void updatePublicYn(boolean isPub) {
		this.publicYn = isPub ? "Y" : "N";
	}

	public void updateRestaurantInfo(
		RestaurantModifyRequest request,
		String image
	) {
		this.name = request.getName() != null ? request.getName() : this.name;
		this.address = request.getAddress() != null ? request.getAddress() : this.address;
		this.description = request.getDescription() != null ? request.getDescription() : this.description;
		this.type = request.getType() != null ? request.getType() : this.type;
		this.thumbnail = image;
	}

	public void updateMapInfo(RestaurantModifyRequest request) {
		this.zipcode = request.getZipcode() != null ? request.getZipcode() : this.zipcode;
		this.code = request.getCode() != null ? request.getCode() : this.code;
		this.latitude = request.getLatitude() != null ? request.getLatitude() : this.latitude;
		this.longitude = request.getLongitude() != null ? request.getLongitude() : this.longitude;
	}
}
