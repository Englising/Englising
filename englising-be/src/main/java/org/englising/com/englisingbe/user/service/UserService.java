package org.englising.com.englisingbe.user.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.user.dto.UserSignUpDto;
import org.englising.com.englisingbe.user.entity.User;
import org.englising.com.englisingbe.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
//    private UserSignUpDto userSignUpDto;

    // 게스트 로그인
    public void signUp() throws Exception{
        //게스트 로그인이기 때문에 중복이메일 조회 x
        // todo. 토큰 확인


         User user = User.builder()
                    .email("random@email.com") // todo. uuid 사용 ex)오늘 날짜 + uuid + @email.com
                    .nickname("tempNickname")
                    .profileImg("tempProfileImgUrl")
                    .type("GUEST") // default로 GUEST로 되어있으면 빼기
                    .build();

//        if (userRepository.findByEmail(userSignUpDto.getEmail()).isPresent()) {
//            throw new Exception("이미 존재하는 이메일입니다.");
//        }
        userRepository.save(user);
    }




}
