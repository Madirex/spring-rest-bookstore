package com.nullers.restbookstore.rest.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * DTO para el registro de usuarios
 *
 * @Author Binwei Wang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpRequest {
    @NotBlank(message = "Nombre no puede estar vacío")
    @Schema(description = "nombre", example = "Manolo")
    private String name;

    @NotBlank(message = "Apellidos no puede estar vacío")
    @Schema(description = "apellidos", example = "García")
    private String surname;

    @NotBlank(message = "Username no puede estar vacío")
    @Schema(description = "username", example = "Manolo42")
    private String username;

    @NotBlank(message = "Email no puede estar vacío")
    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    @Schema(description = "email", example = "contact@madirex.com")
    private String email;

    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 5, message = "Password debe tener al menos 5 caracteres")
    @Schema(description = "password", example = "123456QWERL-s")
    private String password;

    @NotBlank(message = "Password de comprobación no puede estar vacío")
    @Length(min = 5, message = "Password de comprobación debe tener al menos 5 caracteres")
    @Schema(description = "repetir password", example = "123456QWERL-s")
    private String passwordRepeat;
}
