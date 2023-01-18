package com.toppings.common.constants.prop;

import java.util.List;

public class CorsProperties {

	public static List<String> prodUrls = List.of("https://toppings.co.kr", "https://admin.toppings.co.kr");

	public static List<String> prodDomains = List.of("toppings.co.kr", "admin.toppings.co.kr");

	public static List<String> devUrls = List.of("http://127.0.0.1:80", "http://127.0.0.1:3000",
		"http://localhost:3000", "https://toppings.co.kr", "http://toppings.co.kr", "https://dev.toppings.co.kr",
		"http://dev.toppings.co.kr", "http://admin.toppings.co.kr", "https://admin.toppings.co.kr",
		"https://06cb-14-52-187-234.jp.ngrok.io"
	);

	public static String[] getDevUrlStrings() {
		return new String[] {"http://127.0.0.1:80", "http://127.0.0.1:3000",
			"http://localhost:3000", "https://toppings.co.kr", "http://toppings.co.kr", "https://dev.toppings.co.kr",
			"http://dev.toppings.co.kr", "http://admin.toppings.co.kr", "https://admin.toppings.co.kr",
			"https://06cb-14-52-187-234.jp.ngrok.io"};
	}

	public static String[] getProdUrlStrings() {
		return new String[] {"https://toppings.co.kr", "https://admin.toppings.co.kr"};
	}
}
