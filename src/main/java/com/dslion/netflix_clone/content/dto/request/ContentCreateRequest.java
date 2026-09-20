package com.dslion.netflix_clone.content.dto.request;

import com.dslion.netflix_clone.content.entity.ContentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// 콘텐츠 등록 요청
@Getter
@Setter
public class ContentCreateRequest {

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private ContentType type;

    private Integer releaseYear;

    private String posterImageUrl;

    private Integer runningTime;

    // 이 콘텐츠에 연결할 장르 id 목록 (없으면 장르 없이 등록됨)
    private List<Long> genreIds;
}
