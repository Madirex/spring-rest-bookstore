package com.nullers.restbookstore.shop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateShopDto {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @NotBlank(message = "La ubicación no puede estar vacía")
    private String location;
}
