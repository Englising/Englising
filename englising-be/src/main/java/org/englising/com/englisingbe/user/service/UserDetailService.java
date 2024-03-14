package org.englising.com.englisingbe.user.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

//    private final UserRepository userRepository;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepository.findByUserId(Integer.valueOf(username))
//                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다."));
//    }
}
