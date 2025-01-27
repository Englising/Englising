package org.englising.com.englisingbe.auth.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.auth.dto.CustomUserDetails;
import org.englising.com.englisingbe.user.entity.User;
import org.englising.com.englisingbe.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// UserDetails 정보를 토대로 유저 정보 불러올 때 사용
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(Long.parseLong(username))
                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다. - CustomUserDetailService"));

        return new CustomUserDetails(user);
    }
}
