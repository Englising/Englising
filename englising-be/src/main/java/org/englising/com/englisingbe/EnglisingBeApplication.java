package org.englising.com.englisingbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class EnglisingBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnglisingBeApplication.class, args);
    }

}
