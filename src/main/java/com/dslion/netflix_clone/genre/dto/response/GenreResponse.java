package com.dslion.netflix_clone.genre.dto.response;

import com.dslion.netflix_clone.genre.entity.Genre;
import lombok.Getter;
import lombok.Setter;

// 장르 조회 시 클라이언트에게 내려주는 데이터
@Getter
@Setter
public class GenreResponse {

    private Long id;
    private String name;

    public static GenreResponse from(Genre genre) {
        GenreResponse response = new GenreResponse();
        response.setId(genre.getId());
        response.setName(genre.getName());
        return response;
    }
}
