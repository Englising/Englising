package org.englising.com.englisingbe.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        SecurityScheme authAccess = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.COOKIE)
                .name("Authorization");

        SecurityScheme authRefresh = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.COOKIE)
                .name("Authorization-refresh");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("basicAuth");

        return new OpenAPI()
                .info(new Info()
                        .title("Englising")
                        .description("팝송으로 영어 게임을 하며 공부할 수 있는 어플리케이션의 문서입니다.")
                        .version("1.0.0"))
                .addServersItem(new Server().url("https://j10a106.p.ssafy.io/api").description("Production server"))
                .addServersItem(new Server().url("http://localhost:8080").description("Local server"));
    }
}