package com.nullers.restbookstore.client.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
    @Min(value = 0, message = "El telefono no puede ser negativo")
    @Max(value = 999999999, message = "El telefono no puede ser mayor a 999999999")
    private String phone;

    @NotBlank(message = "La direccion no puede estar vacio")
    private String address;

}
