package com.nullers.restbookstore.rest.category.dto;

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
    private String name;
    @Builder.Default
    private boolean isActive = true;
}
