package com.dslion.netflix_clone.watchhistory.dto.response;

import com.dslion.netflix_clone.content.entity.Content;
import com.dslion.netflix_clone.watchhistory.entity.WatchHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// 시청 기록 조회 시 클라이언트에게 내려주는 데이터 (콘텐츠 요약 정보 + 마지막 시청 위치)
@Getter
@Setter
public class WatchHistoryResponse {

    @Schema(description = "콘텐츠 id", example = "4")
    private Long contentId;

    @Schema(description = "콘텐츠 제목", example = "대도시의 사랑법")
    private String title;

    @Schema(description = "포스터 이미지 경로", example = "/images/abc123.jpg")
    private String posterImageUrl;

    @Schema(description = "마지막으로 시청한 위치 (초 단위) - 이어보기 시작 지점", example = "1200")
    private Integer lastPosition;

    @Schema(description = "상영시간 (분 단위, 시리즈는 null)", example = "118")
    private Integer runningTime;

    @Schema(description = "마지막으로 시청한 시각")
    private LocalDateTime watchedAt;

    public static WatchHistoryResponse from(WatchHistory history, Content content) {
        WatchHistoryResponse response = new WatchHistoryResponse();
        response.setContentId(content.getId());
        response.setTitle(content.getTitle());
        response.setPosterImageUrl(content.getPosterImageUrl());
        response.setLastPosition(history.getLastPosition());
        response.setRunningTime(content.getRunningTime());
        response.setWatchedAt(history.getWatchedAt());
        return response;
    }
}
