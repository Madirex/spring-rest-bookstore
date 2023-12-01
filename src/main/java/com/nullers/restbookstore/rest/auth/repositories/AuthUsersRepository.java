package com.nullers.restbookstore.rest.auth.repositories;

import com.nullers.restbookstore.rest.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for the User entity
 *
 * @Author: Binwei Wang
 */

public interface AuthUsersRepository extends JpaRepository<User, UUID> {
    /**
     * Busca un usuario por su username
     * @param username username del usuario
     * @return Optional con el usuario encontrado
     */
    Optional<User> findByUsername(String username);
}
