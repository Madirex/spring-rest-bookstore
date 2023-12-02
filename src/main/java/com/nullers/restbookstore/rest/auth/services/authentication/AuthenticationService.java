package com.nullers.restbookstore.rest.auth.services.authentication;

import com.nullers.restbookstore.rest.auth.dto.JwtAuthResponse;
import com.nullers.restbookstore.rest.auth.dto.UserSignInRequest;
import com.nullers.restbookstore.rest.auth.dto.UserSignUpRequest;

/**
 * Authentication service interface
 *
 * @Author: Binwei Wang
 */
public interface AuthenticationService {
    /**
     *  Registrar usuario
     * @param request UserSignUpRequest
     * @return JwtAuthResponse con token
     */
    JwtAuthResponse signUp(UserSignUpRequest request);

    /**
     * Iniciar sesión de usuario
     * @param request UserSignInRequest
     * @return JwtAuthResponse con token
     */

    JwtAuthResponse signIn(UserSignInRequest request);
}
