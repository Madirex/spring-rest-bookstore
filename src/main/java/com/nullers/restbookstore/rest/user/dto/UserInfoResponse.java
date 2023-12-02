package com.nullers.restbookstore.rest.user.dto;

import com.nullers.restbookstore.rest.orders.models.Order;
import com.nullers.restbookstore.rest.user.models.Role;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Respuesta de usuario con info
 *
 * @Author: Binwei Wang
 */
@Getter
@Builder
@AllArgsConstructor
public class UserInfoResponse {
    private UUID id;
    private String name;
    private String surnames;
    private String username;
    private String email;
    @Builder.Default
    private List<String> order = new ArrayList<>();
    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);
    @Builder.Default
    private Boolean isDeleted = false;
}