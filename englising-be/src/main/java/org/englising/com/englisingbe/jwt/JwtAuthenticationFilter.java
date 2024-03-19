package org.englising.com.englisingbe.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.englising.com.englisingbe.user.entity.User;
import org.englising.com.englisingbe.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Filter를 적용함으로써 servlet에 도달하기 전에 검증 완료 가능
// JwtProvider가 검증을 끝낸 Jwt로부터 유저 정보를 조회해와서
// UserPasswordAuthenticationFilter로 전달
@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/auth/guest";
    //todo. 토큰 재발급 url은 빼야 함

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final CookieUtil cookieUtil;

    // 토큰 유효한지 확인 후 SecurityContext에 계정정보 저장하는 메소드
    /**
     * 1. 쿠키에서 JWT 받아온다
     * 2. 유효한 토큰인지 확인한다
     * 3. 토큰이 유효하면 토큰으로부터 유저 정보 받아온다
     * 4. SecurityContext에 Authentication 객체 저장한다.
     * */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response); // "/auth" 요청이 들어오면, 다음 필터 호출
            return; // return으로 이후 현재 필터 진행 막기 (안해주면 아래로 내려가서 계속 필터 진행시킴)
        } // -> 로그인 요청 시 필터 진행 x

        // 요청 헤더에서 accessToken 추출 -> null이 아니라면 유효성 체크
//        String accessToken = jwtProvider.extractAccessTokenFromHeader(request).orElse(null);

//        String accessToken = null;
//        Cookie[] cookies = request.getCookies();
//        for(Cookie cookie : cookies) {
//            System.out.println(cookie.getName());
//            if(cookie.getName().equals("Authorization")) {
//                accessToken = cookie.getValue();
//            }
//        }

        // 쿠키에서 accessToken 추출.
        String accessToken = cookieUtil.getAccessTokenFromCookie(request);
        
        try {
            //  2 3 4
            if (accessToken != null && jwtProvider.isTokenValid(accessToken)) {
                //유효한 토큰이면 해당 토큰으로 Authentication 가져와서 SecurityContext에 저장
                Authentication authentication = jwtProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                System.out.println("accessToken이 유효하지 않습니다. ");
                System.out.println("redis에서 확인 후 토큰 재발급 진행합니다. ");
                /**
                 * 1. accessToken이 유효하지 않을 경우 accessToken에서 userId 추출
                 * 2. redis에서 해당 userId를 key로 가지고 있는 refershToken 있는지 확인
                 * 3. 있다면 accessToken, refreshToken 재발급 해서 쿠키에 asccessToken 저장, redis에 refreshToken 저장
                 * 이러면 재발급 api 필요없음
                 * */
                
                // 1. accessToken이 유효하지 않을 경우 accessToken에서 userId 추출
                Long userId = jwtProvider.getUserId(accessToken).orElse(null);
                
                // 2. redis에서 userId를 key로 가지고 있는 refreshToken 있는지 확인
                
                
                // 3. 있다면 (존재하는 회원) userId로 token 재발급 todo. 있다면으로 코드 수정
                JwtResponseDto jwtResponseDto =
                                jwtProvider.createTokens(jwtProvider.getAuthentication(accessToken), userId);
                
                // 4. accessToken은 cookie에 저장
                Cookie tokenCookie = cookieUtil.createCookie("Authorization", jwtResponseDto.getAccessToken());
                response.addCookie(tokenCookie); // 응답에 쿠키 추가
                
                // 5. todo. refreshToken은 redis에 저장 (업데이트)
                return;

            }
        } catch (Exception e) {
            request.setAttribute("exeption", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}

// ----------밑 코드 참고 ---------------
//                if (refreshToken != null) { // accessToken 만료되어 refreshToken 보낸것
//                    // refreshToken에서 userId 추출하고 해당 userId 존재하면 accessToken 재발급
//                    Long userId = jwtProvider.getUserId(refreshToken).orElse(null);
//                    if (userRepository.findByUserId(userId).isPresent()) {
//                        JwtResponseDto jwtResponseDto =
//                                jwtProvider.createTokens(jwtProvider.getAuthentication(refreshToken), userId);
//                        jwtProvider.setAccessAndRefreshToken(response, jwtResponseDto.getAccessToken(),
//                                jwtResponseDto.getRefreshToken());
//                        return;// RefreshToken을 보낸 경우에는 AccessToken을 재발급 하고 인증 처리는 하지 않게 하기위해
// 바로 return으로 필터 진행 막기
//                    }
//                }

