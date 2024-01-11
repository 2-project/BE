package com.github.backendpart.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
      return new OpenAPI()
              .info(new Info()
                      .title("프로젝트 게시판 관련 API")
                      .description("Swagger ")
                      .version("1.0.0"));
    }
}
