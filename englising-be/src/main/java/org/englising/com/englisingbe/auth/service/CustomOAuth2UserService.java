package org.englising.com.englisingbe.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.englising.com.englisingbe.auth.CustomOAuth2User;
import org.englising.com.englisingbe.auth.OAuthAttributes;
import org.englising.com.englisingbe.user.entity.User;
import org.englising.com.englisingbe.user.repository.UserRepository;
import org.englising.com.englisingbe.user.service.UserService;
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
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        //기본 OAuth2UserService 객체 생성
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        //OAuth2UserService 사용하여 OAuth2User 정보 가져옴
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

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
        if (findUser == null) {
            // User 객체는 OAuthAttributes의 toEntity() 메소드 이용해 생성
            User createdUser = oAuthAttributes.toEntity(oAuthAttributes.getOAuth2Response(), userService);
            userRepository.save(createdUser);
            return createCustomOAuth2User(createdUser, attributes, oAuthAttributes);
        } else {
            return createCustomOAuth2User(findUser, attributes, oAuthAttributes);
        }
    }

    // DefaulitOAuth2User 구현한 CustomOAuth2User 객체 생성해서 반환
    private CustomOAuth2User createCustomOAuth2User(User user, Map<String, Object> attributes, OAuthAttributes oAuthAttributes) {
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                oAuthAttributes.getNameAttributeKey(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImg(),
                user.getColor()
        );
    }
}