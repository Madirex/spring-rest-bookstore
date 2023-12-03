package com.nullers.restbookstore.rest.category.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Category entity
 */
@Builder
@Getter
@Setter
@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Id de la categoría", example = "23ebd873-4667-4679-bfc8-9f126cc7b04f")
    private UUID id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Nombre de la categoría", example = "Terror")
    private String name;

    @Builder.Default
    @Schema(description = "Categoría activada", example = "true")
    private Boolean isActive = true;

    @Temporal(TemporalType.TIMESTAMP)
    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    @Schema(description = "Fecha de creación", example = "2021-10-10T10:10:10")
    private LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @ColumnDefault(value = "CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Schema(description = "Fecha de actualización", example = "2021-10-10T10:10:10")
    private LocalDateTime updatedAt;

    /**
     * Set createdAt and updatedAt timestamps
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