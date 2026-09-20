package com.dslion.netflix_clone.content.dto.response;

import com.dslion.netflix_clone.content.entity.Content;
import com.dslion.netflix_clone.content.entity.ContentType;
import com.dslion.netflix_clone.genre.dto.response.GenreResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

// 콘텐츠 조회 시 클라이언트에게 내려주는 데이터
@Getter
@Setter
public class ContentResponse {

    private Long id;
    private String title;
    private String description;
    private ContentType type;
    private Integer releaseYear;
    private String posterImageUrl;
    private Integer runningTime;
    private Long createdBy;
    private LocalDateTime createdAt;
    private List<GenreResponse> genres;

    // 엔티티를 그대로 내려주지 않고, 화면에 필요한 형태로 변환해서 담는다
    public static ContentResponse from(Content content) {
        ContentResponse response = new ContentResponse();
        response.setId(content.getId());
        response.setTitle(content.getTitle());
        response.setDescription(content.getDescription());
        response.setType(content.getType());
        response.setReleaseYear(content.getReleaseYear());
        response.setPosterImageUrl(content.getPosterImageUrl());
        response.setRunningTime(content.getRunningTime());
        response.setCreatedBy(content.getCreatedBy());
        response.setCreatedAt(content.getCreatedAt());
        response.setGenres(
                content.getGenres().stream()
                        .map(GenreResponse::from)
                        .toList()
        );
        return response;
    }
}
