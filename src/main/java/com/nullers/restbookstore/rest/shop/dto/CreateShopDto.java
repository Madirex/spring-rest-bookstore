package com.nullers.restbookstore.rest.shop.dto;

import com.nullers.restbookstore.rest.common.Address;
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
    private String name;

    @NotNull(message = "La ubicación no puede estar vacía")
    @Valid
    private Address location;
}

