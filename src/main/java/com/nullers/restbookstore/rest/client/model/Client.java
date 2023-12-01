package com.nullers.restbookstore.rest.client.model;

import com.nullers.restbookstore.rest.book.model.Book;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author daniel
 * @see Book
 * Modelo de cliente
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
@ToString
public class Client {
    public static final String DEFAULT_IMAGE = "https://via.placeholder.com/150";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(columnDefinition = "TEXT default '" + DEFAULT_IMAGE + "'") // Por defecto una imagen
    @Builder.Default
    private String image = DEFAULT_IMAGE;

    @CreatedDate
    private LocalDateTime createdAt;

    @NotNull(message = "La dirección no puede estar vacía")

    @Embedded
    private Address address;

}
