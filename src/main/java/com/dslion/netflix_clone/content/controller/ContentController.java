package com.dslion.netflix_clone.content.controller;

import com.dslion.netflix_clone.auth.userdetails.CustomUserDetails;
import com.dslion.netflix_clone.content.dto.request.ContentCreateRequest;
import com.dslion.netflix_clone.content.dto.request.ContentUpdateRequest;
import com.dslion.netflix_clone.content.dto.response.ContentResponse;
import com.dslion.netflix_clone.content.service.ContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 콘텐츠(영화/시리즈) CRUD API
// 등록/수정/삭제는 SecurityConfig에서 ADMIN 권한만 가능하도록 제한되어 있다 (조회는 로그인한 누구나 가능)
@Tag(name = "콘텐츠", description = "영화/시리즈 등록·조회·검색·수정·삭제 (등록/수정/삭제는 관리자만)")
@RestController
@RequestMapping("/api/contents")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @Operation(summary = "콘텐츠 등록 (관리자)", description = "영화/시리즈를 등록합니다. 포스터는 먼저 POST /api/images로 업로드한 뒤 받은 imageUrl을 posterImageUrl에 넣으세요.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "입력값이 올바르지 않음"),
            @ApiResponse(responseCode = "401", description = "로그인 필요"),
            @ApiResponse(responseCode = "403", description = "관리자 권한 필요")
    })
    @PostMapping
    public ResponseEntity<ContentResponse> create(
            @Valid @RequestBody ContentCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        ContentResponse response = contentService.create(request, loginUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 예: /api/contents?page=0&size=10&sort=releaseYear,desc
    // page, size, sort를 아무것도 안 넘기면 0페이지 / 10개씩 / 정렬 없음으로 조회됨
    @Operation(summary = "콘텐츠 목록 조회 (페이징 + 정렬)", description = "page(0부터 시작), size(기본 10), sort(예: releaseYear,desc)로 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 필요")
    })
    @GetMapping
    public ResponseEntity<Page<ContentResponse>> findAll(
            @ParameterObject @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(contentService.findAll(pageable));
    }

    @Operation(summary = "콘텐츠 단건 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 필요"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 콘텐츠")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ContentResponse> findById(
            @Parameter(description = "콘텐츠 id", example = "1") @PathVariable Long id
    ) {
        return ResponseEntity.ok(contentService.findById(id));
    }

    @Operation(summary = "콘텐츠 제목 검색", description = "제목에 검색어가 포함된 콘텐츠를 대소문자 구분 없이 검색합니다. (예: /api/contents/search?keyword=범죄)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "검색 성공 (결과가 없으면 빈 목록)"),
            @ApiResponse(responseCode = "400", description = "keyword 파라미터 누락"),
            @ApiResponse(responseCode = "401", description = "로그인 필요")
    })
    @GetMapping("/search")
    public ResponseEntity<List<ContentResponse>> search(
            @Parameter(description = "검색어", example = "범죄") @RequestParam String keyword
    ) {
        return ResponseEntity.ok(contentService.search(keyword));
    }

    @Operation(summary = "콘텐츠 수정 (관리자)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "400", description = "입력값이 올바르지 않음"),
            @ApiResponse(responseCode = "401", description = "로그인 필요"),
            @ApiResponse(responseCode = "403", description = "관리자 권한 필요"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 콘텐츠")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ContentResponse> update(
            @Parameter(description = "콘텐츠 id", example = "1") @PathVariable Long id,
            @Valid @RequestBody ContentUpdateRequest request
    ) {
        return ResponseEntity.ok(contentService.update(id, request));
    }

    @Operation(summary = "콘텐츠 삭제 (관리자)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 필요"),
            @ApiResponse(responseCode = "403", description = "관리자 권한 필요"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 콘텐츠")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "콘텐츠 id", example = "1") @PathVariable Long id
    ) {
        contentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
