package com.nullers.restbookstore.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SwaggerConfig
 *
 * @Author Binwei Wang
 * @Author Jaimesalcedo1
 * @Author Daniel
 * @Author Madirex
 * @Author Alexdor00
 */
@Configuration
class SwaggerConfig {

    public static final String REPO_URL = "https://github.com/Madirex/rest-bookstore";

    /**
     * Método que crea el esquema de seguridad
     *
     * @return SecurityScheme
     */
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    /**
     * Método que devuelve la información de la API
     *
     * @return OpenAPI
     */
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("API REST Tienda libros")
                                .version("1.0.0")
                                .description("API REST Tienda libros")
                                .license(
                                        new License()
                                                .name("CC BY-NC-SA 4.0")
                                                .url(REPO_URL)
                                )
                                .contact(
                                        new Contact()
                                                .name("NULLERS")
                                )
                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("Documentación del Proyecto")
                                .url(REPO_URL)
                )
                .addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()));
    }

    /**
     * Método que devuelve la información de la API
     *
     * @return OpenAPI
     */
    @Bean
    public GroupedOpenApi httpApi() {
        return GroupedOpenApi.builder()
                .group("https")
                .pathsToMatch("/api/books/**", "/api/auth/**", "/api/categories/**")
                .displayName("Tienda libros")
                .build();
    }
}