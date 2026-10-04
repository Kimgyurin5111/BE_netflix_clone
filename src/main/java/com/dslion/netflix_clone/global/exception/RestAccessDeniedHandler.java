package com.dslion.netflix_clone.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;

// 로그인은 했지만 권한이 없는 API(예: 일반 회원이 관리자 API)를 호출했을 때 403 + 공통 에러 형식(JSON)으로 응답
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        String body = "{\"status\":403,"
                + "\"error\":\"Forbidden\","
                + "\"message\":\"접근 권한이 없습니다.\","
                + "\"timestamp\":\"" + LocalDateTime.now() + "\"}";
        response.getWriter().write(body);
    }
}
