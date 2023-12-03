package com.nullers.restbookstore.rest.auth.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthenticationDtoTest {
    @Test
    void jwtAuthResponseTest(){
        JwtAuthResponse jwtAuthResponse = JwtAuthResponse.builder().token("token").build();
        assert jwtAuthResponse.getToken().equals("token");
    }

    @Test
    void userSignInRequestTest(){
        UserSignInRequest userSignInRequest = UserSignInRequest.builder().username("username").password("password").build();
        assertAll(
                () -> assertEquals(userSignInRequest.getUsername(), "username"),
                () -> assertEquals(userSignInRequest.getPassword(), "password")
        );
    }

    @Test
    void userSignUpRequestTest(){
        UserSignUpRequest userSignUpRequest = UserSignUpRequest.builder().username("username").password("password").email("email").nombre("nombre").apellidos("apellidos").build();
        assertAll("userSignUpRequest",
                () -> assertEquals(userSignUpRequest.getUsername(), "username"),
                () -> assertEquals(userSignUpRequest.getPassword(), "password"),
                () -> assertEquals(userSignUpRequest.getEmail(), "email"),
                () -> assertEquals(userSignUpRequest.getNombre(), "nombre"),
                () -> assertEquals(userSignUpRequest.getApellidos(), "apellidos")
        );
    }

}
