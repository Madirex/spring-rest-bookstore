package com.nullers.restbookstore.rest.shop.dto;

import com.nullers.restbookstore.rest.common.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

/**
 * Clase UpdateShopDto
 *
 * @author alexdor00
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShopDto {
    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(description = "Nombre", example = "Tienda Nullers")
    private String name;

    @NotNull(message = "La ubicación no puede estar vacía")
    @Schema(description = "Dirección de la tienda")
    @Valid
    private Address location;

    @Builder.Default()
    @Schema(description = "Conjunto de libros disponibles en la tienda")
    private Set<Long> books = Set.of();

    @Builder.Default()
    @Schema(description = "Conjunto de clientes disponibles en la tienda")
    private Set<UUID> clients = Set.of();
}
