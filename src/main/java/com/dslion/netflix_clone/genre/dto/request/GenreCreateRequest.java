package com.dslion.netflix_clone.genre.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// 장르 등록 요청
@Getter
@Setter
public class GenreCreateRequest {

    @NotBlank
    private String name;
}
