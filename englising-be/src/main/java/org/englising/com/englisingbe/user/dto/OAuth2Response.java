package org.englising.com.englisingbe.user.dto;

import java.util.Map;

public abstract class OAuth2Response {

    protected Map<String, Object> attributes;

    public OAuth2Response(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId(); // 소셜 식별 값

    public abstract String getEmail();
}
