package com.nullers.restbookstore.rest.client.dto;

import com.nullers.restbookstore.rest.client.model.Address;
import jakarta.validation.Valid;
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
    @Email(message = "El email debe tener un formato valido")
    private String email;

    @NotBlank(message = "El telefono no puede estar vacio")
    @Size(max = 11, min = 3, message = "El telefono debe tener como maximo 11 caracteres y como minimo 3")
    @Pattern(regexp = "^\\d*$", message = "El telefono debe contener solo digitos")
    private String phone;

    @NotNull(message = "La direccion no puede estar vacia")
    private @Valid Address address;

}
