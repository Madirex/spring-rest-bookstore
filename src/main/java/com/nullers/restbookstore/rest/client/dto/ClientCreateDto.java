package com.nullers.restbookstore.rest.client.dto;

import com.nullers.restbookstore.rest.common.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase ClientCreateDto
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientCreateDto {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Schema(name = "Nombre", example = "Juan")
    private String name;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Schema(name = "Apellido", example = "García")
    private String surname;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe tener un formato valido")
    @Schema(name = "Email", example = "ejemplo@gmail.com")
    private String email;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Size(max = 11, min = 3, message = "El teléfono debe tener como máximo 11 caracteres y como mínimo 3")
    @Pattern(regexp = "^\\d*$", message = "El teléfono debe contener solo dígitos")
    @Schema(name = "Teléfono", example = "676453226")
    private String phone;

    @NotNull(message = "La dirección no puede estar vacía")
    @Schema(name = "Dirección")
    private @Valid Address address;
}
