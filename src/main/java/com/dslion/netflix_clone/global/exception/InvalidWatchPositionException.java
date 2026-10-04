package com.dslion.netflix_clone.global.exception;

// 재생 위치가 콘텐츠 길이를 넘을 때 던지는 예외
public class InvalidWatchPositionException extends RuntimeException {

    public InvalidWatchPositionException() {
        super("재생 위치가 콘텐츠의 상영시간을 넘을 수 없습니다.");
    }
}
