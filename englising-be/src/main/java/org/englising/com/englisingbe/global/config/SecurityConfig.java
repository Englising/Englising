package org.englising.com.englisingbe.global.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {




}

/**
*  1. 클라이언트에서 카카오로그인으로 생성된 AccessToken 서버로 전송
 * 2. 서버는 받은 AccessToken 가지고 카카오 API를 사용하여 유저정보 가져옴
 * 3. 유저정보 DB에 저장하고, AccessToken과 Refresh Token 생성
 * 4. Refresh Token은 유저 테이블에 저장 ( 추후 Redis에 저장하는 걸로 수정)
 * 5. 서버에서 생성한 Acces Token을 클라이언트에 Json으로 전달 ? (Header에 설정)
 * 6. 클라이언트는 Access Token이 만료될 때마다 Refresh Token으로 Access Token 갱신
* */