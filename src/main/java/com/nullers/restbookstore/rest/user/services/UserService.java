package com.nullers.restbookstore.rest.user.services;

import com.nullers.restbookstore.rest.user.dto.UserInfoResponse;
import com.nullers.restbookstore.rest.user.dto.UserResponse;
import com.nullers.restbookstore.rest.user.dto.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Servicio para la entidad User
 */
public interface UserService {
    Page<UserResponse> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isDeleted, Pageable pageable);

    UserInfoResponse findById(UUID id);

    UserResponse save(UserRequest userRequest);

    UserResponse update(UUID id, UserRequest userRequest);

    void deleteById(UUID id);
}
