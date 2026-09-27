package com.dslion.netflix_clone.image.controller;

import com.dslion.netflix_clone.image.dto.response.ImageUploadResponse;
import com.dslion.netflix_clone.image.service.ImageService;
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
@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    // consumes를 명시적으로 지정해줘야 Swagger에서 multipart/form-data(파일 업로드) 입력창이 제대로 뜬다
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageUploadResponse> upload(@RequestParam("file") MultipartFile file) {
        ImageUploadResponse response = imageService.upload(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
