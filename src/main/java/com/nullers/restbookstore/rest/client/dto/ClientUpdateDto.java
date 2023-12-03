package com.nullers.restbookstore.rest.client.dto;

import com.nullers.restbookstore.rest.common.Address;
import io.swagger.v3.oas.annotations.media.Schema;
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

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientUpdateDto {
    @Length(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    @Schema(name = "Nombre", example = "Juan")
    private String name;

    @Length(min = 3, max = 80, message = "El apellido debe tener entre 3 y 80 caracteres")
    @Schema(name = "Apellido", example = "García")
    private String surname;

    @Email(message = "El email debe tener un formato valido")
    @Schema(name = "Email", example = "ejemplo@gmail.com")
    private String email;

    @Size(max = 11, min =3, message = "El telefono debe tener como maximo 11 caracteres y como minimo 3")
    @Pattern(regexp = "^\\d*$", message = "El telefono debe contener solo digitos")
    @Schema(name = "Teléfono", example = "676453226")
    private String phone;

    @NotNull(message = "La direccion no puede estar vacia")
    @Schema(name = "Dirección")
    private @Valid Address address;
}
