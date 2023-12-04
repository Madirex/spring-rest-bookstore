package com.nullers.restbookstore.rest.user.dto;

import com.nullers.restbookstore.rest.user.models.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

/**
 * Petición de usuario para crear
 *
 * @Author Binwei Wang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank(message = "Name no puede estar vacío")
    @Schema(name = "Nombre", example = "Pedro")
    private String name;

    @NotBlank(message = "Surname no puede estar vacío")
    @Schema(name = "Apellido", example = "Gómez")
    private String surname;

    @NotBlank(message = "Username no puede estar vacío")
    @Schema(name = "Nombre de usuario", example = "usuario1")
    private String username;

    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    @NotBlank(message = "Email no puede estar vacío")
    @Schema(name = "Email", example = "ejemplo@gmail.com")
    private String email;

    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 5, message = "Password debe tener al menos 5 caracteres")
    @Size(min = 5, message = "Password debe tener al menos 5 caracteres")
    @Schema(name = "Contraseña", example = "ejemplo1234")
    private String password;

    @Builder.Default
    @Schema(name = "Roles")
    private Set<Role> roles = Set.of(Role.USER);

    @Builder.Default
    @Schema(name = "Usuario borrado", example = "true")
    private Boolean isDeleted = false;
}