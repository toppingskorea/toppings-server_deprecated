package com.toppings.server.domain_global.config.web;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.toppings.common.constants.prop.CorsProperties;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

	private final Environment environment;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		if (Arrays.stream(environment.getActiveProfiles()).anyMatch(env -> env.equalsIgnoreCase("prod"))) {
			registry.addMapping("/**")
				.allowedOrigins(CorsProperties.getProdUrlStrings())
				.allowedMethods("*")
				.allowedHeaders("*")
				.allowCredentials(true)
				.maxAge(3600);
		} else {
			registry.addMapping("/**")
				.allowedOrigins(CorsProperties.getDevUrlStrings())
				.allowedMethods("*")
				.allowedHeaders("*")
				.allowCredentials(true)
				.maxAge(3600);
		}
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		if (Arrays.stream(environment.getActiveProfiles()).anyMatch(env -> env.equalsIgnoreCase("prod")))
			config.setAllowedOrigins(CorsProperties.prodUrls);
		else
			config.setAllowedOrigins(CorsProperties.devUrls);
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.setMaxAge(3600L);
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
}
