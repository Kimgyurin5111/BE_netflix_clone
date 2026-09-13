package com.dslion.netflix_clone.global.exception;

// 이메일 또는 비밀번호가 틀렸을 때 발생시키는 예외
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("이메일 또는 비밀번호가 올바르지 않습니다.");
    }
}
