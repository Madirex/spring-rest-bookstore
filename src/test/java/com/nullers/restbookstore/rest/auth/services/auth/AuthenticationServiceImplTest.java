package com.nullers.restbookstore.rest.auth.services.auth;



import com.nullers.restbookstore.rest.auth.dto.JwtAuthResponse;
import com.nullers.restbookstore.rest.auth.dto.UserSignInRequest;
import com.nullers.restbookstore.rest.auth.dto.UserSignUpRequest;
import com.nullers.restbookstore.rest.auth.exceptions.AuthSingInInvalid;
import com.nullers.restbookstore.rest.auth.exceptions.UserAuthNameOrEmailExisten;
import com.nullers.restbookstore.rest.auth.exceptions.UserDiferentePasswords;
import com.nullers.restbookstore.rest.auth.repositories.AuthUsersRepository;
import com.nullers.restbookstore.rest.auth.services.authentication.AuthenticationServiceImp;
import com.nullers.restbookstore.rest.auth.services.jwt.JwtService;
import com.nullers.restbookstore.rest.user.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    @Mock
    private AuthUsersRepository authUsersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImp authenticationService;

    @Test
    void signUpTest() {
        // Datos de prueba
        UserSignUpRequest request = new UserSignUpRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setPasswordComprobacion("password");
        request.setEmail("test@example.com");
        request.setNombre("Test");
        request.setApellidos("User");

        // Mock del repositorio de usuarios
        User userStored = new User();
        when(authUsersRepository.save(any(User.class))).thenReturn(userStored);

        // Mock del servicio JWT
        String token = "test_token";
        when(jwtService.generateToken(userStored)).thenReturn(token);

        // Llamada al método a probar
        JwtAuthResponse response = authenticationService.signUp(request);

        // Verificaciones
        assertAll("Sign Up",
                () -> assertNotNull(response),
                () -> assertEquals(token, response.getToken()),
                () -> verify(authUsersRepository, times(1)).save(any(User.class)),
                () -> verify(jwtService, times(1)).generateToken(userStored)
        );
    }

    @Test
    void signUp_NotMatch() {
        // Datos de prueba
        UserSignUpRequest request = new UserSignUpRequest();
        request.setUsername("testuser");
        request.setPassword("password1");
        request.setPasswordComprobacion("password2");
        request.setEmail("test@example.com");
        request.setNombre("Test");
        request.setApellidos("User");

        // Llamada al método a probar y verificación de excepción
        assertThrows(UserDiferentePasswords.class, () -> authenticationService.signUp(request));
    }

    @Test
    void signUp_UsernameOrEmailReadyExist() {
        // Datos de prueba
        UserSignUpRequest request = new UserSignUpRequest();
        request.setUsername("testuser");
        request.setPassword("password");
        request.setPasswordComprobacion("password");
        request.setEmail("test@example.com");
        request.setNombre("Test");
        request.setApellidos("User");

        // Mock del repositorio de usuarios
        when(authUsersRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        // Llamada al método a probar y verificación de excepción
        assertThrows(UserAuthNameOrEmailExisten.class, () -> authenticationService.signUp(request));
    }

    @Test
    public void signIn() {
        // Datos de prueba
        UserSignInRequest request = new UserSignInRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        // Mock del repositorio de usuarios
        User user = new User();
        when(authUsersRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));

        // Mock del servicio JWT
        String token = "test_token";
        when(jwtService.generateToken(user)).thenReturn(token);

        // Llamada al método a probar
        JwtAuthResponse response = authenticationService.signIn(request);

        // Verificaciones
        assertAll("Sign In",
                () -> assertNotNull(response),
                () -> assertEquals(token, response.getToken()),
                () -> verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class)),
                () -> verify(authUsersRepository, times(1)).findByUsername(request.getUsername()),
                () -> verify(jwtService, times(1)).generateToken(user)
        );
    }

    @Test
    public void signIn_Invalid() {
        // Datos de prueba
        UserSignInRequest request = new UserSignInRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        // Mock del repositorio de usuarios
        when(authUsersRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());

        // Llamada al método a probar y verificación de excepción
        assertThrows(AuthSingInInvalid.class, () -> authenticationService.signIn(request));
    }
}