package com.nullers.restbookstore.rest.user.services;

import com.nullers.restbookstore.rest.user.dto.UserInfoResponse;
import com.nullers.restbookstore.rest.user.dto.UserRequest;
import com.nullers.restbookstore.rest.user.dto.UserResponse;
import com.nullers.restbookstore.rest.user.exceptions.UserNameOrEmailExists;
import com.nullers.restbookstore.rest.user.exceptions.UserNotFound;
import com.nullers.restbookstore.rest.user.mappers.UserMapper;
import com.nullers.restbookstore.rest.user.model.User;
import com.nullers.restbookstore.rest.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Servicio para la entidad User
 */
@Service
@Slf4j
@CacheConfig(cacheNames = {"users"})
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Constructor de la clase
     *
     * @param userRepository repositorio de usuarios
     * @param userMapper     mapper de usuarios
     */
    public UserServiceImp(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Busca todos los usuarios
     *
     * @param username  nombre de usuario
     * @param email     email del usuario
     * @param isDeleted si el usuario está borrado
     * @param pageable  paginación
     * @return Page de UserResponse
     */
    @Override
    @Cacheable
    public Page<UserResponse> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isDeleted, Pageable pageable) {
        log.info("Buscando todos los usuarios con username: " + username + " y borrados: " + isDeleted);
        // Criterio de búsqueda por nombre
        Specification<User> specUsernameUser = (root, query, criteriaBuilder) ->
                username.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        // Criterio de búsqueda por email
        Specification<User> specEmailUser = (root, query, criteriaBuilder) ->
                email.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        // Criterio de búsqueda por borrado
        Specification<User> specIsDeleted = (root, query, criteriaBuilder) ->
                isDeleted.map(m -> criteriaBuilder.equal(root.get("isDeleted"), m))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        // Combinamos las especificaciones
        Specification<User> criterio = Specification.where(specUsernameUser)
                .and(specEmailUser)
                .and(specIsDeleted);

        // Debe devolver un Page, por eso usamos el findAll de JPA
        return userRepository.findAll(criterio, pageable).map(userMapper::toUserResponse);
    }

    /**
     * Busca un usuario por id
     *
     * @param id id del usuario
     * @return usuario encontrado
     */

    @Override
    @Cacheable(key = "#id")
    public UserInfoResponse findById(UUID id) {
        log.info("Buscando usuario por id: " + id);
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFound("Usuario no encontrado"));
        return userMapper.toUserInfoResponse(user);
    }

    /**
     * Guarda un usuario en la base de datos
     *
     * @param userRequest usuario a guardar
     * @return usuario guardado
     */
    @Override
    @Cacheable(key = "#result.id")
    public UserResponse save(UserRequest userRequest) {
        log.info("Guardando usuario: " + userRequest);
        userRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(userRequest.getUsername(),
                        userRequest.getEmail())
                .ifPresent(user -> {
                    throw new UserNameOrEmailExists("El usuario ya existe");
                });
        return userMapper.toUserResponse(userRepository.save(userMapper.toUser(userRequest)));
    }

    /**
     * Actualiza un usuario en la base de datos
     *
     * @param id          id del usuario a actualizar
     * @param userRequest usuario a actualizar
     * @return usuario actualizado
     */
    @Override
    @CachePut(key = "#result.id")
    public UserResponse update(UUID id, UserRequest userRequest) {
        log.info("Actualizando usuario: " + userRequest);
        userRepository.findById(id).orElseThrow(() -> new UserNotFound("Usuario no encontrado"));
        userRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(userRequest.getUsername(), userRequest.getEmail())
                .ifPresent(user -> {
                    throw new UserNameOrEmailExists("El usuario ya existe");
                });
        return userMapper.toUserResponse(userRepository.save(userMapper.toUser(userRequest, id)));
    }

    /**
     * Borra un usuario de la base de datos
     *
     * @param id id del usuario a borrar
     */
    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public void deleteById(UUID id) {
        log.info("Borrando usuario por id: " + id);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFound("Usuario no encontrado"));
        userRepository.delete(user);
    }
}
