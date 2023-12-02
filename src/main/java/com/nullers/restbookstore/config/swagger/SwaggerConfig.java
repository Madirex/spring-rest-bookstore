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
 * @Author: Binwei Wang
 */
@Configuration
class SwaggerConfig {

    // Añadimos la configuración de JWT
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    /**
     * Método que devuelve la información de la API
     * @return OpenAPI
     */
    @Bean
    OpenAPI apiInfo() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("API REST Tienda Spring Boot DAW 2023/2024")
                                .version("1.0.0")
                                .description("API REST Tienda libros Nullers")
                                .license(
                                        new License()
                                                .name("CC BY-NC-SA 4.0")
                                                .url("https://github.com/Madirex/rest-bookstore")
                                )
                                .contact(
                                        new Contact()
                                                .name("Nullers")
                                                .email("Nullers@gmail.com")
                                )

                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("Documentación del Proyecto")
                                .url("https://github.com/Madirex/rest-bookstore")
                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("GitHub del Proyecto")
                                .url("https://github.com/Madirex/rest-bookstore")
                )
                // Añadimos la seguridad JWT
                .addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()));
    }

    /**
     * Método que devuelve la información de la API
     * @return OpenAPI
     */
    @Bean
    GroupedOpenApi httpApi() {
        return GroupedOpenApi.builder()
                .group("https")
                // Algunas rutas son JWT
                // .pathsToMatch("/v1/**") // Todas las rutas
                .pathsToMatch("/api/books/**") //Solo productos
                .displayName("Tienda libros Nullers")
                .build();
    }
}