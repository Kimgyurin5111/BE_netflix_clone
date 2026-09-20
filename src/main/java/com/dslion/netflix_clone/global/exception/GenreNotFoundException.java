package com.dslion.netflix_clone.global.exception;

// 존재하지 않는 장르를 조회하려고 할 때 발생시키는 예외
public class GenreNotFoundException extends RuntimeException {

    public GenreNotFoundException() {
        super("존재하지 않는 장르입니다.");
    }
}
