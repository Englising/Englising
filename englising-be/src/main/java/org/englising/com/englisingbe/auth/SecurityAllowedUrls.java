package org.englising.com.englisingbe.auth;

public class SecurityAllowedUrls {
    public static final String[] NO_CHECK_URL = {
            /* swagger v2 */
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            /* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/auth/**",
//            "/**",
            "/ws-stomp/**",
            "/auth/**",
//            "/oauth/**",
//            "/oauth2/**"
    };
}
