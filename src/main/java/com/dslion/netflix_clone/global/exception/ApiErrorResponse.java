package com.dslion.netflix_clone.global.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

// 모든 에러 응답이 따르는 공통 형식
// 예: { "status": 404, "error": "Not Found", "message": "존재하지 않는 콘텐츠입니다.", "timestamp": "2026-10-04T12:00:00" }
@Getter
public class ApiErrorResponse {

    @Schema(description = "HTTP 상태 코드", example = "404")
    private final int status;

    @Schema(description = "HTTP 상태 이름", example = "Not Found")
    private final String error;

    @Schema(description = "에러 메시지", example = "존재하지 않는 콘텐츠입니다.")
    private final String message;

    @Schema(description = "에러 발생 시각")
    private final LocalDateTime timestamp;

    public ApiErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public static ApiErrorResponse of(HttpStatus status, String message) {
        return new ApiErrorResponse(status.value(), status.getReasonPhrase(), message);
    }
}
