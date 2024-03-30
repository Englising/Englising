package org.englising.com.englisingbe.global.config;

import lombok.RequiredArgsConstructor;
import org.englising.com.englisingbe.auth.interceptor.HttpHandShakeInterceptor;
import org.englising.com.englisingbe.auth.interceptor.WebSocketInterceptor;
import org.englising.com.englisingbe.auth.jwt.CookieUtil;
import org.englising.com.englisingbe.auth.jwt.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub");
        registry.enableSimpleBroker("/sub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOrigins("*")
                .addInterceptors(httpHandshakeInterceptor());
    }

    @Bean
    public HttpHandShakeInterceptor httpHandshakeInterceptor() {
        return new HttpHandShakeInterceptor(jwtProvider, cookieUtil);
    }
}
