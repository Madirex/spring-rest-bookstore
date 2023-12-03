package com.nullers.restbookstore.rest.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nullers.restbookstore.rest.auth.dto.JwtAuthResponse;
import com.nullers.restbookstore.rest.auth.dto.UserSignInRequest;
import com.nullers.restbookstore.rest.auth.dto.UserSignUpRequest;
import com.nullers.restbookstore.rest.auth.exceptions.AuthSingInInvalid;
import com.nullers.restbookstore.rest.auth.exceptions.UserDiferentePasswords;
import com.nullers.restbookstore.rest.auth.services.authentication.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class AuthenticationsControllerTest {
    private final String endPoint = "/api/auth";
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;
    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    public AuthenticationsControllerTest( AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void singIn() throws Exception {
        // Arrange
        UserSignUpRequest userSignUpRequest = UserSignUpRequest.builder()
                .nombre("test")
                .apellidos("test")
                .username("test")
                .passwordComprobacion("tests")
                .email("test@test.com")
                .password("tests")
                .build();

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse("token");

        when(authenticationService.signUp(userSignUpRequest)).thenReturn(jwtAuthResponse);

        // Act
        MockHttpServletResponse response = mockMvc.perform(
                        post(endPoint + "/signup")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(mapper.writeValueAsString(userSignUpRequest)))
                .andReturn().getResponse();

        JwtAuthResponse res = mapper.readValue(response.getContentAsString(), JwtAuthResponse.class);

        // Assert
        assertAll("signup",
                ()-> assertEquals(200, response.getStatus()),
                ()-> assertEquals(jwtAuthResponse.getToken(), res.getToken())
                );
    }

    @Test
    void singUp_NotMatchPassword() throws Exception {
        // Arrange
        UserSignUpRequest userSignUpRequest = UserSignUpRequest.builder()
                .nombre("test")
                .apellidos("test")
                .username("test")
                .passwordComprobacion("testss")
                .email("test@test.com")
                .password("tests")
                .build();

        when(authenticationService.signUp(any(UserSignUpRequest.class))).thenThrow(new UserDiferentePasswords("Las contraseñas no coinciden"));

        // Act

        // Assert
        assertThrows(UserDiferentePasswords.class, ()-> authenticationService.signUp(userSignUpRequest));
    }

    @Test
    void singIn_UsernameOrEmailAlreadyExists() throws Exception {
        // Arrange
        UserSignUpRequest userSignUpRequest = UserSignUpRequest.builder()
                .nombre("test")
                .apellidos("test")
                .username("test")
                .passwordComprobacion("tests")
                .email("test@test.com")
                .password("tests")
                .build();

        when(authenticationService.signUp(any(UserSignUpRequest.class))).thenThrow(new UserDiferentePasswords("Las contraseñas no coinciden"));

        // Act

        // Assert
        assertThrows(UserDiferentePasswords.class, () -> authenticationService.signUp(userSignUpRequest));
    }

    @Test
    void singUp_BadRequest() throws Exception {
        // Arrange
        UserSignUpRequest userSignUpRequest = UserSignUpRequest.builder()
                .nombre("")
                .apellidos("")
                .username("")
                .passwordComprobacion("tests")
                .email("")
                .password("tests")
                .build();

        // Act
        MockHttpServletResponse response = mockMvc.perform(
                        post(endPoint + "/signup")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(mapper.writeValueAsString(userSignUpRequest)))
                .andReturn().getResponse();

        // Assert
        assertAll("signup",
                () -> assertEquals(400, response.getStatus())
        );
    }

    @Test
    void signIn() throws Exception {
        // Arrange
        UserSignUpRequest userSignUpRequest = UserSignUpRequest.builder()
                .nombre("test")
                .apellidos("test")
                .username("test")
                .passwordComprobacion("tests")
                .email("test@test.com")
                .password("tests")
                .build();

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse("token");

        when(authenticationService.signIn(any(UserSignInRequest.class))).thenReturn(jwtAuthResponse);

        // Act
        MockHttpServletResponse response = mockMvc.perform(
                        post(endPoint + "/signin")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(mapper.writeValueAsString(userSignUpRequest)))
                .andReturn().getResponse();

        JwtAuthResponse res = mapper.readValue(response.getContentAsString(), JwtAuthResponse.class);

        // Assert
        assertAll("signin",
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals("token", res.getToken())
        );
    }

    @Test
    void signIn_Invalid(){
        // Arrange
        UserSignInRequest userSignInRequest = UserSignInRequest.builder()
                .username("test")
                .password("tests")
                .build();

        when(authenticationService.signIn(any(UserSignInRequest.class))).thenThrow(new AuthSingInInvalid("Usuario o contraseña incorrectos"));

        // Act

        // Assert
        assertThrows(AuthSingInInvalid.class, ()-> authenticationService.signIn(userSignInRequest));
    }

    @Test
    void signIn_BadRequest() throws Exception {
        // Arrange
        UserSignInRequest userSignInRequest = UserSignInRequest.builder()
                .username("")
                .password("")
                .build();

        // Act
        MockHttpServletResponse response = mockMvc.perform(
                        post(endPoint + "/signin")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                // Le paso el body
                                .content(mapper.writeValueAsString(userSignInRequest)))
                .andReturn().getResponse();

        // Assert
        assertAll("signin",
                () -> assertEquals(400, response.getStatus())
        );
    }

}
