package com.dslion.netflix_clone.image.controller;

import com.dslion.netflix_clone.image.dto.response.ImageUploadResponse;
import com.dslion.netflix_clone.image.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

// 이미지 업로드 API - 콘텐츠 등록/수정 시 쓸 포스터 이미지를 올릴 때 사용
// SecurityConfig에서 ADMIN 권한만 가능하도록 제한되어 있다
@Tag(name = "이미지", description = "포스터 이미지 업로드 (관리자만)")
@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    // consumes를 명시적으로 지정해줘야 Swagger에서 multipart/form-data(파일 업로드) 입력창이 제대로 뜬다
    @Operation(summary = "이미지 업로드 (관리자)", description = "이미지 파일을 업로드하고 접근 경로(imageUrl)를 돌려받습니다. 받은 imageUrl을 콘텐츠 등록/수정 시 posterImageUrl에 넣으세요. 업로드된 이미지는 http://localhost:8080 + imageUrl 로 누구나 볼 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "업로드 성공 (imageUrl 반환)"),
            @ApiResponse(responseCode = "400", description = "파일이 비어있거나 저장 중 오류 발생"),
            @ApiResponse(responseCode = "401", description = "로그인 필요"),
            @ApiResponse(responseCode = "403", description = "관리자 권한 필요"),
            @ApiResponse(responseCode = "413", description = "파일 크기 초과")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageUploadResponse> upload(@RequestParam("file") MultipartFile file) {
        ImageUploadResponse response = imageService.upload(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
