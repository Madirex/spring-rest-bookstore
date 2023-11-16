package com.nullers.restbookstore.rest.shop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

/**
 * Clase CreateShopDto
 *
 *  @author alexdor00
 */
@Getter
@Builder
public class CreateShopDto {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;  // Nombre de la tienda, no debe estar vacío.

    @NotBlank(message = "La ubicación no puede estar vacía")
    private String location;  // Ubicación de la tienda, no debe estar vacía.
}

