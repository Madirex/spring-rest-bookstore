package com.nullers.restbookstore.user.mappers;

import com.nullers.restbookstore.user.dto.UserInfoResponse;
import com.nullers.restbookstore.user.dto.UserRequest;
import com.nullers.restbookstore.user.dto.UserResponse;
import com.nullers.restbookstore.user.model.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Mapeador de usuario
 */
@Component
public class UserMapper {
    /**
     * Mapea un usuario a partir de una peticion
     *
     * @param request petición de usuario
     * @return usuario mapeado
     */
    public User toUser(UserRequest request) {
        return User.builder()
                .nombre(request.getNombre())
                .apellidos(request.getApellidos())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .roles(request.getRoles())
                .isDeleted(request.getIsDeleted())
                .build();
    }

    /**
     * Mapea un usuario a partir de una peticion y un id
     *
     * @param request petición de usuario
     * @param id      id del usuario
     * @return usuario mapeado con id
     */
    public User toUser(UserRequest request, UUID id) {
        return User.builder()
                .id(id)
                .nombre(request.getNombre())
                .apellidos(request.getApellidos())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .roles(request.getRoles())
                .isDeleted(request.getIsDeleted())
                .build();
    }

    /**
     * Mapea un usuario a partir de una peticion y un id
     *
     * @param user usuario
     * @return usuario mapeado con id
     */
    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .nombre(user.getNombre())
                .apellidos(user.getApellidos())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .isDeleted(user.getIsDeleted())
                .build();
    }

    /**
     * Mapea un usuario a partir de una peticion y un id
     *
     * @param user usuario autenticado
     * @return usuario mapeado con id
     */
    public UserInfoResponse toUserInfoResponse(User user/*, List<String> pedidos*/) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .nombre(user.getNombre())
                .apellidos(user.getApellidos())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .isDeleted(user.getIsDeleted())
//                .pedidos(pedidos)
                .build();
    }
}
