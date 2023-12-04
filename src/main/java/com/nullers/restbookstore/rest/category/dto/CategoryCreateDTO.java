package com.nullers.restbookstore.rest.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

/**
 * DTO para crear una categoría
 */
@Data
@Builder
public class CategoryCreateDTO {
    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(description = "Nombre de la categoría", example = "Terror")
    private String name;

    @Builder.Default
    @Schema(description = "Categoría activada", example = "true")
    private boolean isActive = true;
}
