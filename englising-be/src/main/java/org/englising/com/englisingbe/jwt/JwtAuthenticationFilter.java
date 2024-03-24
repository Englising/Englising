package org.englising.com.englisingbe.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.user.entity.User;
import org.englising.com.englisingbe.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// JwtTokenProvider가 검증을 끝낸 Jwt로부터 유저 정보를 조회해와서
// UserPasswordAuthenticationFilter로 전달
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

//    private static final String NO_CHECK_URL = "/login";
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private JwtTokenType jwtTokenType;

    // 토큰 유효한지 확인 후 SecurityContext에 계정정보 저장하는 메소드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        if (request.getRequestURI().equals(NO_CHECK_URL)) {
//            filterChain.doFilter(request, response); // "/login" 요청이 들어오면, 다음 필터 호출
//            return; // return으로 이후 현재 필터 진행 막기 (안해주면 아래로 내려가서 계속 필터 진행시킴)
//        } //todo. 게스트, 카카오 로그인 요청 시 필터 진행 x 하도록 수정


        // 해당 토큰이 refreshToken인지 확인
        // (refreshToken이 null이면 accessToken 유효성 검사, null이 아니면 refreshToken 유효성 검사)
        // filter해서 null이 아니라면 refreshToken이고 유효한 것
        // -> 클라이언트가 AccessToken 만료되어 RefreshToken을 보낸 것
//        String refreshToken = jwtTokenProvider.getRefreshTokenFromHeader(request)
//                .filter(jwtTokenProvider::isTokenValid)
//                .orElse(null);


        // 헤더에서 토큰 추출
        String token = jwtTokenProvider.extractTokenFromHeader(request)
                .filter(jwtTokenProvider::isTokenValid)
                .orElse(null);

        // 토큰 타입 받기 (accessToken인지 refreshToken인지)
        if(token != null) {
            jwtTokenType = jwtTokenProvider.getTokenTypeFromHeader(token);
        }

        // JwtTokenType이 accessToken이면
        if(jwtTokenType == JwtTokenType.ACCESS_TOKEN) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }
        else if (jwtTokenType == JwtTokenType.REFRESH_TOKEN) { // JwtTokenType이 refreshToken이면




        } else {
            // 두가지 타입이 모두 아닌경우 예외처리

        }











//        if(refreshToken != null) {
//            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
//            return;
//        }
//
//        if(refreshToken == null) {
//            checkAccessTokenAndAuthentication(request,response, filterChain);
//        }
    }

    // RefreshToken으로 유저 정보 찾기, Access/Refresh Token 재발급
//    private JwtResponseDto checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
//        userRepository.findByRefreshToken(refreshToken)
//                .ifPresent(user -> {
//                    String reIssuedAccessToken = jwtTokenProvider.createAccessToken(user.getUsername());
//                    String reIssuedRefreshToken = reIssueRefreshToken(user);
//
//                    //todo. 헤더에 담아서 토큰 보내는 방식 아니고 ResponseData로 보내는 형식으로 수정
////                    JwtResponseDto jwtResponseDto = new JwtResponseDto(reIssuedAccessToken, reIssuedRefreshToken);
//                    return new JwtResponseDto(reIssuedAccessToken, reIssuedRefreshToken);
//                });
//        return null;
//    };
//
//    // 리프레시 토큰 재발급, db에 업데이트
//    //todo. Redis로 수정
//    private String reIssueRefreshToken(User user) {
//        String reIssuedRefreshToken = jwtTokenProvider.createRefreshToken(user.getUsername());
//        user.updateRefreshToken(reIssuedRefreshToken);
//        userRepository.saveAndFlush(user);
//        return reIssuedRefreshToken;
//    }

    // 액세스 토큰 유효성 검사, 인증 처리
    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                   FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtTokenProvider.extractTokenFromHeader(request).toString();

        if(jwtTokenProvider.isTokenValid(accessToken)) {// 토큰 유효하면 토큰으로부터 유저 정보 가져옴
            String userId = jwtTokenProvider.getUserId(accessToken).toString();
            User user = userRepository.findByUserId(Integer.valueOf(userId))
                    .orElseThrow(IllegalAccessError::new);

            // SecurityContext에 인증 객체 등록
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

}