package com.nullers.restbookstore.rest.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para el request de inicio de sesión
 *
 * @Author Binwei Wang
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSignInRequest {
    @NotBlank(message = "Username no puede estar vacío")
    @Schema(description = "username", example = "Manolo42")
    private String username;

    @NotBlank(message = "Password no puede estar vacío")
    @Schema(description = "password", example = "123456QWERL-s")
    private String password;
}
