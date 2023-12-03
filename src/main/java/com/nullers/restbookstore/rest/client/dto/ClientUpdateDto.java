package com.nullers.restbookstore.rest.client.dto;

import com.nullers.restbookstore.rest.common.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * Clase ClientUpdateDto
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientUpdateDto {
    @Length(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String name;
    @Length(min = 3, max = 80, message = "El apellido debe tener entre 3 y 80 caracteres")
    private String surname;

    @Email(message = "El email debe tener un formato valido")
    private String email;

    @Size(max = 11, min = 3, message = "El teléfono debe tener como máximo 11 caracteres y como mínimo 3")
    @Pattern(regexp = "^\\d*$", message = "El teléfono debe contener solo dígitos")
    private String phone;

    @NotNull(message = "La dirección no puede estar vacía")
    private @Valid Address address;
}
