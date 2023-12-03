package com.nullers.restbookstore.rest.book.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nullers.restbookstore.rest.category.model.Category;
import com.nullers.restbookstore.rest.publisher.model.Publisher;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * Clase Book
 *
 * @Author Madirex
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Book {
    public static final String IMAGE_DEFAULT = "https://books.madirex.com/favicon.ico";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Schema(description = "ID", example = "1")
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(description = "Nombre", example = "¿El asesino sigue aquí?")
    private String name;

    @NotBlank(message = "El autor no puede estar vacío")
    @Schema(description = "Autor", example = "Madirex")
    private String author;

    @NotNull(message = "publisher no puede ser nulo")
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    @ToString.Exclude
    @JsonIgnore
    @Schema(description = "Editorial")
    private Publisher publisher;

    @NotBlank(message = "La imagen no puede estar vacía")
    @Schema(description = "Imagen", example = "https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEgPej2LyNLEL2RS-SwcNUVkHV6ZULZ6a6QvaUX68BiNEyCmMaVVUuiU6-MVxXYO-WWwATrUvLuJN7RVLkAC5x-arYpMNYc7-cGFkc0vrlfSP4MAexQV1SopKOgEbfNMVkhfWGm7kTJ5StWsB_f4kOP6DCG8YGe7c7W_w2ReV9v6D8HRc7veA_FsxaL6ec3g/w680/El%20asesino%20sigue%20aqu%C3%AD.png")
    private String image;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Schema(description = "Descripción", example = "Manuel es un detective que vive junto a su hijo Toni en el pueblo Risirú. En el pasado, ambos sufrieron la pérdida de un ser querido. La mujer de Manuel había sido asesinada. Pasado un tiempo y con ayuda de profesionales, consiguieron superar el trauma que les había dejado ese asesino.Risirú tenía un pasado muy oscuro, lleno de delincuencia. Manuel consiguió erradicar por completo la mala fama que tenía ese pueblo.Años después... Volvió a morir alguien.Manuel y Toni se preguntaron:¿El asesino sigue aquí?")
    private String description;

    @NotNull(message = "El precio no puede estar vacío")
    @Min(value = 0, message = "El precio no puede estar en negativo")
    @Schema(description = "Precio", example = "12.99")
    private Double price;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    @Schema(description = "Fecha de creación", example = "2021-08-01T00:00:00.000Z")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Schema(description = "Fecha de actualización", example = "2021-08-01T00:00:00.000Z")
    private LocalDateTime updatedAt;

    @NotNull
    @Schema(description = "Libro activo (en caso contrario, se entiende que está eliminado)", example = "true")
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @Schema(description = "Categoría")
    private Category category;

    @Min(value = 0, message = "El stock no puede estar en negativo")
    @Builder.Default
    @Schema(description = "Stock de los libros", example = "1")
    private Integer stock = 0;

    /**
     * Método que se ejecuta antes de persistir un objeto
     * Soluciona los problemas de generación de fecha
     */
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

}
