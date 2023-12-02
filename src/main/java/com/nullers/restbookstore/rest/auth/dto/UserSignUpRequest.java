package com.nullers.restbookstore.rest.auth.dto;

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
    private String nombre;
    @NotBlank(message = "Apellidos no puede estar vacío")
    private String apellidos;
    @NotBlank(message = "Username no puede estar vacío")
    private String username;
    @NotBlank(message = "Email no puede estar vacío")
    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    private String email;
    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 5, message = "Password debe tener al menos 5 caracteres")
    private String password;
    @NotBlank(message = "Password de comprobación no puede estar vacío")
    @Length(min = 5, message = "Password de comprobación debe tener al menos 5 caracteres")
    private String passwordComprobacion;
}
