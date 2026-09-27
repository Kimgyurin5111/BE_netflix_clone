package com.dslion.netflix_clone.global.exception;

// 마이리스트에 없는 콘텐츠를 삭제하려고 할 때 발생시키는 예외
public class MyListNotFoundException extends RuntimeException {

    public MyListNotFoundException() {
        super("마이리스트에 없는 콘텐츠입니다.");
    }
}
