package org.englising.com.englisingbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:env.properties")
public class EnglisingBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnglisingBeApplication.class, args);
    }

}
