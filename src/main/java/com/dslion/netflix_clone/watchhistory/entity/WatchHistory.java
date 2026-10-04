package com.dslion.netflix_clone.watchhistory.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// watch_history 테이블과 매핑되는 시청 기록 엔티티
// 사용자 한 명이 콘텐츠 하나에 대해 기록을 하나만 가진다 (같은 콘텐츠를 다시 보면 위치만 갱신)
// 마이리스트와 마찬가지로 연관관계 없이 id만 저장해서 단순하게 간다
@Entity
@Table(
        name = "watch_history",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "content_id"})
)
@Getter
@Setter
@NoArgsConstructor
public class WatchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "content_id", nullable = false)
    private Long contentId;

    // 마지막으로 시청한 위치 (초 단위) -> 이어보기에 사용
    @Column(nullable = false)
    private Integer lastPosition;

    // 마지막으로 시청한 시각 -> 최근 본 순으로 정렬할 때 사용
    @Column(nullable = false)
    private LocalDateTime watchedAt;

    public WatchHistory(Long userId, Long contentId, Integer lastPosition) {
        this.userId = userId;
        this.contentId = contentId;
        this.lastPosition = lastPosition;
        this.watchedAt = LocalDateTime.now();
    }
}
