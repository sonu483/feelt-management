package com.feelt.fleet.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI fleetOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fleet Management and Route Optimization API")
                        .version("1.0.0")
                        .description("REST APIs for fleet registry, dispatch workflow, delivery tracking, and route optimization."));
    }
}
