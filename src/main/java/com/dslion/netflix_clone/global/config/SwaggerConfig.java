package com.dslion.netflix_clone.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Swagger 우측 상단에 Authorize 버튼을 띄우기 위한 설정
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

        return new OpenAPI()
                .components(components)
                .addSecurityItem(securityRequirement);
    }
}
