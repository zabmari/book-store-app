package com.mate.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI openApi() {
        SecurityScheme bearerScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME, bearerScheme))
            .addSecurityItem(new SecurityRequirement()
                .addList(SECURITY_SCHEME_NAME));
    }
}
