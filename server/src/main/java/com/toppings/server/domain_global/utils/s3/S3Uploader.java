package com.toppings.server.domain_global.utils.s3;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.toppings.common.constants.ResponseCode;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain_global.utils.file.FileConverter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3Uploader {

	private final AmazonS3 amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	public String bucket;

	private static final String BASE_PATH = "image/";

	public S3Response uploadBase64(
		byte[] file,
		String path
	) {
		if (file == null)
			throw new GeneralException(ResponseCode.BAD_REQUEST);

		String imageName = UUID.randomUUID().toString();
		String imagePath = BASE_PATH + path + imageName;

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.length);
		metadata.setContentType("image/jpeg");

		amazonS3Client.putObject(
			new PutObjectRequest(bucket, imagePath, new ByteArrayInputStream(file), metadata).withCannedAcl(
				CannedAccessControlList.PublicRead));

		return createS3Response(null, imageName, imagePath);
	}

	public S3Response uploadMultipartFile(
		MultipartFile file,
		String path
	) {
		if (file == null)
			throw new GeneralException(ResponseCode.BAD_REQUEST);

		String imageName = getChangedImageName(file.getOriginalFilename());
		String imagePath = BASE_PATH + path + imageName;

		try {
			uploadFile(file, imagePath);
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new GeneralException(ResponseCode.INTERNAL_ERROR);
		}

		return createS3Response(file, imageName, imagePath);
	}

	private S3Response createS3Response(
		MultipartFile file,
		String imageName,
		String imagePath
	) {
		return S3Response.builder()
			.imageRealName(file != null ? file.getOriginalFilename() : null)
			.imageName(imageName)
			.imagePath(imagePath)
			.imageUrl(amazonS3Client.getUrl(bucket, imagePath).toString())
			.build();
	}

	private void uploadFile(
		MultipartFile file,
		String imagePath
	) throws IOException {
		File content = FileConverter.multipartFileToFile(file);
		amazonS3Client.putObject(
			new PutObjectRequest(bucket, imagePath, content).withCannedAcl(CannedAccessControlList.PublicRead));
		boolean delete = content.delete();
		log.info("is deleted : {}", delete);
	}

	private String getChangedImageName(String fileName) {
		Date date = new Date();
		StringBuilder stringBuilder = new StringBuilder(UUID.randomUUID().toString().split("-")[0]);
		stringBuilder.append("_");
		stringBuilder.append(date.getTime());
		stringBuilder.append("_");
		stringBuilder.append(fileName);
		return stringBuilder.toString();
	}

	public void deleteImage(String path) {
		try {
			amazonS3Client.deleteObject(bucket, path);
		} catch (Exception exception) {
			throw new GeneralException(ResponseCode.BAD_REQUEST);
		}
	}
}
