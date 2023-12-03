package com.nullers.restbookstore.rest.publisher.model;

import com.nullers.restbookstore.rest.book.model.Book;
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
@Builder
@Entity
public class Publisher {
    public static final String DEFAULT_IMAGE = "https://books.madirex.com/favicon.ico";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "el nombre no puede estar vacío")
    private String name;

    @NotBlank(message = "la imagen no puede estar vacía")
    private String image;

    @OneToMany(mappedBy = "publisher")
    @Builder.Default
    private Set<Book> books = new HashSet<>();

    @Builder.Default
    private Boolean active = true;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
