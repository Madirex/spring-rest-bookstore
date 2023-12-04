package com.nullers.restbookstore.rest.book.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Nombre", example = "¿El asesino sigue aquí?")
    private String name;

    @Schema(description = "Autor", example = "Madirex")
    private String author;

    @Schema(description = "ID de la editorial", example = "1")
    private Long publisherId;

    @Schema(description = "Imagen", example = "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEgPej2LyNLEL2RS-SwcNUVkHV6ZULZ6a6QvaUX68BiNEyCmMaVVUuiU6-MVxXYO-WWwATrUvLuJN7RVLkAC5x-arYpMNYc7-cGFkc0vrlfSP4MAexQV1SopKOgEbfNMVkhfWGm7kTJ5StWsB_f4kOP6DCG8YGe7c7W_w2ReV9v6D8HRc7veA_FsxaL6ec3g/w680/El%20asesino%20sigue%20aqu%C3%AD.png")
    private String image;

    @Schema(description = "Descripción", example = "Manuel es un detective que vive junto a su hijo Toni en el pueblo Risirú. En el pasado, ambos sufrieron la pérdida de un ser querido. La mujer de Manuel había sido asesinada. Pasado un tiempo y con ayuda de profesionales, consiguieron superar el trauma que les había dejado ese asesino.Risirú tenía un pasado muy oscuro, lleno de delincuencia. Manuel consiguió erradicar por completo la mala fama que tenía ese pueblo.Años después... Volvió a morir alguien.Manuel y Toni se preguntaron:¿El asesino sigue aquí?")
    private String description;

    @Min(value = 0, message = "El precio no puede estar en negativo")
    @Schema(description = "Precio", example = "12.99")
    private Double price;

    @Schema(description = "Libro activo (en caso contrario, se entiende que está eliminado)", example = "true")
    private Boolean active;

    @Schema(description = "UUID de la categoría", example = "8542eb90-1f3d-4d12-8ba7-8f5b46c03f18")
    private String category;

    @Min(value = 0, message = "El stock no puede estar en negativo")
    @Builder.Default
    @Schema(description = "Stock de los libros", example = "1")
    private Integer stock = 0;
}