package com.nullers.restbookstore.rest.shop.dto;

import com.nullers.restbookstore.rest.common.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.UUID;

/**
 * Clase PatchShopDto
 *
 *  @author alexdor00
 */

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatchShopDto {

    @NotBlank(message = "El id no puede estar vacío")
    private UUID id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @NotBlank(message = "La ubicación no puede estar vacía")
    @Valid
    private Address location;

    @Builder.Default()
    private List<Long> books = List.of();
    @Builder.Default()
    private List<UUID> clients = List.of();
}
