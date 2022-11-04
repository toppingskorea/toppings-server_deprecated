package com.toppings.server.domain_global.config.security;

import java.util.Arrays;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.toppings.common.constants.prop.CorsProperties;
import com.toppings.server.domain.user.service.UserService;
import com.toppings.server.domain_global.config.security.handler.CustomLogoutSuccessHandler;
import com.toppings.server.domain_global.config.security.handler.OAuth2FailHandler;
import com.toppings.server.domain_global.config.security.handler.OAuth2SuccessHandler;
import com.toppings.server.domain_global.config.security.jwt.JwtProperties;
import com.toppings.server.domain_global.config.security.jwt.filter.JwtAuthenticationFilter;
import com.toppings.server.domain_global.config.security.jwt.filter.JwtAuthorizationFilter;
import com.toppings.server.domain_global.config.security.jwt.handler.JwtAuthenticationDeniedHandler;
import com.toppings.server.domain_global.config.security.jwt.handler.JwtAuthenticationEntryPoint;
import com.toppings.server.domain_global.config.security.oauth.PrincipalOauth2UserService;

import lombok.RequiredArgsConstructor;

/*
    # Spring Security 설정 클래스
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserService userService;

	private final CorsFilter corsFilter;

	private final Environment environment;

	private final PrincipalOauth2UserService principalOauth2UserService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors()
			.configurationSource(corsConfigurationSource())
			.and()
			.csrf()
			.disable()
			.formLogin()
			.disable()
			.httpBasic()
			.disable();

		http.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new JwtAuthenticationFilter(authenticationManager(), userService),
				UsernamePasswordAuthenticationFilter.class
			)
			.addFilterBefore(new JwtAuthorizationFilter(authenticationManager(), userService,
					Arrays.stream(environment.getActiveProfiles()).anyMatch(env -> env.equalsIgnoreCase("prod"))),
				BasicAuthenticationFilter.class
			);

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeRequests().anyRequest().permitAll();

		http.exceptionHandling()
			.accessDeniedHandler(new JwtAuthenticationDeniedHandler())
			.authenticationEntryPoint(new JwtAuthenticationEntryPoint());

		http.oauth2Login()
			.successHandler(oauth2SuccessHandler())
			.failureHandler(oauth2FailHandler())
			.userInfoEndpoint()
			.userService(principalOauth2UserService);

		http.logout()
			.logoutUrl("/logout")
			.logoutSuccessHandler(logoutSuccessHandler())
			.invalidateHttpSession(true)
			.deleteCookies(JwtProperties.JWT_REFRESH_HEADER);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new CustomLogoutSuccessHandler();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		if (Arrays.stream(environment.getActiveProfiles()).anyMatch(env -> env.equalsIgnoreCase("prod")))
			configuration.setAllowedOrigins(CorsProperties.prodUrls);
		else
			configuration.setAllowedOrigins(CorsProperties.devUrls);

		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public AuthenticationSuccessHandler oauth2SuccessHandler() {
		return new OAuth2SuccessHandler();
	}

	@Bean
	public AuthenticationFailureHandler oauth2FailHandler() {
		return new OAuth2FailHandler();
	}

}
