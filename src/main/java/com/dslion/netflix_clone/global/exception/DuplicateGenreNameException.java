package com.dslion.netflix_clone.global.exception;

// 이미 있는 장르 이름으로 등록을 시도했을 때 발생시키는 예외
public class DuplicateGenreNameException extends RuntimeException {

    public DuplicateGenreNameException() {
        super("이미 존재하는 장르 이름입니다.");
    }
}
