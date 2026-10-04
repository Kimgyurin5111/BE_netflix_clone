package com.dslion.netflix_clone.watchhistory.service;

import com.dslion.netflix_clone.content.entity.Content;
import com.dslion.netflix_clone.content.repository.ContentRepository;
import com.dslion.netflix_clone.global.exception.ContentNotFoundException;
import com.dslion.netflix_clone.global.exception.InvalidWatchPositionException;
import com.dslion.netflix_clone.global.exception.WatchHistoryNotFoundException;
import com.dslion.netflix_clone.watchhistory.dto.response.WatchHistoryResponse;
import com.dslion.netflix_clone.watchhistory.entity.WatchHistory;
import com.dslion.netflix_clone.watchhistory.repository.WatchHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 시청 기록 저장 / 이어보기 조회 / 내 시청 기록 조회 / 삭제의 실제 처리 로직
@Service
public class WatchHistoryService {

    @Autowired
    private WatchHistoryRepository watchHistoryRepository;

    @Autowired
    private ContentRepository contentRepository;

    // 시청 위치 저장 (처음 보면 새로 만들고, 이미 기록이 있으면 위치와 시각만 갱신)
    public WatchHistoryResponse savePosition(Long userId, Long contentId, Integer position) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(ContentNotFoundException::new);

        // 영화처럼 상영시간(분)이 정해진 콘텐츠는 그 길이를 넘는 위치를 저장할 수 없다
        // (상영시간은 분 단위, 위치는 초 단위라서 * 60 해서 비교)
        if (content.getRunningTime() != null && position > content.getRunningTime() * 60L) {
            throw new InvalidWatchPositionException();
        }

        WatchHistory history = watchHistoryRepository.findByUserIdAndContentId(userId, contentId)
                .orElse(null);

        if (history == null) {
            history = new WatchHistory(userId, contentId, position);
        } else {
            history.setLastPosition(position);
            history.setWatchedAt(LocalDateTime.now());
        }

        watchHistoryRepository.save(history);
        return WatchHistoryResponse.from(history, content);
    }

    // 내 시청 기록 전체 조회 (최근 본 순)
    public List<WatchHistoryResponse> findMyHistory(Long userId) {
        List<WatchHistory> histories = watchHistoryRepository.findByUserIdOrderByWatchedAtDesc(userId);
        List<WatchHistoryResponse> result = new ArrayList<>();

        for (WatchHistory history : histories) {
            Content content = contentRepository.findById(history.getContentId()).orElse(null);
            // 이미 삭제된 콘텐츠의 기록은 목록에서 건너뛴다
            if (content != null) {
                result.add(WatchHistoryResponse.from(history, content));
            }
        }
        return result;
    }

    // 이어보기: 특정 콘텐츠를 어디까지 봤는지 조회 (기록이 없으면 예외)
    public WatchHistoryResponse findResume(Long userId, Long contentId) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(ContentNotFoundException::new);
        WatchHistory history = watchHistoryRepository.findByUserIdAndContentId(userId, contentId)
                .orElseThrow(WatchHistoryNotFoundException::new);

        return WatchHistoryResponse.from(history, content);
    }

    // 시청 기록 삭제
    public void remove(Long userId, Long contentId) {
        WatchHistory history = watchHistoryRepository.findByUserIdAndContentId(userId, contentId)
                .orElseThrow(WatchHistoryNotFoundException::new);
        watchHistoryRepository.delete(history);
    }
}
