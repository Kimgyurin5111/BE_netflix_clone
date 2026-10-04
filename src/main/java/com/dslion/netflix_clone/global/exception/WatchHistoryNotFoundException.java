package com.dslion.netflix_clone.global.exception;

// 시청 기록이 없을 때 던지는 예외
public class WatchHistoryNotFoundException extends RuntimeException {

    public WatchHistoryNotFoundException() {
        super("시청 기록이 없는 콘텐츠입니다.");
    }
}
