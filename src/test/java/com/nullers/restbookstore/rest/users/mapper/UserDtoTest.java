package com.nullers.restbookstore.rest.users.mapper;

import com.nullers.restbookstore.rest.user.dto.UserInfoResponse;
import com.nullers.restbookstore.rest.user.dto.UserRequest;
import com.nullers.restbookstore.rest.user.dto.UserResponse;
import com.nullers.restbookstore.rest.user.mapper.UserMapper;
import com.nullers.restbookstore.rest.user.models.Role;
import com.nullers.restbookstore.rest.user.models.User;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDtoTest {
    private final UserMapper userMapper = new UserMapper();
    private final UserRequest userRequest = UserRequest.builder()
            .name("userRequest")
            .surnames("test")
            .password("test")
            .username("test")
            .email("test@test.com")
            .isDeleted(false)
            .roles(Set.of(Role.USER))
            .build();

    private final User user = User.builder()
            .name("user")
            .surnames("test")
            .password("test")
            .username("test")
            .email("test@user.com")
            .isDeleted(false)
            .roles(Set.of(Role.USER))
            .build();
    private final UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    @Test
    void toUserTest() {
        User user = userMapper.toUser(userRequest);
        assertAll("ToUser",
                ()-> assertEquals(user.getName(), userRequest.getName()),
                ()-> assertEquals(user.getSurnames(), userRequest.getSurnames()),
                ()-> assertEquals(user.getPassword(), userRequest.getPassword()),
                ()-> assertEquals(user.getUsername(), userRequest.getUsername()),
                ()-> assertEquals(user.getEmail(), userRequest.getEmail()),
                ()-> assertEquals(user.getRoles(), userRequest.getRoles()),
                ()-> assertEquals(user.getIsDeleted(), userRequest.getIsDeleted())
        );
    }

    @Test
    void toUserWithIdTest() {
        User userToUser = userMapper.toUser(userRequest, uuid);

        assertAll("ToUser con id",
                ()-> assertEquals(userToUser.getId(), uuid),
                ()-> assertEquals(userToUser.getName(), userRequest.getName()),
                ()-> assertEquals(userToUser.getSurnames(), userRequest.getSurnames()),
                ()-> assertEquals(userToUser.getPassword(), userRequest.getPassword()),
                ()-> assertEquals(userToUser.getUsername(), userRequest.getUsername()),
                ()-> assertEquals(userToUser.getEmail(), userRequest.getEmail()),
                ()-> assertEquals(userToUser.getRoles(), userRequest.getRoles()),
                ()-> assertEquals(userToUser.getIsDeleted(), userRequest.getIsDeleted())

                );
    }

    @Test
    void toUserResponse () {
        UserResponse userResponse = userMapper.toUserResponse(user);

        assertAll("ToUserResponse",
                () -> assertEquals(userResponse.getId(), user.getId()),
                () -> assertEquals(userResponse.getName(), user.getName()),
                () -> assertEquals(userResponse.getSurnames(), user.getSurnames()),
                () -> assertEquals(userResponse.getUsername(), user.getUsername()),
                () -> assertEquals(userResponse.getEmail(), user.getEmail()),
                () -> assertEquals(userResponse.getRoles(), user.getRoles()),
                () -> assertEquals(userResponse.getIsDeleted(), user.getIsDeleted())
        );
    }

    @Test
    void toUserInfoResponse(){
        UserInfoResponse userInfoResponse = userMapper.toUserInfoResponse(user);

        assertAll("ToUserInfoResponse",
                () -> assertEquals(userInfoResponse.getId(), user.getId()),
                () -> assertEquals(userInfoResponse.getName(), user.getName()),
                () -> assertEquals(userInfoResponse.getSurnames(), user.getSurnames()),
                () -> assertEquals(userInfoResponse.getUsername(), user.getUsername()),
                () -> assertEquals(userInfoResponse.getEmail(), user.getEmail()),
                () -> assertEquals(userInfoResponse.getRoles(), user.getRoles()),
                () -> assertEquals(userInfoResponse.getIsDeleted(), user.getIsDeleted()
                )
        );
    }
}
