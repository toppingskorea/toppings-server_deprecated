package com.toppings.server;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("local")
@SpringBootTest
@DisplayName("NodamServerApplicationTests test")
class ServerApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	@DisplayName("1. 프로퍼티값 암호화 코드")
	void jasypt() {
		String value1 = "AKIA3ON22AZ6BCLLDQHQ";
		String value2 = "value2";
		System.out.println(jasyptEncoding(value1));
		System.out.println(jasyptEncoding(value2));
	}

	public String jasyptEncoding(String value) {
		String key = "emimanghamzz";
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
		pbeEnc.setAlgorithm("PBEWithMD5AndDES");
		pbeEnc.setPassword(key);
		return pbeEnc.encrypt(value);
	}
}
