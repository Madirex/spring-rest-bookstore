package com.nullers.restbookstore.rest.shop.model;


import com.nullers.restbookstore.rest.book.model.Book;
import com.nullers.restbookstore.rest.client.model.Client;
import com.nullers.restbookstore.rest.common.Address;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Clase Shop
 *
 * @author alexdor00
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "shops")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    @Embedded
    @NotNull(message = "La ubicación no puede estar vacía")
    @Valid
    private Address location;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany
    @JoinColumn(name = "book_id")
    @NotNull(message = "La tienda debe tener al menos un libro")
    @Builder.Default()
    private Set<Book> books = Set.of();

    @OneToMany
    @JoinColumn(name = "client_id")
    @NotNull(message = "La tienda debe tener al menos un cliente")
    @Builder.Default()
    private Set<Client> clients = Set.of();

}
