package com.dslion.netflix_clone.global.exception;

// 존재하지 않는 콘텐츠를 조회/수정/삭제하려고 할 때 발생시키는 예외
public class ContentNotFoundException extends RuntimeException {

    public ContentNotFoundException() {
        super("존재하지 않는 콘텐츠입니다.");
    }
}
