package com.nullers.restbookstore.rest.auth.controllers;

import com.nullers.restbookstore.rest.auth.dto.JwtAuthResponse;
import com.nullers.restbookstore.rest.auth.dto.UserSignInRequest;
import com.nullers.restbookstore.rest.auth.dto.UserSignUpRequest;
import com.nullers.restbookstore.rest.auth.services.authentication.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de auth
 *
 * @Author Binwei Wang
 */

@RestController
@Slf4j
@RequestMapping("/api/auth")
public class AuthenticationController {
    /**
     * Servicio de autenticación
     */
    private final AuthenticationService authenticationService;

    /**
     * Constructor de AuthenticationController
     *
     * @param authenticationService Servicio de autenticación
     */
    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Método para registrar un usuario
     *
     * @param request Usuario a registrar
     * @return Respuesta de autenticación
     */
    @Operation(summary = "crea una cuenta", description = "crear cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta creada"),
            @ApiResponse(responseCode = "400", description = "Error al crear cuenta"),
            @ApiResponse(responseCode = "403", description = "Denegado"),
    })
    @PostMapping("/signup")
    public ResponseEntity<JwtAuthResponse> singUp(@Valid @RequestBody UserSignUpRequest request) {
        log.info("Registrando usuario: {}", request);
        return ResponseEntity.ok(authenticationService.signUp(request));
    }

    /**
     * Método para iniciar sesión
     *
     * @param request Usuario a iniciar sesión
     * @return Respuesta de autenticación
     */
    @Operation(summary = "inicia sesión", description = "iniciar sesión")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inicio de sesión realizado"),
            @ApiResponse(responseCode = "400", description = "Error al iniciar sesión"),
            @ApiResponse(responseCode = "403", description = "Denegado"),
    })
    @PostMapping("/signin")
    public ResponseEntity<JwtAuthResponse> signIn(@Valid @RequestBody UserSignInRequest request) {
        log.info("Iniciando sesión de usuario: {}", request);
        return ResponseEntity.ok(authenticationService.signIn(request));
    }
}
