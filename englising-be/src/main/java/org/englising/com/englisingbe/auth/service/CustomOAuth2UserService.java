package org.englising.com.englisingbe.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.englising.com.englisingbe.auth.CustomOAuth2User;
import org.englising.com.englisingbe.auth.OAuthAttributes;
import org.englising.com.englisingbe.user.entity.User;
import org.englising.com.englisingbe.user.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;


// OAuth2 로그인 로직 담당
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        /**
         * DefaultOAuth2UserService 객체를 생성하여, loadUser(userRequest)를 통해 DefaultOAuth2User 객체를 생성 후 반환
         * DefaultOAuth2UserService의 loadUser()는 소셜 로그인 API의 사용자 정보 제공 URI로 요청을 보내서
         * 사용자 정보를 얻은 후, 이를 통해 DefaultOAuth2User 객체를 생성 후 반환한다.
         * 결과적으로, OAuth2User는 OAuth 서비스에서 가져온 유저 정보를 담고 있는 유저 (카카오에서 토큰을 통해 받아온 유저 정보)
         */

        //기본 OAuth2UserService 객체 생성
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        //OAuth2UserService 사용하여 OAuth2User 정보 가져옴
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        /**
         * userRequest에서 registrationId 추출 후 registrationId으로 SocialType 저장
         * http://localhost:8080/oauth2/authorization/kakao에서 kakao가 registrationId
         * userNameAttributeName은 이후에 nameAttributeKey로 설정된다.
         */

        // registrationId => socailType (kakao, naver, google ..등)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // ->사용자 이름 속성 (키 값)

        // 소셜 로그인에서 api가 제공하는 user json값(유저 정보들)
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // registationId에 따라 유저 정보를 통해 OAuthAttributes 객체 생성
        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());


        // email로 가입된 회원인지 조회
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String email = String.valueOf(kakaoAccount.get("email"));
        User findUser = userRepository.findByEmail(email).orElse(null);

        //회원이 아니면 DB에 저장 (회원 등록)
        if(findUser == null) {          // User 객체는 OAuthAttributes의 toEntity() 메소드 이용해 생성
            User createdUser = oAuthAttributes.toEntity(registrationId, oAuthAttributes.getOAuth2Response());
            userRepository.save(createdUser);
        }

        // DefualitOAuth2User 구현한 CustomOAuth2User 객체 생성해서 반환
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), // 우리 서비는 권한 따로 없기 때문에 모두 USER
                        attributes,
                        oAuthAttributes.getNameAttributeKey(),
                        findUser.getEmail(),
                        findUser.getNickname(),
                        findUser.getProfileImg()
        );
    }
}
