package com.nullers.restbookstore.rest.user.dto;

import com.nullers.restbookstore.rest.user.models.UserRole;
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
    private UUID id;
    private String name;
    private String surname;
    private String username;
    private String email;
    @Builder.Default
    private Set<UserRole> userRoles = Set.of(UserRole.USER);
    private Boolean isDeleted = false;
}