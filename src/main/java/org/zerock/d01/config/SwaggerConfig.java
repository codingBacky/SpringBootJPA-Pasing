package org.zerock.d01.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi restApi(){
        return GroupedOpenApi.builder()
                .group("API")
                .packagesToScan("org.zerock.d01.controller")
                .pathsToExclude("/board/*")
                .build();
    }

    @Bean
    public GroupedOpenApi commonApi(){
        return GroupedOpenApi.builder()
                .pathsToMatch("/board/*")//모든요청에 이걸주겠다
                .group("COMMON API")
                .build();
    }
}
