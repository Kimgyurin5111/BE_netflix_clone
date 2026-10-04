package com.dslion.netflix_clone.mylist.controller;

import com.dslion.netflix_clone.auth.userdetails.CustomUserDetails;
import com.dslion.netflix_clone.content.dto.response.ContentResponse;
import com.dslion.netflix_clone.mylist.service.MyListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 찜(마이리스트) API - 로그인만 하면 누구나 사용 가능 (ADMIN 권한 불필요)
@Tag(name = "마이리스트", description = "콘텐츠 찜하기, 찜 해제, 내 마이리스트 조회")
@RestController
@RequestMapping("/api/my-list")
public class MyListController {

    @Autowired
    private MyListService myListService;

    @Operation(summary = "마이리스트에 추가", description = "콘텐츠를 내 마이리스트에 담습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "추가 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 콘텐츠"),
            @ApiResponse(responseCode = "409", description = "이미 마이리스트에 있는 콘텐츠")
    })
    @PostMapping("/{contentId}")
    public ResponseEntity<Void> add(
            @Parameter(description = "콘텐츠 id", example = "4") @PathVariable Long contentId,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        myListService.add(loginUser.getUserId(), contentId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "마이리스트에서 삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "마이리스트에 없는 콘텐츠")
    })
    @DeleteMapping("/{contentId}")
    public ResponseEntity<Void> remove(
            @Parameter(description = "콘텐츠 id", example = "4") @PathVariable Long contentId,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        myListService.remove(loginUser.getUserId(), contentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "내 마이리스트 조회", description = "내가 찜해둔 콘텐츠 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 필요")
    })
    @GetMapping
    public ResponseEntity<List<ContentResponse>> findMyList(
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        return ResponseEntity.ok(myListService.findMyList(loginUser.getUserId()));
    }
}
