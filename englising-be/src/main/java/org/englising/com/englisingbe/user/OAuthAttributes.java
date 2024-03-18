package org.englising.com.englisingbe.user;

import lombok.Builder;
import lombok.Getter;
import org.englising.com.englisingbe.user.dto.KakaoOAuth2Response;
import org.englising.com.englisingbe.user.dto.OAuth2Response;
import org.englising.com.englisingbe.user.entity.User;

import java.util.Map;


// 각 소셜에서 받아오는 데이터 다르므로
// 소셜별로 받는 데이터 분기 처리하는 dto 클래스
@Getter
public class OAuthAttributes {
    private final String nameAttributeKey; //OAuth2 로그인 진행 시 키가 되는 필드 값
    private final OAuth2Response oAuth2Response; // 소셜 타입별 로그인 유저 응답 정보

    @Builder
    private OAuthAttributes (String nameAttributeKey, OAuth2Response oAuth2Response) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2Response = oAuth2Response;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName,
                                     Map<String, Object> attributes) {

        switch (registrationId) {
            case "kakao" :
                return ofKaKao(userNameAttributeName, attributes);
            default:
                throw new RuntimeException();
        }
    }

    private static OAuthAttributes ofKaKao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2Response(new KakaoOAuth2Response(attributes))
                .build();
    }

    /**
     * of메소드로 OAuthAttributes 객체가 생성되어, 유저 정보들이 담긴 OAuth2Response가 소셜 타입별로 주입된 상태
     * OAuth2Response에서 email 가져와서 build
     * role은 USER로 설정 (게스트 로그인 회원은 GUEST)
     * * */

    public User toEntity(String registrationId, OAuth2Response oAuth2Response) {
        return User.builder()
                .email(oAuth2Response.getEmail())
                .nickname("nickname111")
                .profileImg("profileimg222")
                .type("USER")
                .build();
    }
    // todo. 랜덤 닉네임, 랜덤 프로필 이미지 만드는 메소드 유틸로 빼서
    // 게스트 로그인, 카카오 로그인 때 사용하기

}
