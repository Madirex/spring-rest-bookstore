package com.nullers.restbookstore.rest.user.dto;

import com.nullers.restbookstore.rest.user.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

/**
 * Respuesta de usuario con info
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private UUID id;
    private String nombre;
    private String apellidos;
    private String username;
    private String email;
    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);
    @Builder.Default
    private Boolean isDeleted = false;
//    @Builder.Default
//    private List<String> pedidos = new ArrayList<>();
}