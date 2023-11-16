package com.nullers.restbookstore.rest.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Clase GetShopDto que representa el Data Transfer Object (DTO) para obtener la información de una tienda.
 * Incluye detalles como el identificador, nombre, ubicación, y las fechas de creación y actualización de la tienda.
 *
 * @author alexdor00
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetShopDto {
    private UUID id;

    private String name;

    private String location;

    @Setter
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
