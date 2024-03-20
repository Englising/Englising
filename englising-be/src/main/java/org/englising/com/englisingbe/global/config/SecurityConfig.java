package org.englising.com.englisingbe.global.config;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.auth.AllowedUrls;
import org.englising.com.englisingbe.auth.jwt.JwtAuthenticationFilter;
import org.englising.com.englisingbe.auth.jwt.JwtExceptionFilter;
import org.englising.com.englisingbe.auth.handler.OAuth2LoginFailureHandler;
import org.englising.com.englisingbe.auth.handler.OAuth2LoginSuccessHandler;
import org.englising.com.englisingbe.auth.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    // HTTPSecurity Configuration -------------------------
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {
                    cors.configurationSource(corsConfigurationSource());
                })
                .csrf(AbstractHttpConfigurer::disable) //csrf disable
                .formLogin(AbstractHttpConfigurer::disable) // Form 로그인 방식 disable
                .httpBasic(AbstractHttpConfigurer::disable) //httpBasic 인증 방식 disable
                .authorizeHttpRequests(requests -> // 경로별 인가 작업
                        requests.requestMatchers(AllowedUrls.NO_CHECK_URL).permitAll()
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement -> // 세션 설정 : STATELESS
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfo) -> userInfo
                                .userService(customOAuth2UserService))
                        .failureHandler(oAuth2LoginFailureHandler) // 소셜 로그인 실패시
                        .successHandler(oAuth2LoginSuccessHandler)); //소셜 로그인 성공시

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);
        return http.build();
    }

    // Password 설정 추가
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


    // Cors Configuration -----------------------------------------------------
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "https://j10a106.p.ssafy.io"));
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.addExposedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}