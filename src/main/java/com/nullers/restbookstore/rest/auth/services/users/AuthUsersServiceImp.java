package com.nullers.restbookstore.rest.auth.services.users;

import com.nullers.restbookstore.rest.auth.repositories.AuthUsersRepository;
import com.nullers.restbookstore.rest.user.exceptions.UserNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * AuthUsersServiceImp implements AuthUsersService
 *
 * @Author Binwei Wang
 */
@Service("userDetailsService")
public class AuthUsersServiceImp implements AuthUsersService {
    /**
     * AuthUsersRepository
     */
    private final AuthUsersRepository authUsersRepository;

    /**
     * Constructor for AuthUsersServiceImp
     *
     * @param authUsersRepository AuthUsersRepository
     */

    @Autowired
    public AuthUsersServiceImp(AuthUsersRepository authUsersRepository) {
        this.authUsersRepository = authUsersRepository;
    }

    /**
     * loadUserByUsername
     *
     * @param username Nombre de usuario
     * @return UserDetails
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        return authUsersRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UserNotFound("Usuario con username " + username + " no encontrado"));
    }
}
