package com.nullers.restbookstore.rest.client.model;

import com.nullers.restbookstore.rest.book.model.Book;
import com.nullers.restbookstore.rest.common.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Daniel
 * @see Book
 */
@Getter
@Setter
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
    @Schema(name = "ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Column(nullable = false)
    @Schema(name = "Nombre", example = "Juan")
    private String name;

    @Column(nullable = false)
    @Schema(name = "Apellido", example = "García")
    private String surname;

    @Column(nullable = false, unique = true)
    @Schema(name = "Email", example = "ejemplo@gmail.com")
    private String email;

    @Column(nullable = false)
    @Schema(name = "Teléfono", example = "676453226")
    private String phone;

    @Column(columnDefinition = "TEXT default '" + DEFAULT_IMAGE + "'")
    @Builder.Default
    @Schema(name = "Imagen", example = "https://st4.depositphotos.com/1049680/20734/i/450/depositphotos_207343968-stock-photo-young-hipster-man-happy-face.jpg")
    private String image = DEFAULT_IMAGE;

    @CreatedDate
    @Schema(name = "Fecha de creación", example = "2021-03-05T11:11:11")
    private LocalDateTime createdAt;

    @NotNull(message = "La dirección no puede estar vacía")
    @Embedded
    @Schema(name = "Dirección")
    private Address address;
}
