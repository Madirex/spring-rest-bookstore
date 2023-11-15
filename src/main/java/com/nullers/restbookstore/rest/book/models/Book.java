package com.nullers.restbookstore.rest.book.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nullers.restbookstore.rest.publisher.models.Publisher;
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
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @NotNull(message = "publisher no puede ser nulo")
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    @ToString.Exclude
    @JsonIgnore
    private Publisher publisher;

    @NotBlank(message = "La imagen no puede estar vacía")
    private String image;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String description;

    @NotNull(message = "El precio no puede estar vacío")
    @Min(value = 0, message = "El precio no puede estar en negativo")
    private Double price;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

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
