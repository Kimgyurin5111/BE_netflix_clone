package com.dslion.netflix_clone.image.dto.response;

import lombok.Getter;
import lombok.Setter;

// 이미지 업로드 응답 - 저장된 이미지의 URL을 내려줌 (콘텐츠 등록 시 posterImageUrl로 사용하면 됨)
@Getter
@Setter
public class ImageUploadResponse {

    private String imageUrl;

    public ImageUploadResponse(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
