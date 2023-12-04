package com.nullers.restbookstore.rest.user.dto;

import com.nullers.restbookstore.rest.user.models.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Respuesta de usuario con info
 *
 * @Author Binwei Wang
 */
@Getter
@Builder
@AllArgsConstructor
public class UserInfoResponse {
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
    private Set<UserRole> userRoles = Set.of(UserRole.USER);
    @Schema(name = "Pedidos")
    private List<String> order;
    @Builder.Default
    @Schema(name = "Usuario borrado", example = "true")
    private Boolean isDeleted = false;
}