package com.dslion.netflix_clone.content.dto.request;

import com.dslion.netflix_clone.content.entity.ContentType;
import lombok.Getter;
import lombok.Setter;

// 콘텐츠 수정 요청
// 값을 채운 항목만 반영되고, 비워둔(null) 항목은 기존 값 그대로 유지된다
@Getter
@Setter
public class ContentUpdateRequest {

    private String title;
    private String description;
    private ContentType type;
    private Integer releaseYear;
    private String posterImageUrl;
    private Integer runningTime;
}
