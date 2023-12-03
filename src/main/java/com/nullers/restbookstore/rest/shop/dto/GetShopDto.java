package com.nullers.restbookstore.rest.shop.dto;

import com.nullers.restbookstore.rest.common.Address;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
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

    private Address location;

    private Set<Long> booksId;

    private Set<UUID> clientsId;

    @Setter
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
