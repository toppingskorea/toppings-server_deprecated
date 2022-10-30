package com.toppings.common.request;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class RequestIpProvider {

	private static final List<String> ipHeaders = List.of(
		"Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR");

	public static String getIp(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-Forwarded-For");
		for (String header : ipHeaders) {
			if (isIpNull(ipAddress))
				ipAddress = request.getHeader(header);
		}

		return ipAddress == null
			? request.getRemoteAddr()
			: ipAddress;
	}

	private static boolean isIpNull(String ip) {
		return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
	}
}
