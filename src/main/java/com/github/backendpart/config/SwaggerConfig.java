package com.github.backendpart.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        String jwtSchemeName = "jwtAuth";

        SecurityScheme bearerAuth = new SecurityScheme()
                .name(jwtSchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER);

        SecurityRequirement addSecurityItem = new SecurityRequirement();
        addSecurityItem.addList("Authorization");


        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Authorization", bearerAuth))
                .addSecurityItem(addSecurityItem)
                .info(info);
    }


}
