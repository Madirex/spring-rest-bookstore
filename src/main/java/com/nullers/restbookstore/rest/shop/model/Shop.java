package com.nullers.restbookstore.rest.shop.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nullers.restbookstore.rest.book.model.Book;
import com.nullers.restbookstore.rest.client.model.Client;
import com.nullers.restbookstore.rest.common.Address;
import com.nullers.restbookstore.rest.orders.models.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Clase Shop
 *
 *  @author alexdor00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shops")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "ID", example = "1")
    private UUID id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(description = "Nombre", example = "Tienda Nullers")
    private String name;

    @Embedded
    @NotNull(message = "La ubicación no puede estar vacía")
    @Schema(description = "Dirección de la tienda")
    @Valid
    private Address location;

    @CreatedDate
    @Schema(description = "Fecha de creación", example = "2021-08-01T00:00:00.000Z")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Schema(description = "Fecha de actualización", example = "2021-08-01T00:00:00.000Z")
    private LocalDateTime updatedAt;

    @OneToMany
    @JoinColumn(name = "book_id")
    @Schema(description = "Conjunto de libros disponibles en la tienda")
    @Builder.Default()
    private Set<Book> books = Set.of();


    @OneToMany
    @JoinColumn(name = "client_id")
    @Schema(description = "Conjunto de clientes de la tienda")    @NotNull(message = "La tienda debe tener al menos un cliente")
    @Builder.Default()
    private Set<Client> clients = Set.of();

}
