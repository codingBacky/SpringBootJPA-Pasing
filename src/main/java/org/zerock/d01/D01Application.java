package org.zerock.d01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class D01Application {

    public static void main(String[] args) {
        SpringApplication.run(D01Application.class, args);
    }
}
