package com.nullers.restbookstore.rest.user.dto;

import com.nullers.restbookstore.rest.user.models.Role;
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
    private UUID id;
    private String name;
    private String surname;
    private String username;
    private String email;
    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);
    private List<String> order;
    @Builder.Default
    private Boolean isDeleted = false;
}