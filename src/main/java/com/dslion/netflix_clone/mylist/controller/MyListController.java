package com.dslion.netflix_clone.mylist.controller;

import com.dslion.netflix_clone.auth.userdetails.CustomUserDetails;
import com.dslion.netflix_clone.content.dto.response.ContentResponse;
import com.dslion.netflix_clone.mylist.service.MyListService;
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
@RestController
@RequestMapping("/api/my-list")
public class MyListController {

    @Autowired
    private MyListService myListService;

    // 마이리스트에 추가
    @PostMapping("/{contentId}")
    public ResponseEntity<Void> add(
            @PathVariable Long contentId,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        myListService.add(loginUser.getUserId(), contentId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 마이리스트에서 삭제
    @DeleteMapping("/{contentId}")
    public ResponseEntity<Void> remove(
            @PathVariable Long contentId,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        myListService.remove(loginUser.getUserId(), contentId);
        return ResponseEntity.noContent().build();
    }

    // 내 마이리스트 조회
    @GetMapping
    public ResponseEntity<List<ContentResponse>> findMyList(
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        return ResponseEntity.ok(myListService.findMyList(loginUser.getUserId()));
    }
}
