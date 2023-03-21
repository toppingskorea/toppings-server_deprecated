package com.toppings.server.domain_global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Profile({"local", "dev"})
@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI openAPI() {

		Info info = new Info()
			.version("v1.0.0")
			.title("Toppings")
			.description("Toppings API Docs");

		return new OpenAPI()
			.info(info);
	}
}
