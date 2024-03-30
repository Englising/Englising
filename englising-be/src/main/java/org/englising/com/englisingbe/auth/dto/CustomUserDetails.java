package org.englising.com.englisingbe.auth.dto;

import lombok.Getter;
import org.englising.com.englisingbe.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.Collections;

// Spring Security는 유저 인증과정에서 UserDetails 참조하여 간단한 인증 진행 가능
// DB의 위에서 선언한 사용자의 정보를 토대로 인증 진행하도록 설정
// User에 바로 UserDetials 상속해도 되지만 동작 엔티티 관련 여러 단점 존재

@Getter
public class CustomUserDetails implements UserDetails {

    private User user;
//    private Map<String, Object> attribute;

    /**
     * 우리 서비스는 비밀번호를 받지 않기 때문에 password 임의로 지정해둔다.
     * */
    private String password = "password";

    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * 우리 서비스는 권한이 따로 없음 (관리자나 다른 권한 x)
     * -> 이용자에게 가장 기본적인 사용자 권한 (ROLE_USER) 부여해주기
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return new BCryptPasswordEncoder().encode(password);
    }

    /**
     * userId로 가져오기
     * */
    @Override
    public String getUsername() {
        return  getUserId();
    }

    public String getUserId() {
        // userId을 반환하는 로직을 구현
        return user.getUserId().toString();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
