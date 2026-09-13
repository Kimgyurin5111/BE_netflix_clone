package com.dslion.netflix_clone.auth.service;

import com.dslion.netflix_clone.auth.dto.request.LoginRequest;
import com.dslion.netflix_clone.auth.dto.request.SignupRequest;
import com.dslion.netflix_clone.auth.dto.response.SignupResponse;
import com.dslion.netflix_clone.auth.dto.response.TokenResponse;
import com.dslion.netflix_clone.auth.jwt.JwtTokenProvider;
import com.dslion.netflix_clone.global.exception.DuplicateEmailException;
import com.dslion.netflix_clone.global.exception.InvalidCredentialsException;
import com.dslion.netflix_clone.user.entity.Role;
import com.dslion.netflix_clone.user.entity.User;
import com.dslion.netflix_clone.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// 회원가입 / 로그인의 실제 처리 로직
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public SignupResponse signup(SignupRequest request) {
        // 1. 이메일 중복 확인
        boolean alreadyExists = userRepository.existsByEmail(request.getEmail());
        if (alreadyExists) {
            throw new DuplicateEmailException();
        }

        // 2. 비밀번호는 암호화해서 저장 (평문 저장 금지)
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 3. 회원 저장 (기본 권한은 USER)
        User user = new User(request.getEmail(), encodedPassword, request.getNickname(), Role.USER);
        User savedUser = userRepository.save(user);

        return new SignupResponse(savedUser.getId(), savedUser.getEmail(), savedUser.getNickname());
    }

    public TokenResponse login(LoginRequest request) {
        // 1. 이메일로 회원 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        // 2. 비밀번호 확인 (입력한 비밀번호를 암호화해서 저장된 값과 비교)
        boolean matches = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!matches) {
            throw new InvalidCredentialsException();
        }

        // 3. 토큰 발급
        String token = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole().name());
        return new TokenResponse(token);
    }
}
