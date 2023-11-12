package com.nullers.restbookstore.client.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientCreateDto {

    @NotBlank(message = "El nombre no puede estar vacio")
    private String name;

    @NotBlank(message = "El apellido no puede estar vacio")
    private String surname;

    @NotBlank(message = "El email no puede estar vacio")
    @Email(message = "El email no es valido")
    private String email;

    @NotBlank(message = "El telefono no puede estar vacio")
    @Size(max = 11, message = "El teléfono debe tener como máximo 11 caracteres")
    @Pattern(regexp = "^\\d*$", message = "El teléfono debe contener solo dígitos")
    private String phone;

    @NotBlank(message = "La direccion no puede estar vacio")
    private String address;

}
