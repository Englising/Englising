package org.englising.com.englisingbe.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.englising.com.englisingbe.user.entity.User;
import org.englising.com.englisingbe.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Filter를 적용함으로써 servlet에 도달하기 전에 검증 완료 가능
// JwtProvider가 검증을 끝낸 Jwt로부터 유저 정보를 조회해와서
// UserPasswordAuthenticationFilter로 전달
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/auth";
    //todo. 토큰 재발급 url은 빼야 함

    private final JwtProvider jwtProvider;

    // 토큰 유효한지 확인 후 SecurityContext에 계정정보 저장하는 메소드
    /**
     * 1. 헤더에서 JWT 받아온다
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

        // 1
        String token = jwtProvider.extractTokenFromHeader(request).toString();

        try {
            //  2 3 4
            if (StringUtils.hasText(token) && jwtProvider.isTokenValid(token)) {
                //유효한 토큰이면 해당 토큰으로 Authentication 가져와서 SecurityContext에 저장
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            request.setAttribute("exeption", e.getMessage());
        }
        filterChain.doFilter(request, response);


    }
}

//          todo. UserService로 이동?
//        // RefreshToken으로 유저 정보 찾기, Access/Refresh Token 재발급
//        private JwtResponseDto checkRefreshTokenAndReIssueAccessToken (HttpServletResponse response, String refreshToken)
//        {
//            // RefreshToken의 Claim에서 userId 추출
//            String userId = jwtProvider.getUserId(refreshToken).toString();
//
//            // DB에 해당 userId가 있다면 토큰 재발급
//            userRepository.findByUserId(Integer.valueOf(userId))
//                    .ifPresent(user -> {
//                        return jwtProvider.getTokens(userId);
//                    });
//
//            // DB에 해당 userId가 없다면
//            log.info("유효하지 않은 RefreshToken입니다.");
//            return null;
//        }
//        ;
//
//        // 리프레시 토큰 재발급, db에 업데이트
//        //todo. Redis로 수정
//        private String reIssueRefreshToken (User user){
//            String reIssuedRefreshToken = jwtProvider.createRefreshToken(user.getUsername());
//            user.updateRefreshToken(reIssuedRefreshToken);
//            userRepository.saveAndFlush(user);
//            return reIssuedRefreshToken;
//        }
