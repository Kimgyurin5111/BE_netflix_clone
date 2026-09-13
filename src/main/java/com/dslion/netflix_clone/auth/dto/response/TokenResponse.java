package com.dslion.netflix_clone.auth.dto.response;

import lombok.Getter;
import lombok.Setter;

// 로그인 성공 후 발급되는 토큰
@Getter
@Setter
public class TokenResponse {

    private String accessToken;

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
