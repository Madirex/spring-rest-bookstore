package com.nullers.restbookstore.rest.auth.services.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;


import static org.junit.jupiter.api.Assertions.*;

class JwtServiceImpTest {

    private JwtServiceImp jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImp();
        // Aquí debes configurar los valores de jwtSecretKey y jwtExpiration
        jwtService.setJwtSecretKey("tuClaveSecreta");
        jwtService.setJwtExpiration(3600000L); // Por ejemplo, una hora
    }

    @Test
    void testGenerateToken() {
        UserDetails userDetails = User.withUsername("testuser").password("password").roles("USER").build();
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token, "El token no debería ser nulo");
    }

    @Test
    void testIsTokenValid() {
        UserDetails userDetails = User.withUsername("testuser").password("password").roles("USER").build();
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails), "El token debería ser válido");
    }

    @Test
    void testExtractUserName() {
        UserDetails userDetails = User.withUsername("testuser").password("password").roles("USER").build();
        String token = jwtService.generateToken(userDetails);
        assertEquals("testuser", jwtService.extractUserName(token), "El nombre de usuario extraído debería coincidir");
    }

}
