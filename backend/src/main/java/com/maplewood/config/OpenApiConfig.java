package com.maplewood.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI maplewoodApi() {
        return new OpenAPI()
            .info(new Info()
                .title("Maplewood Course Planning API")
                .description("API for managing course planning and student scheduling")
                .version("1.0"));
    }
}