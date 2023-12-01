package com.nullers.restbookstore.rest.auth.controllers;

import com.nullers.restbookstore.rest.auth.dto.JwtAuthResponse;
import com.nullers.restbookstore.rest.auth.dto.UserSignInRequest;
import com.nullers.restbookstore.rest.auth.dto.UserSignUpRequest;
import com.nullers.restbookstore.rest.auth.services.authentication.AuthenticationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador de auth
 *
 * @Author: Binwei Wang
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
     * @param authenticationService Servicio de autenticación
     */
    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Método para registrar un usuario
     * @param request Usuario a registrar
     * @return Respuesta de autenticación
     */
    @PostMapping("/signup")
    public ResponseEntity<JwtAuthResponse> singUp(@Valid @RequestBody UserSignUpRequest request){
        log.info("Registrando usuario: {}", request);
        return ResponseEntity.ok(authenticationService.signUp(request));
    }

    /**
     * Método para iniciar sesión
     * @param request Usuario a iniciar sesión
     * @return Respuesta de autenticación
     */
    @PostMapping("/signin")
    public ResponseEntity<JwtAuthResponse> signIn(@Valid @RequestBody UserSignInRequest request){
        log.info("Iniciando sesión de usuario: {}", request);
        return ResponseEntity.ok(authenticationService.signIn(request));
    }

    /**
     * Método para manejar excepciones de validación
     * @param ex Excepción a manejar
     * @return Mapa de errores
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
