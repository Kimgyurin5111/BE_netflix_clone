package com.dslion.netflix_clone.auth.controller;

import com.dslion.netflix_clone.auth.dto.request.LoginRequest;
import com.dslion.netflix_clone.auth.dto.request.SignupRequest;
import com.dslion.netflix_clone.auth.dto.response.SignupResponse;
import com.dslion.netflix_clone.auth.dto.response.TokenResponse;
import com.dslion.netflix_clone.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 회원가입 / 로그인 API - 로그인 없이 누구나 호출 가능
// @SecurityRequirements(비워둠)는 Swagger에서 이 API들에 자물쇠(인증 필요) 표시가 안 뜨게 하는 설정
@Tag(name = "인증", description = "회원가입, 로그인")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "회원가입", description = "이메일과 비밀번호로 회원가입합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "입력값이 올바르지 않음"),
            @ApiResponse(responseCode = "409", description = "이미 가입된 이메일")
    })
    @SecurityRequirements
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
        SignupResponse response = authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "로그인", description = "로그인에 성공하면 JWT 토큰을 발급합니다. 이 토큰을 Authorize 버튼에 넣어 사용하세요.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공 (토큰 발급)"),
            @ApiResponse(responseCode = "400", description = "입력값이 올바르지 않음"),
            @ApiResponse(responseCode = "401", description = "이메일 또는 비밀번호가 틀림")
    })
    @SecurityRequirements
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
