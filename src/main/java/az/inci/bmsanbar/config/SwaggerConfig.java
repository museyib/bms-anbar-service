package az.inci.bmsanbar.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi publicApi()
    {
        return GroupedOpenApi.builder()
                .group("Anbar API V4")
                .pathsToMatch("/v4/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenApi()
    {
        return new OpenAPI()
                .info(new Info().title("Anbar").version("v4"))
                .addSecurityItem(new SecurityRequirement().addList("Bearer"))
                .components(
                        new Components()
                                .addSecuritySchemes("Bearer",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("Bearer")
                                                .bearerFormat("JWT")));
    }
}
