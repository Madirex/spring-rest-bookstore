package com.nullers.restbookstore.rest.client.dto;

import com.nullers.restbookstore.rest.common.Address;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(name = "Nombre", example = "Juan")
    private String name;

    @NotBlank(message = "El apellido no puede estar vacio")
    @Schema(name = "Apellido", example = "García")
    private String surname;

    @NotBlank(message = "El email no puede estar vacio")
    @Email(message = "El email debe tener un formato valido")
    @Schema(name = "Email", example = "ejemplo@gmail.com")
    private String email;

    @NotBlank(message = "El telefono no puede estar vacio")
    @Size(max = 11, min = 3, message = "El telefono debe tener como maximo 11 caracteres y como minimo 3")
    @Pattern(regexp = "^\\d*$", message = "El telefono debe contener solo digitos")
    @Schema(name = "Teléfono", example = "676453226")
    private String phone;

    @NotNull(message = "La direccion no puede estar vacia")
    @Schema(name = "Dirección")
    private @Valid Address address;

}
