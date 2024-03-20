package org.englising.com.englisingbe.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;


//super로 부모 객체인 DefaultOAuth2User 생성, 추가 파라미터 받아 주입
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private String email;
    private String nickname;
    private String profile_img;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes, String nameAttributeKey,
                            String email, String nickname, String profile_img) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.nickname = nickname;
        this.profile_img = profile_img;
    }
}