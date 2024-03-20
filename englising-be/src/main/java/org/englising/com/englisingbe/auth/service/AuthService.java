package org.englising.com.englisingbe.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.englising.com.englisingbe.auth.jwt.CookieUtil;
import org.englising.com.englisingbe.auth.jwt.JwtProvider;
import org.englising.com.englisingbe.auth.jwt.JwtResponseDto;
import org.englising.com.englisingbe.auth.dto.CustomUserDetails;
import org.englising.com.englisingbe.user.entity.User;
import org.englising.com.englisingbe.user.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final CustomUserDetailService customUserDetailService;
    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;

    public JwtResponseDto guest() throws Exception{
         User user = User.builder()
                    .email(makeRandomEmail()) // todo. uuid 사용 ex)오늘 날짜 + uuid + @email.com
                    .nickname(makeRandomNickname()) //todo. 수정
                    .profileImg(makeRandomProfileImgUrl()) //todo. 수정
                    .type("GUEST") // default로 GUEST 되어있으면 빼기
                    .build();

        userRepository.save(user); // 회원 등록

        CustomUserDetails customUserDetails =
                (CustomUserDetails) customUserDetailService.loadUserByUsername(user.getEmail());

        // 1. Authentication 생성
        System.out.println("Authentication 생성 시작");
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(customUserDetails, customUserDetails.getPassword(),
                        customUserDetails.getAuthorities());

        // 2. Authentication 검증
        System.out.println("Authentication 검증 시작");
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보 기반으로 JWT 생성
        System.out.println("jwt 생성 시작");
        JwtResponseDto jwtResponseDto = jwtProvider.createTokens(authentication, user.getUserId());

        return jwtResponseDto;
    }

    public Long getUserID(HttpServletRequest request) {
        String accessToken = cookieUtil.getAccessTokenFromCookie(request);

        if(accessToken != null && jwtProvider.isTokenValid(accessToken)) {
            Long userId = jwtProvider.getUserId(accessToken).orElse(null);
            return userId;
        }
        return null;
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
        //일단 임시로
        String uuid = UUID.randomUUID().toString();
        String nickname = uuid + "닉네임";

//        List<String> adjective = Arrays.asList("귀여운", "행복한", "즐거운", "배고픈", "노란", "동그란", "푸른", "수줍은"
//        , "그리운", "배부른", "부자", "깨발랄한", "웃고있는", "해맑은", "슬픈", "반가운", "무서운", "", "귀여운");
        
        return nickname;
    }

    // todo. 랜덤 프로필이미지 (현지가 만든 알파벳 캐릭터)
    public String makeRandomProfileImgUrl() {
        String uuid = UUID.randomUUID().toString();
        String img = uuid + "프로필이미지";
        return img;
    }

}