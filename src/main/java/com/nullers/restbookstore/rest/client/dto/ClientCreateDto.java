package com.nullers.restbookstore.rest.client.dto;

import com.nullers.restbookstore.rest.common.Address;
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
    private String name;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String surname;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe tener un formato valido")
    private String email;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Size(max = 11, min = 3, message = "El teléfono debe tener como máximo 11 caracteres y como mínimo 3")
    @Pattern(regexp = "^\\d*$", message = "El teléfono debe contener solo dígitos")
    private String phone;

    @NotNull(message = "La dirección no puede estar vacía")
    private @Valid Address address;
}
