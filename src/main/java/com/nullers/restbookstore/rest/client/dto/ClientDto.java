package com.nullers.restbookstore.rest.client.dto;

import com.nullers.restbookstore.rest.common.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

/**
 * Clase ClientDto
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

    @Schema(name = "ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;
    @Schema(name = "Nombre", example = "Juan")
    private String name;
    @Schema(name = "Apellido", example = "García")
    private String surname;
    @Schema(name = "Email", example = "ejemplo@gmail.com")
    private String email;
    @Schema(name = "Teléfono", example = "676453226")
    private String phone;
    @Schema(name = "Dirección")
    private Address address;
    @Schema(name = "Imagen", example = "https://st4.depositphotos.com/1049680/20734/i/450/depositphotos_207343968-stock-photo-young-hipster-man-happy-face.jpg")
    private String image;

}
