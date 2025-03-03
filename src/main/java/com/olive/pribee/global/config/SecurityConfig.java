package com.olive.pribee.global.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.olive.pribee.global.common.filter.JwtAuthenticationFilter;
import com.olive.pribee.module.auth.handler.OAuth2AuthenticationFailureHandler;
import com.olive.pribee.module.auth.handler.OAuth2AuthenticationSuccessHandler;
import com.olive.pribee.module.auth.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	@Value("${front.url}")
	private String FRONT_URL;

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final OAuth2AuthenticationSuccessHandler successHandler;
	private final OAuth2AuthenticationFailureHandler failureHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(
					"/api/auth/token",
					"/oauth2/**",
					"/swagger-ui/**",
					"/webjars/**",
					"/swagger-ui.html",
					"/v3/api-docs/**").permitAll()
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

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		// 허용할 출처, HTTP 메서드, 헤더 설정 및 자격 증명 포함 설정
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of(FRONT_URL));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
		configuration.setAllowCredentials(true);

		// 특정 API 경로에 대해 CORS 정책을 적용
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/api/**", configuration);
		source.registerCorsConfiguration("/oauth2/**", configuration);
		source.registerCorsConfiguration("/swagger-ui/**", configuration);
		source.registerCorsConfiguration("/v3/api-docs/**", configuration);
		source.registerCorsConfiguration("/webjars/**", configuration);
		source.registerCorsConfiguration("/swagger-ui.html", configuration);

		return source;
	}
}
