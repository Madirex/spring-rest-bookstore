package com.nullers.restbookstore.rest.users.services;

import com.nullers.restbookstore.rest.user.dto.UserInfoResponse;
import com.nullers.restbookstore.rest.user.dto.UserRequest;
import com.nullers.restbookstore.rest.user.dto.UserResponse;
import com.nullers.restbookstore.rest.user.exceptions.UserNameOrEmailExists;
import com.nullers.restbookstore.rest.user.exceptions.UserNotFound;
import com.nullers.restbookstore.rest.user.mapper.UserMapper;
import com.nullers.restbookstore.rest.user.models.User;
import com.nullers.restbookstore.rest.user.repository.UserRepository;
import com.nullers.restbookstore.rest.user.services.UserService;
import com.nullers.restbookstore.rest.user.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


/**
 * Test for {@link UserService}
 *
 * @Author Binwei Wang
 */

@ExtendWith(MockitoExtension.class)
class UserServicesTest {
    private final UserRequest userRequest = UserRequest.builder()
            .name("test")
            .surnames("test")
            .password("test")
            .username("test")
            .email("test@test.com")
            .build();
    private final User user = User.builder().id(UUID.fromString("c671d981-bd6f-4e75-b7cc-fd3ca96582d5")).username("test").email("test@test.com").build();
    private final UserResponse userResponse = UserResponse.builder().username("test").email("test@test.com").build();
    private final UserInfoResponse userInfoResponse = UserInfoResponse.builder().username("test").email("test@test.com").build();

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findAll() {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        Page<User> page = new PageImpl<>(users);
        when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(userMapper.toUserResponse(any(User.class))).thenReturn(new UserResponse());

        // Act
        Page<UserResponse> result = userService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Pageable.unpaged());

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.getTotalElements())
        );
    }

    @Test
    void findById() {
        // Arrange
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(userMapper.toUserInfoResponse(any(User.class))).thenReturn(userInfoResponse);

        // Act
        UserInfoResponse result = userService.findById(UUID.fromString("c671d981-bd6f-4e75-b7cc-fd3ca96582d5"));

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("test", result.getUsername())
        );
    }

    @Test
    void findByIdNotFound() {
        // Arrange
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        assertThrows(UserNotFound.class, () -> userService.findById(UUID.fromString("c671d981-bd6f-4e75-b7cc-fd3ca96582d5")));

        // Assert
    }

    @Test
    void save() {
        // Arrange
        when(userRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.empty());
        when(userMapper.toUser(any(UserRequest.class))).thenReturn(user);
        when(userMapper.toUserResponse(any(User.class))).thenReturn(userResponse);
        when(userRepository.save(user)).thenReturn(user);

        // Act
        UserResponse result = userService.save(userRequest);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("test", result.getUsername())
        );
    }

    @Test
    void saveDuplicateUsernameOrEmail() {
        // Arrange
        when(userRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.of(user));

        // Act
        assertThrows(UserNameOrEmailExists.class, () -> userService.save(userRequest));

        // Assert
    }

    @Test
    void update() {
        // Arrange
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(userRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.empty());
        when(userMapper.toUser(any(UserRequest.class), any(UUID.class))).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

        // Act
        UserResponse result = userService.update(UUID.fromString("c671d981-bd6f-4e75-b7cc-fd3ca96582d5"), userRequest);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("test", result.getUsername())
        );
    }

    @Test
    void updateUsernameOrEmailExists() {
        // Arrange
        UUID id = UUID.fromString("c671d981-bd6f-4e75-b7cc-fd3ca96582d5");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(any(String.class), any(String.class))).thenReturn(Optional.of(user));

        // Act
        assertThrows(UserNameOrEmailExists.class, () -> userService.update(UUID.fromString("c671d981-bd6f-4e75-b7cc-fd3ca96582d5"), userRequest));

        // Assert
    }

    @Test
    void patchUser(){
            // Arrange
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));
        when(userMapper.toUser(any(UserRequest.class),any(UUID.class))).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

        // Act
        UserResponse result = userService.patch(UUID.fromString("c671d981-bd6f-4e75-b7cc-fd3ca96582d5"), userRequest);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("test", result.getUsername())
        ); }

    @Test
    void updateUserNotFound() {
        // Arrange
        UUID userId = UUID.fromString("c671d981-bd6f-4e75-b7cc-fd3ca96582d5");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        assertThrows(UserNotFound.class, () -> userService.update(userId, userRequest));

        // Assert
    }

    @Test
    void deleteById() {
        // Arrange
        UUID id = UUID.fromString("c671d981-bd6f-4e75-b7cc-fd3ca96582d5");
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(user));

        // Act
        userService.deleteById(id);

        // Assert

        // Verify
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void deleteByIdNotFound() {
        // Arrange
        UUID id = UUID.fromString("c671d981-bd6f-4e75-b7cc-fd3ca96582d5");
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act
        assertThrows(UserNotFound.class, () -> userService.deleteById(id));

        // Assert
    }
}
