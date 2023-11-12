package com.nullers.restbookstore.client.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientUpdateDto {
    @Length(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String name;
    @Length(min = 3, max = 80, message = "El apellido debe tener entre 3 y 80 caracteres")
    private String surname;

    @Email(message = "El email no es valido")
    private String email;

    @Size(max = 11, message = "El teléfono debe tener como máximo 11 caracteres")
    @Pattern(regexp = "^\\d*$", message = "El teléfono debe contener solo dígitos")
    private String phone;

    @Length(min = 3, max = 150, message = "La direccion debe tener entre 3 y 150 caracteres")
    private String address;
}
