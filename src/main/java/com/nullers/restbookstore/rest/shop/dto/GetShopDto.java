package com.nullers.restbookstore.rest.shop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nullers.restbookstore.rest.common.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;
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

    private List<Long> books_id;

    private List<UUID> clients_id;

    @Setter
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
