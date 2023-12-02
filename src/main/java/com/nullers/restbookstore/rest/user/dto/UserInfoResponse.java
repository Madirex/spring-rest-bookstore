package com.nullers.restbookstore.rest.user.dto;

import com.nullers.restbookstore.rest.orders.models.Order;
import lombok.*;

import java.util.ArrayList;
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
    private Set<UserRole> userRoles = Set.of(UserRole.USER);
    private List<String> order = new ArrayList<>();
    @Builder.Default
    private Boolean isDeleted = false;
}