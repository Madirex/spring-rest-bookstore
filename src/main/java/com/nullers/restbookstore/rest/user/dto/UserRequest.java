package com.nullers.restbookstore.rest.user.dto;

import com.nullers.restbookstore.rest.user.models.UserRole;
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
    private String name;

    @NotBlank(message = "Surname no puede estar vacío")
    private String surname;

    @NotBlank(message = "Username no puede estar vacío")
    private String username;

    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    @NotBlank(message = "Email no puede estar vacío")
    private String email;

    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 5, message = "Password debe tener al menos 5 caracteres")
    @Size(min = 5, message = "Password debe tener al menos 5 caracteres")
    private String password;

    @Builder.Default
    private Set<UserRole> userRoles = Set.of(UserRole.USER);

    @Builder.Default
    private Boolean isDeleted = false;
}