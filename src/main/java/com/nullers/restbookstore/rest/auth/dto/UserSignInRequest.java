package com.nullers.restbookstore.rest.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * DTO para el request de inicio de sesión
 *
 * @Author: Binwei Wang
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSignInRequest {
    @NotBlank(message = "Username no puede estar vacío")
    private String username;
    @NotBlank(message = "Password no puede estar vacío")
    @Length(min = 5, message = "Password debe tener al menos 5 caracteres")
    private String password;
}
