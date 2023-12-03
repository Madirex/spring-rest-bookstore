package com.nullers.restbookstore.rest.auth.services.users;

import com.nullers.restbookstore.rest.auth.repositories.AuthUsersRepository;
import com.nullers.restbookstore.rest.user.models.Role;
import com.nullers.restbookstore.rest.user.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthUsersServiceImpTest {

    @Mock
    private AuthUsersRepository authUsersRepository;

    @InjectMocks
    private AuthUsersServiceImp authUsersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserExists(){
        String username = "testuser";
        User user = User.builder()
                .username(username)
                .password("testpassword")
                .roles(Collections.singleton(Role.USER))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(authUsersRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails result = authUsersService.loadUserByUsername(username);

        assertNotNull(result, "El resultado no debería ser nulo");
        assertEquals(username, result.getUsername(), "El nombre de usuario debe coincidir");
    }

    @Test
    void loadUserByUsername_UserDoesNotExist() {
        String username = "unknownuser";
        when(authUsersRepository.findByUsername(username)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authUsersService.loadUserByUsername(username);
        });

        String expectedMessage = "Usuario con username " + username + " no encontrado";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }
}
