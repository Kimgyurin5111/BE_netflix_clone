package com.dslion.netflix_clone.genre.controller;

import com.dslion.netflix_clone.content.dto.response.ContentResponse;
import com.dslion.netflix_clone.genre.dto.request.GenreCreateRequest;
import com.dslion.netflix_clone.genre.dto.response.GenreResponse;
import com.dslion.netflix_clone.genre.service.GenreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 장르 등록/조회 API
// 등록은 SecurityConfig에서 ADMIN 권한만 가능하도록 제한되어 있다 (조회는 로그인한 누구나 가능)
@RestController
@RequestMapping("/api/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    // 장르 등록
    @PostMapping
    public ResponseEntity<GenreResponse> create(@Valid @RequestBody GenreCreateRequest request) {
        GenreResponse response = genreService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 장르 목록 조회
    @GetMapping
    public ResponseEntity<List<GenreResponse>> findAll() {
        return ResponseEntity.ok(genreService.findAll());
    }

    // 특정 장르에 속한 콘텐츠 목록 조회
    @GetMapping("/{id}/contents")
    public ResponseEntity<List<ContentResponse>> findContentsByGenre(@PathVariable Long id) {
        return ResponseEntity.ok(genreService.findContentsByGenre(id));
    }
}
