package com.nullers.restbookstore.rest.user.mapper;

import com.nullers.restbookstore.rest.user.dto.UserInfoResponse;
import com.nullers.restbookstore.rest.user.dto.UserRequest;
import com.nullers.restbookstore.rest.user.dto.UserResponse;
import com.nullers.restbookstore.rest.user.models.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Mapper de usuario
 *
 * @Author: Binwei Wang
 */
@Component
public class UserMapper {
    /**
     * Mapea un usuario a partir de una petición
     *
     * @param request petición de usuario
     * @return usuario mapeado
     */
    public User toUser(UserRequest request) {
        return User.builder()
                .name(request.getName())
                .surnames(request.getSurnames())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .roles(request.getRoles())
                .isDeleted(request.getIsDeleted())
                .build();
    }

    /**
     * Mapea un usuario a partir de una petición y un ID
     *
     * @param request petición de usuario
     * @param id      id del usuario
     * @return usuario mapeado con id
     */
    public User toUser(UserRequest request, UUID id) {
        return User.builder()
                .id(id)
                .name(request.getName())
                .surnames(request.getSurnames())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .roles(request.getRoles())
                .isDeleted(request.getIsDeleted())
                .build();
    }

    /**
     * Mapea un usuario a partir de una petición y un ID
     *
     * @param user usuario
     * @return usuario mapeado con ID
     */
    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .surnames(user.getSurnames())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .isDeleted(user.getIsDeleted())
                .build();
    }

    /**
     * Mapea un usuario a partir de una petición y un ID
     *
     * @param user usuario autenticado
     * @return usuario mapeado con ID
     */
    public UserInfoResponse toUserInfoResponse(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .surnames(user.getSurnames())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .isDeleted(user.getIsDeleted())
                .build();
    }
}
