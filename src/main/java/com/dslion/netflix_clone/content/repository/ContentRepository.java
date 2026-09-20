package com.dslion.netflix_clone.content.repository;

import com.dslion.netflix_clone.content.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {

    // 특정 장르(genreId)가 달린 콘텐츠 목록 조회
    List<Content> findByGenres_Id(Long genreId);

    // 제목에 검색어가 포함된 콘텐츠 목록 조회 (대소문자 구분 없이)
    List<Content> findByTitleContainingIgnoreCase(String keyword);
}
