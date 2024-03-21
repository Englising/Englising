package org.englising.com.englisingbe.auth.jwt;

import org.englising.com.englisingbe.auth.dto.CustomUserDetails;
import org.englising.com.englisingbe.auth.service.CustomUserDetailService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailService customUserDetailService;

    public CustomAuthenticationProvider(CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        CustomUserDetails customUserDetails = (CustomUserDetails) customUserDetailService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(customUserDetails, password, customUserDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
