package com.nullers.restbookstore.rest.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

/**
 * Respuesta de usuario con info
 *
 * @Author Binwei Wang
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    @Schema(name = "ID", example = "660e8400-e29b-41d4-a716-446655440000")
    private UUID id;
    @Schema(name = "Nombre", example = "Pedro")
    private String name;
    @Schema(name = "Apellido", example = "Gómez")
    private String surname;
    @Schema(name = "Nombre de usuario", example = "usuario1")
    private String username;
    @Schema(name = "Email", example = "ejemplo@gmail.com")
    private String email;
    @Builder.Default
    @Schema(name = "Roles")
    private Set<Role> roles = Set.of(Role.USER);
    @Schema(name = "Usuario borrado", example = "true")
    private Boolean isDeleted = false;
}