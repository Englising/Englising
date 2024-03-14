package org.englising.com.englisingbe.user.service;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.user.dto.UserSignUpDto;
import org.englising.com.englisingbe.user.entity.User;
import org.englising.com.englisingbe.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 게스트 로그인
    public String signUp() throws Exception{

         User user = User.builder()
                    .email(makeRandomEmail()) // todo. uuid 사용 ex)오늘 날짜 + uuid + @email.com
                    .nickname("tempNickname3") //todo. 수정
                    .profileImg("tempProfileImgUrl3") //todo. 수정
                    .type("GUEST") // default로 GUEST 되어있으면 빼기
                    .build();

        userRepository.save(user);
        // todo. Redis에 refreshToken 저장
        return user.getUserId().toString();
    }

    public String makeRandomEmail() {
        String uuid = UUID.randomUUID().toString();
        String todayDate = LocalDateTime.now().toString()
                .replace(":", "").replace(".", "");
        return todayDate + "" + uuid + "@englising.com";
    }

    //todo. 랜덤 닉네임
    /**
     * "익명의" + 동물 랜덤 이름 + (count+1)
     * //동물 이름 데이터셋 db에 저장
     * */
    public String makeRandomNickname() {
        List<String> adjective = Arrays.asList("귀여운", "행복한", "즐거운", "배고픈", "노란", "동그란", "푸른", "수줍은"
        , "그리운", "배부른", "부자", "깨발랄한", "웃고있는", "해맑은", "슬픈", "반가운", "무서운", "", "귀여운");
        
        return null;
    }


    // todo. 랜덤 프로필이미지
    /**
     * 저작권 없는 동물 랜덤 이미지 이것도 저장?
     *
     *
     * */
    public String makeRandomProfileImgUrl() {
        return null;
    }




}
