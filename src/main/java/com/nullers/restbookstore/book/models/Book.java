package com.nullers.restbookstore.book.models;

import com.nullers.restbookstore.NOADD.Publisher;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Clase Book
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
public class Book {
    @NotNull
    @Id
    private UUID id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @NotNull(message = "publisher no puede ser nulo")
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @NotBlank(message = "La imagen no puede estar vacía")
    private String image;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String description;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
