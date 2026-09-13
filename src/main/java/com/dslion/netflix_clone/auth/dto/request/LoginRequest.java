package com.dslion.netflix_clone.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// 로그인 요청 시 클라이언트가 보내는 데이터
@Getter
@Setter
public class LoginRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
