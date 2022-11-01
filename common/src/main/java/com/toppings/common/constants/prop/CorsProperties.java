package com.toppings.common.constants.prop;

import java.util.List;

public class CorsProperties {

	public static List<String> prodUrls = List.of("https://jjaltoonrumble.bigpicture-games.com",
		"https://admin-rumble.bigpicture-games.com");

	public static List<String> prodDomains = List.of("jjaltoonrumble.bigpicture-games.com",
		"admin-rumble.bigpicture-games.com");

	public static List<String> devUrls = List.of("http://127.0.0.1:80", "http://127.0.0.1:3000",
		"http://localhost:3000", "https://jjaltoonrumble.bigpicture-games.com",
		"https://admin-rumble.bigpicture-games.com", "https://pilot-rumble.bigpicture-games.com",
		"https://pilot-admin-rumble.bigpicture-games.com", "http://192.168.0.3:3000"
	);

	public static String[] getDevUrlStrings() {
		return new String[] {"http://127.0.0.1:80", "http://127.0.0.1:3000", "http://localhost:3000",
			"https://jjaltoonrumble.bigpicture-games.com", "https://admin-rumble.bigpicture-games.com",
			"https://pilot-rumble.bigpicture-games.com", "https://pilot-admin-rumble.bigpicture-games.com",
			"http://192.168.0.3:3000"};
	}

	public static String[] getProdUrlStrings() {
		return new String[] {"https://jjaltoonrumble.bigpicture-games.com",
			"https://admin-rumble.bigpicture-games.com",};
	}
}
