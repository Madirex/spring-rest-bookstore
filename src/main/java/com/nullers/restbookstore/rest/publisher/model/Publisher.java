package com.nullers.restbookstore.rest.publisher.model;

import com.nullers.restbookstore.rest.book.model.Book;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Clase Publisher
 *
 * @author jaimesalcedo1
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
public class Publisher {
    public static final String DEFAULT_IMAGE = "https://books.madirex.com/favicon.ico";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Schema(name = "ID", example = "1")
    private Long id;

    @NotBlank(message = "el nombre no puede estar vacío")
    @Schema(name = "Nombre", example = "Planeta")
    private String name;

    @NotBlank(message = "la imagen no puede estar vacía")
    @Schema(name = "Imagen", example = "https://proassetspdlcom.cdnstatics2.com/usuaris/editorial/logo/d8253153-6647-454f-884e-b923429307f3-planeta.svg")
    private String image;

    @OneToMany(mappedBy = "publisher")
    @Builder.Default
    @Schema(name = "Libros")
    private Set<Book> books = new HashSet<>();

    @Builder.Default
    @Schema(name = "Editorial activa", example = "true")
    private Boolean active = true;

    @CreatedDate
    @Schema(name = "Fecha de creación", example = "2021-03-05T11:11:11")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Schema(name = "Fecha de actualización", example = "2021-03-05T11:11:11")
    private LocalDateTime updatedAt;
}
