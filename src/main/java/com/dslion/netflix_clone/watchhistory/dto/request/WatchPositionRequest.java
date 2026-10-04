package com.dslion.netflix_clone.watchhistory.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 시청 위치 저장 요청 데이터
@Getter
@Setter
@NoArgsConstructor
public class WatchPositionRequest {

    @NotNull(message = "재생 위치(초)를 입력해주세요.")
    @Min(value = 0, message = "재생 위치는 0 이상이어야 합니다.")
    @Schema(description = "마지막으로 시청한 위치 (초 단위)", example = "1200")
    private Integer position;
}
