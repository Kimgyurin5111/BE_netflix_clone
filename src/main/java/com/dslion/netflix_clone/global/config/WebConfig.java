package com.dslion.netflix_clone.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 업로드된 이미지 파일을 /images/** 경로로 서빙하기 위한 설정
// 예: /images/abc123.jpg 로 요청하면 프로젝트 루트의 uploads/abc123.jpg 파일을 내려준다
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:uploads/");
    }
}
