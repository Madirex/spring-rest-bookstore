package com.nullers.restbookstore.rest.book.dto;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;

/**
 * Class PatchBookDTO
 *
 * @Author Madirex
 */
@Getter
@Builder
public class PatchBookDTO {
    private String name;

    private String author;

    private Long publisherId;

    private String image;

    private String description;

    @Min(value = 0, message = "El precio no puede estar en negativo")
    private Double price;

    private Boolean active;

    @Min(value = 0, message = "El stock no puede estar en negativo")
    @Builder.Default
    private Integer stock = 0;
}