package com.dslion.netflix_clone.global.exception;

// 비어있는 파일을 올리거나, 이미지 저장 중 문제가 생겼을 때 발생시키는 예외
public class InvalidImageFileException extends RuntimeException {

    public InvalidImageFileException(String message) {
        super(message);
    }
}
