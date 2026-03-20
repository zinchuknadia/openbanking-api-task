package com.example.OpenBankingApiTask.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Payment API",
                version = "1.0",
                description = "API for processing payments"
        )
)
@Configuration
public class OpenApiConfig {
}
