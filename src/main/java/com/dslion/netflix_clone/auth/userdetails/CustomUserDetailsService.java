package com.dslion.netflix_clone.auth.userdetails;

import com.dslion.netflix_clone.user.entity.User;
import com.dslion.netflix_clone.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 이메일로 회원을 찾아서 Spring Security가 이해할 수 있는 형태(UserDetails)로 바꿔주는 역할
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("가입되지 않은 이메일입니다: " + email));

        return new CustomUserDetails(user);
    }
}
