package com.olive.pribee.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.olive.pribee.global.common.filter.JwtAuthenticationFilter;
import com.olive.pribee.module.auth.handler.OAuth2AuthenticationFailureHandler;
import com.olive.pribee.module.auth.handler.OAuth2AuthenticationSuccessHandler;
import com.olive.pribee.module.auth.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final OAuth2AuthenticationSuccessHandler successHandler;
	private final OAuth2AuthenticationFailureHandler failureHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/api/auth/token",
					"/oauth2/**",
					"/v3/api-docs/**",
					"/swagger-ui/**",
					"/swagger-ui.html").permitAll()
				.anyRequest().authenticated()
			)

			// OAuth2 로그인 설정
			.oauth2Login(oauth2 -> oauth2
				.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
				.successHandler(successHandler)
				.failureHandler(failureHandler)
			)
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
