package com.dslion.netflix_clone.global.exception;

// 이미 마이리스트에 있는 콘텐츠를 또 찜하려고 할 때 발생시키는 예외
public class DuplicateMyListException extends RuntimeException {

    public DuplicateMyListException() {
        super("이미 마이리스트에 있는 콘텐츠입니다.");
    }
}
