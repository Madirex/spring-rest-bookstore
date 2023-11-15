package com.nullers.restbookstore.rest.user.services;

import com.nullers.restbookstore.rest.user.dto.UserInfoResponse;
import com.nullers.restbookstore.rest.user.dto.UserRequest;
import com.nullers.restbookstore.rest.user.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Servicio para la entidad User
 */
public interface UserService {
    /**
     * Busca todos los usuarios
     *
     * @param username  username
     * @param email     email
     * @param isDeleted ¿está eliminado?
     * @param pageable  paginación
     * @return lista de usuarios
     */
    Page<UserResponse> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isDeleted,
                               Pageable pageable);

    /**
     * Busca un usuario por ID
     *
     * @param id id del usuario
     * @return usuario encontrado
     */
    UserInfoResponse findById(UUID id);

    /**
     * Guarda un usuario
     *
     * @param userRequest petición de usuario
     * @return usuario guardado
     */
    UserResponse save(UserRequest userRequest);

    /**
     * Actualiza un usuario por ID
     *
     * @param id          id del usuario
     * @param userRequest petición de usuario
     * @return usuario actualizado
     */
    UserResponse update(UUID id, UserRequest userRequest);

    /**
     * Elimina un usuario por ID
     *
     * @param id id del usuario
     */
    void deleteById(UUID id);
}
