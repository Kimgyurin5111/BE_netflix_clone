package com.dslion.netflix_clone.content.service;

import com.dslion.netflix_clone.content.dto.request.ContentCreateRequest;
import com.dslion.netflix_clone.content.dto.request.ContentUpdateRequest;
import com.dslion.netflix_clone.content.dto.response.ContentResponse;
import com.dslion.netflix_clone.content.entity.Content;
import com.dslion.netflix_clone.content.repository.ContentRepository;
import com.dslion.netflix_clone.global.exception.ContentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// 콘텐츠 등록/수정/삭제/조회의 실제 처리 로직
@Service
public class ContentService {

    @Autowired
    private ContentRepository contentRepository;

    // 콘텐츠 등록 (userId = 로그인한 사람의 id, createdBy로 저장)
    public ContentResponse create(ContentCreateRequest request, Long userId) {
        Content content = new Content(
                request.getTitle(),
                request.getDescription(),
                request.getType(),
                request.getReleaseYear(),
                request.getPosterImageUrl(),
                request.getRunningTime(),
                userId
        );

        Content saved = contentRepository.save(content);
        return ContentResponse.from(saved);
    }

    // 콘텐츠 목록 조회
    public List<ContentResponse> findAll() {
        List<Content> contents = contentRepository.findAll();
        return contents.stream()
                .map(ContentResponse::from)
                .toList();
    }

    // 콘텐츠 단건 조회 (수정 화면 들어가기 전에 이 API로 기존 값을 먼저 불러오면 된다)
    public ContentResponse findById(Long id) {
        Content content = findContentOrThrow(id);
        return ContentResponse.from(content);
    }

    // 콘텐츠 수정
    // 요청에 값이 들어있는 항목만 바꾸고, null인 항목은 기존 값을 그대로 둔다
    public ContentResponse update(Long id, ContentUpdateRequest request) {
        Content content = findContentOrThrow(id);

        if (request.getTitle() != null) {
            content.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            content.setDescription(request.getDescription());
        }
        if (request.getType() != null) {
            content.setType(request.getType());
        }
        if (request.getReleaseYear() != null) {
            content.setReleaseYear(request.getReleaseYear());
        }
        if (request.getPosterImageUrl() != null) {
            content.setPosterImageUrl(request.getPosterImageUrl());
        }
        if (request.getRunningTime() != null) {
            content.setRunningTime(request.getRunningTime());
        }

        Content saved = contentRepository.save(content);
        return ContentResponse.from(saved);
    }

    // 콘텐츠 삭제
    public void delete(Long id) {
        Content content = findContentOrThrow(id);
        contentRepository.delete(content);
    }

    private Content findContentOrThrow(Long id) {
        return contentRepository.findById(id)
                .orElseThrow(ContentNotFoundException::new);
    }
}
