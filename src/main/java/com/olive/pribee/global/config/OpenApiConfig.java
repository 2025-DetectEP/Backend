package com.olive.pribee.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	private static final String BEARER_TOKEN_PREFIX = "bearer";
	private static String securityJwtName = "JWT";

	@Bean
	public OpenAPI customOpenAPI() {
		SecurityRequirement securityRequirement = new SecurityRequirement().addList(securityJwtName);

		return new OpenAPI()
				.addServersItem(new Server().url("/").description("현재 서버"))
				.components(apiComponents())
				.addSecurityItem(securityRequirement)
				.info(apiInfo());
	}

	private Info apiInfo() {
		return new Info()
				.title("Pribee API 명세서")
				.description("SNS 개인정보 탐지 보조 서비스 | Spring Boot를 이용한 REST API")
				.version("0.0.1")
				.contact(new io.swagger.v3.oas.models.info.Contact()
						.name("이예림")
						.url("https://github.com/yerim123456")
						.email("yearim1226@naver.com"));
	}

	private Components apiComponents() {
		return new Components()
				.addSecuritySchemes(securityJwtName, new SecurityScheme()
						.name(securityJwtName)
						.type(SecurityScheme.Type.HTTP)
						.scheme(BEARER_TOKEN_PREFIX)
						.bearerFormat(securityJwtName));
	}
}