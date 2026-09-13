package com.dslion.netflix_clone.content.dto.request;

import com.dslion.netflix_clone.content.entity.ContentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

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
}
