package com.dslion.netflix_clone.content.controller;

import com.dslion.netflix_clone.auth.userdetails.CustomUserDetails;
import com.dslion.netflix_clone.content.dto.request.ContentCreateRequest;
import com.dslion.netflix_clone.content.dto.request.ContentUpdateRequest;
import com.dslion.netflix_clone.content.dto.response.ContentResponse;
import com.dslion.netflix_clone.content.service.ContentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 콘텐츠(영화/시리즈) CRUD API
// 등록/수정/삭제는 SecurityConfig에서 ADMIN 권한만 가능하도록 제한되어 있다 (조회는 로그인한 누구나 가능)
@RestController
@RequestMapping("/api/contents")
public class ContentController {

    @Autowired
    private ContentService contentService;

    // 콘텐츠 등록
    @PostMapping
    public ResponseEntity<ContentResponse> create(
            @Valid @RequestBody ContentCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        ContentResponse response = contentService.create(request, loginUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 콘텐츠 목록 조회
    @GetMapping
    public ResponseEntity<List<ContentResponse>> findAll() {
        return ResponseEntity.ok(contentService.findAll());
    }

    // 콘텐츠 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ContentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(contentService.findById(id));
    }

    // 콘텐츠 수정
    @PutMapping("/{id}")
    public ResponseEntity<ContentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ContentUpdateRequest request
    ) {
        return ResponseEntity.ok(contentService.update(id, request));
    }

    // 콘텐츠 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
