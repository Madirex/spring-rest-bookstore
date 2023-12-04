package com.nullers.restbookstore.rest.shop.dto;

import com.nullers.restbookstore.rest.common.Address;
import io.swagger.v3.oas.annotations.media.Schema;

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

    @Schema(description = "ID", example = "1")
    private UUID id;

    @Schema(description = "Nombre", example = "Tienda Nullers")
    private String name;

    @NotNull(message = "La ubicación no puede estar vacía")
    private Address location;

    @Schema(description = "IDs de libros asociados a la tienda")
    private Set<Long> booksId;

    @Schema(description = "IDs de clientes asociados a la tienda")
    private Set<UUID> clientsId;


    @Schema(description = "Fecha de creación", example = "2021-08-01T00:00:00.000Z")
    @Setter
    private LocalDateTime createdAt;

    @Schema(description = "Fecha actualizada", example = "2021-08-01T00:00:00.000Z")
    private LocalDateTime updatedAt;
}
