package com.nullers.restbookstore.rest.shop.dto;

import com.nullers.restbookstore.rest.common.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

/**
 * Clase CreateShopDto
 *
 * @author alexdor00
 */
@Getter
@Builder
public class CreateShopDto {
    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(description = "Nombre", example = "Tienda Nullers")
    private String name;

    @NotNull(message = "La ubicación no puede estar vacía")
    @Schema(description = "Dirección de la tienda")
    @Valid
    private Address location;
}

