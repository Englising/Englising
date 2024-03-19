package org.englising.com.englisingbe.user.dto;

import java.util.Map;

public class KakaoOAuth2Response extends OAuth2Response{

    public KakaoOAuth2Response(Map<String, Object> attributes) {
        super(attributes);
    }


    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

        if(kakaoAccount == null) {
            return null;
        }
        //여기서 null인건가?


        return String.valueOf(kakaoAccount.get("email"));
    }
}
