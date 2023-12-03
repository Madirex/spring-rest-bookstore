package com.nullers.restbookstore.rest.auth.services.users;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * AuthUsersService interface
 *
 * @Author Binwei Wang
 */
public interface AuthUsersService extends UserDetailsService {
    /**
     * Cargar los datos de un usuario a partir de su nombre de usuario.
     *
     * @param username Nombre de usuario
     * @return UserDetails
     */
    @Override
    UserDetails loadUserByUsername(String username);
}
