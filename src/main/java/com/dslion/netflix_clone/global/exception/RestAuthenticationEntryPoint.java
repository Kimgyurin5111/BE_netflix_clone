package com.dslion.netflix_clone.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;

// 로그인(토큰) 없이 보호된 API를 호출했을 때 401 + 공통 에러 형식(JSON)으로 응답
// (Security 필터 단계에서 발생하는 에러라서 GlobalExceptionHandler가 못 잡기 때문에 따로 만든다)
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        String body = "{\"status\":401,"
                + "\"error\":\"Unauthorized\","
                + "\"message\":\"로그인이 필요합니다. (토큰이 없거나 유효하지 않습니다.)\","
                + "\"timestamp\":\"" + LocalDateTime.now() + "\"}";
        response.getWriter().write(body);
    }
}
