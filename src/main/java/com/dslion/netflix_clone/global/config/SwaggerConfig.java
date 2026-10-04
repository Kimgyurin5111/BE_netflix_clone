package com.dslion.netflix_clone.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Swagger(API 명세서) 기본 정보 + 우측 상단 Authorize 버튼(JWT 입력) 설정
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        String jwtSchemeName = "bearerAuth";

        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        SecurityScheme jwtScheme = new SecurityScheme()
                .name(jwtSchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        Components components = new Components().addSecuritySchemes(jwtSchemeName, jwtScheme);

        Info info = new Info()
                .title("Netflix Clone API")
                .version("v1.0")
                .description("""
                        Spring Boot로 만든 넷플릭스 클론 백엔드 API 명세서입니다.

                        **인증 방법**
                        1. `POST /api/auth/signup`으로 회원가입, `POST /api/auth/login`으로 로그인해서 토큰을 받습니다.
                        2. 우측 상단 **Authorize** 버튼에 토큰만 붙여넣습니다. (`Bearer ` 는 붙이지 않습니다.)

                        **권한**
                        - 콘텐츠/장르 등록·수정·삭제, 이미지 업로드는 **관리자(ADMIN)** 만 가능합니다.
                        - 그 외 API(조회, 마이리스트, 시청 기록)는 로그인한 사용자라면 누구나 가능합니다.

                        **공통 에러 응답 형식**
                        ```
                        { "status": 404, "error": "Not Found", "message": "존재하지 않는 콘텐츠입니다.", "timestamp": "2026-10-04T12:00:00" }
                        ```
                        - 400: 잘못된 요청 / 401: 로그인 필요 / 403: 권한 없음 / 404: 대상 없음 / 409: 중복 / 500: 서버 오류
                        """);

        return new OpenAPI()
                .info(info)
                .components(components)
                .addSecurityItem(securityRequirement);
    }
}
