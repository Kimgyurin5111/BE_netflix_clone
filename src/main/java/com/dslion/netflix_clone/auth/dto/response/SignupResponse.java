package com.dslion.netflix_clone.auth.dto.response;

import lombok.Getter;
import lombok.Setter;

// 회원가입 성공 후 클라이언트에게 보여줄 데이터 (비밀번호는 절대 포함하지 않음)
@Getter
@Setter
public class SignupResponse {

    private Long id;
    private String email;
    private String nickname;

    public SignupResponse(Long id, String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }
}
