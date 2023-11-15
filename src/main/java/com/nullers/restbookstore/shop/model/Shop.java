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
import java.util.List;
import java.util.UUID;


/**
 * Clase Shop
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
    private UUID id;

    private String name;
    private String location;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


    // Lista de libros asociados con la tienda
    @OneToMany(mappedBy = "shop")
    private Book book;

    // Lista de clientes asociados con la tienda
    @OneToMany(mappedBy = "shop")
    private List<Client> clients;


}