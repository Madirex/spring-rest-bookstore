package com.nullers.restbookstore.rest.book.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

/**
 * Class UpdateBookDTO
 *
 * @Author Madirex
 */
@Getter
@Builder
public class UpdateBookDTO {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @NotBlank(message = "El autor no puede estar vacío")
    private String author;

    @NotNull(message = "Publisher no puede ser nulo")
    private Long publisherId;

    @Min(value = 0, message = "El precio no puede estar en negativo")
    @NotNull(message = "El precio no puede ser nulo")
    private Double price;

    @NotBlank(message = "La imagen no puede estar vacía")
    private String image;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String description;

    private String category;

    @Min(value = 0, message = "El stock no puede estar en negativo")
    @Builder.Default
    private Integer stock = 0;

}