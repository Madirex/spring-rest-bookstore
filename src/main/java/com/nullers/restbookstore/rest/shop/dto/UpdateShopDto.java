package com.nullers.restbookstore.rest.shop.dto;

import com.nullers.restbookstore.rest.common.Address;
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
    private String name;

    @NotNull(message = "La ubicación no puede estar vacía")
    @Valid
    private Address location;

    @Builder.Default()
    private Set<Long> books = Set.of();
    @Builder.Default()
    private Set<UUID> clients = Set.of();
}
