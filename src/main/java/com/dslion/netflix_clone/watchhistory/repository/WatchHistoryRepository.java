package com.dslion.netflix_clone.watchhistory.repository;

import com.dslion.netflix_clone.watchhistory.entity.WatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {

    // 특정 사용자의 특정 콘텐츠 시청 기록 조회 (이어보기, 위치 갱신에 사용)
    Optional<WatchHistory> findByUserIdAndContentId(Long userId, Long contentId);

    // 특정 사용자의 시청 기록을 최근 본 순으로 조회
    List<WatchHistory> findByUserIdOrderByWatchedAtDesc(Long userId);
}
