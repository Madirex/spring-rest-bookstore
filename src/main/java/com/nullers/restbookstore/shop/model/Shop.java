package com.nullers.restbookstore.shop.model;

import com.nullers.restbookstore.book.models.Book;
import com.nullers.restbookstore.client.models.Client;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Clase Shop
 *
 * @author alexdor00
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
    private LocalDateTime createdAt;  // Fecha de creación de la tienda.

    @LastModifiedDate
    private LocalDateTime updatedAt;  // Fecha de última modificación de la tienda.

    @Builder.Default
    private Boolean active = true;  // Estado activo de la tienda.

    /**
     * Método que se ejecuta antes de persistir un objeto.
     * Soluciona los problemas de generación de fecha.
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

    /**
     * Lista de libros asociados con la tienda.
     * La relación es de uno a muchos, indicando que una tienda puede tener varios libros.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "shop_id")  // shop_id es la columna en la tabla de Book que referencia a Shop.
    @Builder.Default
    private List<Book> books = new ArrayList<>();

    /**
     * Lista de clientes asociados con la tienda.
     * La relación es de uno a muchos, indicando que una tienda puede tener varios clientes.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "shop_id")  // shop_id es la columna en la tabla de Client que referencia a Shop.
    @Builder.Default
    private List<Client> clients = new ArrayList<>();
}
