package com.toppings.server.domain_global.utils.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileConverter {

	public static File multipartFileToFile(MultipartFile file) throws IOException {
		File content = new File(Objects.requireNonNull(file.getOriginalFilename()));

		if (!content.exists()) {
			boolean newFile = content.createNewFile();
			log.info("is newFile : {}", newFile);
		}

		FileOutputStream fos = new FileOutputStream(content);
		fos.write(file.getBytes());
		fos.close();
		return content;
	}
}
