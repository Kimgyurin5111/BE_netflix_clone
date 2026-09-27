package com.dslion.netflix_clone.image.service;

import com.dslion.netflix_clone.global.exception.InvalidImageFileException;
import com.dslion.netflix_clone.image.dto.response.ImageUploadResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

// 이미지를 서버(프로젝트 루트의 uploads 폴더)에 저장하는 로직
// 외부 스토리지(S3 등) 없이 로컬 파일 저장 방식으로 가장 기초적인 수준으로 구현
@Service
public class ImageService {

    // 프로젝트 루트 기준 uploads 폴더에 저장 (.gitignore에 추가되어 있어서 커밋되지 않음)
    private static final String UPLOAD_DIR = "uploads/";

    public ImageUploadResponse upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidImageFileException("업로드할 파일이 비어있습니다.");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 파일명이 겹치지 않도록 UUID로 새 이름을 만들어서 저장
        String savedFilename = UUID.randomUUID() + extension;

        // 상대 경로("uploads/")를 그대로 넘기면 transferTo()가 프로젝트 폴더가 아니라
        // 톰캣 임시 디렉토리 기준으로 경로를 해석해서 존재하지 않는 경로에 저장을 시도하다가 실패한다.
        // (지금 400 에러가 나는 원인) 그래서 절대 경로로 변환한 뒤 사용해야 한다.
        File uploadDir = new File(UPLOAD_DIR).getAbsoluteFile();
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        try {
            File destination = new File(uploadDir, savedFilename);
            file.transferTo(destination);
        } catch (IOException e) {
            throw new InvalidImageFileException("이미지 저장 중 오류가 발생했습니다.");
        }

        // WebConfig에서 /images/** 요청을 uploads 폴더로 서빙하도록 설정해둠
        String imageUrl = "/images/" + savedFilename;
        return new ImageUploadResponse(imageUrl);
    }
}
