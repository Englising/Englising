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
import org.englising.com.englisingbe.user.service.UserService;
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
    private final UserService userService;

    public JwtResponseDto guest() throws Exception{
         User user = User.builder()
                    .email(makeRandomEmail()) // todo. uuid 사용 ex)오늘 날짜 + uuid + @email.com
                    .nickname(userService.makeRandomNickname()) //todo. 수정
                    .profileImg(userService.makeRandomProfileImgUrl()) //todo. 수정
                    .color(userService.makeRandomColor())
                    .type("GUEST") // default로 GUEST 되어있으면 빼기
                    .build();
        userRepository.save(user); // 회원 등록

        CustomUserDetails customUserDetails =
                (CustomUserDetails) customUserDetailService.loadUserByUsername(user.getUserId().toString());

        // 1. Authentication 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(customUserDetails, customUserDetails.getPassword(),
                        customUserDetails.getAuthorities());

        // 2. Authentication 검증
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보 기반으로 JWT 생성
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


}
