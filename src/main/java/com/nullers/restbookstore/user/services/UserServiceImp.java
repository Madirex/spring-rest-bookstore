package com.nullers.restbookstore.user.services;

import com.nullers.restbookstore.user.dto.UserInfoResponse;
import com.nullers.restbookstore.user.dto.UserRequest;
import com.nullers.restbookstore.user.dto.UserResponse;
import com.nullers.restbookstore.user.exception.UserNotFound;
import com.nullers.restbookstore.user.mappers.UserMapper;
import com.nullers.restbookstore.user.model.User;
import com.nullers.restbookstore.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@CacheConfig(cacheNames = {"users"})
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImp(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
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

    @Override
    @Cacheable(key = "#id")
    public UserInfoResponse findById(UUID id) {
        log.info("Buscando usuario por id: " + id);
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFound("Usuario no encontrado"));
        return userMapper.toUserInfoResponse(user);
    }

    @Override
    @Cacheable(key = "#result.id")
    public UserResponse save(UserRequest userRequest) {
        log.info("Guardando usuario: " + userRequest);
        userRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(userRequest.getUsername(), userRequest.getEmail())
                .ifPresent(user -> {
                    throw new UsernameNotFoundException("El usuario ya existe");
                });
        return userMapper.toUserResponse(userRepository.save(userMapper.toUser(userRequest)));
    }

    @Override
    @CachePut(key = "#result.id")
    public UserResponse update(UUID id, UserRequest userRequest) {
        log.info("Actualizando usuario: " + userRequest);
        userRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(userRequest.getUsername(), userRequest.getEmail())
                .ifPresent(user -> {
                    throw new UsernameNotFoundException("El usuario ya existe");
                });
        return userMapper.toUserResponse(userRepository.save(userMapper.toUser(userRequest, id)));
    }

    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public void deleteById(UUID id) {
        log.info("Borrando usuario por id: " + id);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFound("Usuario no encontrado"));
        userRepository.delete(user);
    }
}
