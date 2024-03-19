package org.englising.com.englisingbe.user.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.user.dto.CustomUserDetails;
import org.englising.com.englisingbe.user.entity.User;
import org.englising.com.englisingbe.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

// UserDetails 정보를 토대로 유저 정보 불러올 때 사용
// Jpa 이용하여 db 유저 정보 조회할 것이므로 이에 맞추어 구현해줌
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다."));

        return new CustomUserDetails(user); // 유저정보 가져옴
    }



}
