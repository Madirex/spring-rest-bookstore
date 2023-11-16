package com.nullers.restbookstore.rest.shop.model;


import com.nullers.restbookstore.rest.book.models.Book;
import com.nullers.restbookstore.rest.client.models.Client;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;  // Identificador único para la tienda.

    private String name;  // Nombre de la tienda.
    private String location;  // Ubicación de la tienda.

    @CreatedDate
    private LocalDateTime createdAt;  // Fecha y hora de creación de la tienda.

    @LastModifiedDate
    private LocalDateTime updatedAt;  // Fecha y hora de la última actualización de la tienda.

    /**
     * Lista de libros asociados con la tienda.
     * La relación es de uno a muchos, indicando que una tienda puede tener varios libros.
     */
//    @OneToMany
//    @JoinColumn(name = "book_id")
//    private List<Book> books;

    /**
     * Lista de clientes asociados con la tienda.
     * La relación es de uno a muchos, indicando que una tienda puede tener varios clientes.
     */
    @OneToMany
    @JoinColumn(name = "client_id")
    private List<Client> clients;
}
