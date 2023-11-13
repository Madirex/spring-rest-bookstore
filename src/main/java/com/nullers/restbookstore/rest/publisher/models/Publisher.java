package com.nullers.restbookstore.rest.publisher.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Clase Publisher
 *
 * @author jaimesalcedo1
 * */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
public class Publisher {
    public static final String DEFAULT_IMAGE = "https://via.placeholder.com/200";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "el nombre no puede estar vacío")
    private String name;


    @NotBlank(message = "la imagen no puede estar vacía")
    private String image;

    @OneToMany
    @JoinColumn(name = "book_id")
    @Builder.Default
    private Set<Book> books = new HashSet<>();

    @CreatedDate
    private LocalDateTime created_at;

    @LastModifiedDate
    private LocalDateTime updated_at;
}
