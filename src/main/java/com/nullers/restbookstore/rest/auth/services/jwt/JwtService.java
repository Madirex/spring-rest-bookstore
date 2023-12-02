package com.nullers.restbookstore.rest.auth.services.jwt;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * JwtService interface
 *
 * @Author Binwei Wang
 */
public interface JwtService {
    /**
     * Extract username from token
     * @param token jwt token
     * @return username string
     */
    String extractUserName(String token);

    /**
     * Generate token
     * @param username username
     * @return jwt token
     */

    String generateToken(UserDetails username);

    /**
     * Validate token
     * @param token jwt token
     * @param userDetails user details
     * @return vuelve verdadero si el token es válido
     */

    boolean isTokenValid(String token,  UserDetails userDetails);
}
