package com.toppings.server.domain_global.utils.s3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class S3Response {

	private String imageRealName;

	private String imageUrl;

	private String imagePath;

	private String imageName;

	public static S3Response createEmptyResponse() {
		return S3Response.builder().build();
	}
}
