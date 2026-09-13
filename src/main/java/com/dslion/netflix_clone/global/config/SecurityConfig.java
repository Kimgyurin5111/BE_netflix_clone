package com.dslion.netflix_clone.global.config;

import com.dslion.netflix_clone.auth.jwt.JwtAuthenticationFilter;
import com.dslion.netflix_clone.auth.jwt.JwtTokenProvider;
import com.dslion.netflix_clone.auth.userdetails.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Spring Security 설정 - 어떤 요청을 막고 열지, 비밀번호는 어떻게 암호화할지 정하는 곳
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // 비밀번호 암호화에 사용할 인코더 (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // JWT를 쓰므로 세션/쿠키 기반 보호인 csrf는 끈다
                .csrf(csrf -> csrf.disable())
                // 세션을 만들지 않고 매 요청마다 토큰으로 인증한다
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 회원가입/로그인은 누구나 접근 가능
                        .requestMatchers("/api/auth/**").permitAll()
                        // Swagger UI 문서 페이지도 로그인 없이 접근 가능
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // 콘텐츠 등록/수정/삭제는 관리자만 가능, 조회(GET)는 로그인한 누구나 가능
                        // "/api/contents"(정확히 이 경로)와 "/api/contents/**"(뒤에 id가 붙는 경로)를 둘 다 걸어야
                        // 등록(POST /api/contents)까지 빠짐없이 막힌다
                        .requestMatchers(HttpMethod.POST, "/api/contents", "/api/contents/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/contents", "/api/contents/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/contents", "/api/contents/**").hasRole("ADMIN")
                        // 그 외 관리자 전용 API가 생기면 여기로
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // 그 외 요청은 로그인(토큰)이 있어야 접근 가능
                        .anyRequest().authenticated()
                )
                // 매 요청마다 JWT를 확인하는 필터를 Security 기본 필터보다 먼저 실행
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
