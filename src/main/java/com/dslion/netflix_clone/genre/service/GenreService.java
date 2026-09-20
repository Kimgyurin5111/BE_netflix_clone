package com.dslion.netflix_clone.genre.service;

import com.dslion.netflix_clone.content.dto.response.ContentResponse;
import com.dslion.netflix_clone.content.entity.Content;
import com.dslion.netflix_clone.content.repository.ContentRepository;
import com.dslion.netflix_clone.genre.dto.request.GenreCreateRequest;
import com.dslion.netflix_clone.genre.dto.response.GenreResponse;
import com.dslion.netflix_clone.genre.entity.Genre;
import com.dslion.netflix_clone.genre.repository.GenreRepository;
import com.dslion.netflix_clone.global.exception.DuplicateGenreNameException;
import com.dslion.netflix_clone.global.exception.GenreNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// 장르 등록/조회의 실제 처리 로직
@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ContentRepository contentRepository;

    // 장르 등록 (이름 중복이면 예외)
    public GenreResponse create(GenreCreateRequest request) {
        if (genreRepository.existsByName(request.getName())) {
            throw new DuplicateGenreNameException();
        }

        Genre genre = new Genre(request.getName());
        Genre saved = genreRepository.save(genre);
        return GenreResponse.from(saved);
    }

    // 장르 전체 목록 조회
    public List<GenreResponse> findAll() {
        return genreRepository.findAll().stream()
                .map(GenreResponse::from)
                .toList();
    }

    // 특정 장르에 속한 콘텐츠 목록 조회
    public List<ContentResponse> findContentsByGenre(Long genreId) {
        if (!genreRepository.existsById(genreId)) {
            throw new GenreNotFoundException();
        }

        List<Content> contents = contentRepository.findByGenres_Id(genreId);
        return contents.stream()
                .map(ContentResponse::from)
                .toList();
    }
}
