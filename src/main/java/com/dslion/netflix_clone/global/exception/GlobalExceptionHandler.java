package com.dslion.netflix_clone.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

// 컨트롤러에서 발생한 예외를 한 곳에서 잡아서 공통 형식(ApiErrorResponse)으로 응답해주는 클래스
// 응답 예: { "status": 404, "error": "Not Found", "message": "존재하지 않는 콘텐츠입니다.", "timestamp": "..." }
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 상태 코드와 메시지를 받아서 공통 에러 응답을 만들어주는 도우미 메서드
    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(ApiErrorResponse.of(status, message));
    }

    // ===== 우리가 직접 만든 예외 =====

    // 이미 가입된 이메일 -> 409 Conflict
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateEmail(DuplicateEmailException e) {
        return build(HttpStatus.CONFLICT, e.getMessage());
    }

    // 로그인 실패 -> 401 Unauthorized
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidCredentials(InvalidCredentialsException e) {
        return build(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    // 존재하지 않는 콘텐츠 -> 404 Not Found
    @ExceptionHandler(ContentNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleContentNotFound(ContentNotFoundException e) {
        return build(HttpStatus.NOT_FOUND, e.getMessage());
    }

    // 이미 있는 장르 이름 -> 409 Conflict
    @ExceptionHandler(DuplicateGenreNameException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateGenreName(DuplicateGenreNameException e) {
        return build(HttpStatus.CONFLICT, e.getMessage());
    }

    // 존재하지 않는 장르 -> 404 Not Found
    @ExceptionHandler(GenreNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleGenreNotFound(GenreNotFoundException e) {
        return build(HttpStatus.NOT_FOUND, e.getMessage());
    }

    // 이미 마이리스트에 있는 콘텐츠 -> 409 Conflict
    @ExceptionHandler(DuplicateMyListException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateMyList(DuplicateMyListException e) {
        return build(HttpStatus.CONFLICT, e.getMessage());
    }

    // 마이리스트에 없는 콘텐츠 -> 404 Not Found
    @ExceptionHandler(MyListNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleMyListNotFound(MyListNotFoundException e) {
        return build(HttpStatus.NOT_FOUND, e.getMessage());
    }

    // 잘못된 이미지 파일 (비어있음, 저장 실패 등) -> 400 Bad Request
    @ExceptionHandler(InvalidImageFileException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidImageFile(InvalidImageFileException e) {
        return build(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // 시청 기록이 없음 -> 404 Not Found
    @ExceptionHandler(WatchHistoryNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleWatchHistoryNotFound(WatchHistoryNotFoundException e) {
        return build(HttpStatus.NOT_FOUND, e.getMessage());
    }

    // 재생 위치가 콘텐츠 길이를 넘음 -> 400 Bad Request
    @ExceptionHandler(InvalidWatchPositionException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidWatchPosition(InvalidWatchPositionException e) {
        return build(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // ===== 요청 자체가 잘못된 경우 (400 계열) =====

    // @Valid 검증 실패 (제목/이름이 비어있다 등) -> 400 Bad Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = (fieldError != null) ? fieldError.getDefaultMessage() : "입력값이 올바르지 않습니다.";
        return build(HttpStatus.BAD_REQUEST, message);
    }

    // 요청 본문(JSON) 형식이 깨졌거나 타입이 안 맞음 -> 400 Bad Request
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleNotReadable(HttpMessageNotReadableException e) {
        return build(HttpStatus.BAD_REQUEST, "요청 본문 형식이 올바르지 않습니다. JSON 형식과 값의 타입을 확인해주세요.");
    }

    // 필수 쿼리 파라미터가 없음 (예: 검색에서 keyword 누락) -> 400 Bad Request
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingParameter(MissingServletRequestParameterException e) {
        return build(HttpStatus.BAD_REQUEST, "필수 요청 파라미터가 없습니다: " + e.getParameterName());
    }

    // 파라미터 타입이 안 맞음 (예: /api/contents/abc 처럼 id 자리에 문자) -> 400 Bad Request
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        return build(HttpStatus.BAD_REQUEST, "요청 값의 형식이 올바르지 않습니다: " + e.getName());
    }

    // 파일 업로드 형식이 잘못됨 (multipart/form-data가 아님 등) -> 400 Bad Request
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<ApiErrorResponse> handleMultipart(MultipartException e) {
        return build(HttpStatus.BAD_REQUEST, "파일 업로드 요청 형식이 올바르지 않습니다. multipart/form-data로 file을 보내주세요.");
    }

    // 업로드 파일이 너무 큼 -> 413 Payload Too Large
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiErrorResponse> handleMaxUploadSize(MaxUploadSizeExceededException e) {
        return build(HttpStatus.PAYLOAD_TOO_LARGE, "업로드할 수 있는 파일 크기를 초과했습니다.");
    }

    // ===== DB / 그 밖의 예외 =====

    // 유니크 제약 위반 등 DB 제약조건 위반 (동시에 같은 데이터를 넣으려 할 때 등) -> 409 Conflict
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrity(DataIntegrityViolationException e) {
        return build(HttpStatus.CONFLICT, "이미 존재하거나 제약조건에 맞지 않는 데이터입니다.");
    }

    // 위에서 처리하지 못한 나머지 모든 예외
    // - 스프링이 이미 상태 코드를 정해둔 예외(404 경로 없음, 405 메서드 불가, 415 미디어 타입 등)는 그 상태 코드를 그대로 사용
    // - 그 외 예상하지 못한 예외는 500으로 응답하고, 원인은 서버 로그에만 남긴다 (내부 정보가 클라이언트에 노출되지 않도록)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception e) {
        if (e instanceof org.springframework.web.ErrorResponse errorResponse) {
            HttpStatus status = HttpStatus.resolve(errorResponse.getStatusCode().value());
            if (status != null) {
                String message = switch (status) {
                    case NOT_FOUND -> "요청한 경로를 찾을 수 없습니다.";
                    case METHOD_NOT_ALLOWED -> "지원하지 않는 요청 방식(HTTP 메서드)입니다.";
                    case UNSUPPORTED_MEDIA_TYPE -> "지원하지 않는 Content-Type 입니다.";
                    default -> "요청을 처리할 수 없습니다.";
                };
                return build(status, message);
            }
        }

        log.error("처리되지 않은 예외가 발생했습니다.", e);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
    }
}
