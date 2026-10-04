package com.dslion.netflix_clone.watchhistory.controller;

import com.dslion.netflix_clone.auth.userdetails.CustomUserDetails;
import com.dslion.netflix_clone.watchhistory.dto.request.WatchPositionRequest;
import com.dslion.netflix_clone.watchhistory.dto.response.WatchHistoryResponse;
import com.dslion.netflix_clone.watchhistory.service.WatchHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 시청 기록 / 이어보기 API - 로그인만 하면 누구나 사용 가능 (ADMIN 권한 불필요)
@Tag(name = "시청 기록", description = "시청 위치 저장, 이어보기, 내 시청 기록 조회")
@RestController
@RequestMapping("/api/watch-history")
public class WatchHistoryController {

    @Autowired
    private WatchHistoryService watchHistoryService;

    @Operation(summary = "시청 위치 저장", description = "콘텐츠를 어디까지 봤는지(초 단위) 저장합니다. 처음이면 기록을 만들고, 이미 있으면 위치를 갱신합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "저장 성공"),
            @ApiResponse(responseCode = "400", description = "재생 위치가 비어있거나 음수이거나 콘텐츠 길이를 넘음"),
            @ApiResponse(responseCode = "401", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 콘텐츠")
    })
    @PutMapping("/{contentId}")
    public ResponseEntity<WatchHistoryResponse> savePosition(
            @PathVariable Long contentId,
            @Valid @RequestBody WatchPositionRequest request,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        return ResponseEntity.ok(
                watchHistoryService.savePosition(loginUser.getUserId(), contentId, request.getPosition())
        );
    }

    @Operation(summary = "내 시청 기록 조회", description = "내가 본 콘텐츠 목록을 최근 본 순으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 필요")
    })
    @GetMapping
    public ResponseEntity<List<WatchHistoryResponse>> findMyHistory(
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        return ResponseEntity.ok(watchHistoryService.findMyHistory(loginUser.getUserId()));
    }

    @Operation(summary = "이어보기 위치 조회", description = "특정 콘텐츠를 어디까지 봤는지(lastPosition, 초 단위) 조회합니다. 이 위치부터 이어서 재생하면 됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 콘텐츠이거나 시청 기록이 없음")
    })
    @GetMapping("/{contentId}")
    public ResponseEntity<WatchHistoryResponse> findResume(
            @PathVariable Long contentId,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        return ResponseEntity.ok(watchHistoryService.findResume(loginUser.getUserId(), contentId));
    }

    @Operation(summary = "시청 기록 삭제", description = "특정 콘텐츠의 시청 기록을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "시청 기록이 없음")
    })
    @DeleteMapping("/{contentId}")
    public ResponseEntity<Void> remove(
            @PathVariable Long contentId,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        watchHistoryService.remove(loginUser.getUserId(), contentId);
        return ResponseEntity.noContent().build();
    }
}
