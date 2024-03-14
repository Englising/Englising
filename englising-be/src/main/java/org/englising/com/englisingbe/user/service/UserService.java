package org.englising.com.englisingbe.user.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.user.dto.User;
import org.englising.com.englisingbe.user.dto.UserSignUpDto;
import org.englising.com.englisingbe.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 게스트 로그인
    public void signUp(UserSignUpDto userSignUpDto) {
        //게스트 로그인이기 때문에 중복이메일 조회 x
        User user = userSignUpDto.toEntity();

        userRepository.save(user);
    }




}
