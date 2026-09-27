package com.dslion.netflix_clone.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 컨트롤러에서 발생한 예외를 한 곳에서 잡아서 에러 메시지로 응답해주는 클래스
// (정식 전역 예외 처리는 5주차에서 더 다듬을 예정, 지금은 최소한으로만)
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 이미 가입된 이메일 -> 409 Conflict
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<String> handleDuplicateEmail(DuplicateEmailException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    // 로그인 실패 -> 401 Unauthorized
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentials(InvalidCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    // 존재하지 않는 콘텐츠 -> 404 Not Found
    @ExceptionHandler(ContentNotFoundException.class)
    public ResponseEntity<String> handleContentNotFound(ContentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    // 이미 있는 장르 이름 -> 409 Conflict
    @ExceptionHandler(DuplicateGenreNameException.class)
    public ResponseEntity<String> handleDuplicateGenreName(DuplicateGenreNameException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    // 존재하지 않는 장르 -> 404 Not Found
    @ExceptionHandler(GenreNotFoundException.class)
    public ResponseEntity<String> handleGenreNotFound(GenreNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    // 이미 마이리스트에 있는 콘텐츠 -> 409 Conflict
    @ExceptionHandler(DuplicateMyListException.class)
    public ResponseEntity<String> handleDuplicateMyList(DuplicateMyListException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    // 마이리스트에 없는 콘텐츠 -> 404 Not Found
    @ExceptionHandler(MyListNotFoundException.class)
    public ResponseEntity<String> handleMyListNotFound(MyListNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    // 잘못된 이미지 파일 (비어있음, 저장 실패 등) -> 400 Bad Request
    @ExceptionHandler(InvalidImageFileException.class)
    public ResponseEntity<String> handleInvalidImageFile(InvalidImageFileException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    // @Valid 검증 실패 (제목/이름이 비어있다 등) -> 400 Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
