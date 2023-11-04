package com.nullers.restbookstore.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

/**
 * Class UpdateBookDTO
 */
@Getter
@Builder
public class UpdateBookDTO {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @NotNull(message = "publisher no puede ser nulo")
    private String publisherId;

    @NotBlank(message = "La imagen no puede estar vacía")
    private String image;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String description;
}