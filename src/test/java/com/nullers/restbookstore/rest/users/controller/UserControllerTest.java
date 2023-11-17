package com.nullers.restbookstore.rest.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nullers.restbookstore.rest.user.controller.UserController;
import com.nullers.restbookstore.rest.user.dto.UserInfoResponse;
import com.nullers.restbookstore.rest.user.dto.UserRequest;
import com.nullers.restbookstore.rest.user.dto.UserResponse;
import com.nullers.restbookstore.rest.user.models.User;
import com.nullers.restbookstore.rest.user.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test for {@link UserController}
 *
 * @Author: Binwei Wang
 */
@SpringBootTest(properties = "spring.config.name=application-test")
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private final UserRequest userRequest = UserRequest.builder()
            .name("test")
            .surnames("test")
            .username("test")
            .email("test@test.com")
            .password("tests")
            .build();

    private final User user = User.builder()
            .name("test")
            .surnames("test")
            .username("test")
            .email("test@test.com")
            .password("tests")
            .build();

    private final UserResponse userResponse = UserResponse.builder()
            .id(UUID.fromString("c671d981-bd6f-4e75-b7cc-fd3ca96582d5"))
            .name("test")
            .surnames("test")
            .username("test")
            .email("test@test.com")
            .build();

    private final UserInfoResponse userInfoResponse = UserInfoResponse.builder()
            .id(UUID.fromString("c671d981-bd6f-4e75-b7cc-fd3ca96582d5"))
            .name("test")
            .surnames("test")
            .username("test")
            .email("test@test.com")
            .build();

    private final String myEndpoint = "/api/users";

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    public UserControllerTest(UserService userService) {
        this.userService = userService;
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void findAll() throws Exception {
        // Arrange
        var listUser = List.of(userResponse, userResponse);
        Page<UserResponse> page = new PageImpl<>(listUser);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        when(userService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), pageable)).thenReturn(page);

        // Act
        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        // Assert

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertNotNull(response.getContentAsString())
        );
    }

    /**
     * Test para comprobar retorno de error cuando page tiene valor no válido
     *
     * @throws Exception excepción
     */
    @Test
    void getAll_ShouldReturnErrorResponse_withInvalidPageParam() throws Exception {
        mockMvc.perform(get(myEndpoint)
                        .param("page", "-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    /**
     * Test para comprobar retorno de error cuando size tiene valor no válido
     *
     * @throws Exception excepción
     */
    @Test
    void getAll_ShouldReturnErrorResponse_withInvalidSizeParam() throws Exception {
        mockMvc.perform(get(myEndpoint)
                        .param("size", "0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void findById() throws Exception {
        // Arrange
        when(userService.findById(UUID.fromString("c671d981-bd6f-4e75-b7cc-fd3ca96582d5"))).thenReturn(userInfoResponse);

        // Act
        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint + "/{id}", UUID.fromString("c671d981-bd6f-4e75-b7cc-fd3ca96582d5"))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertNotNull(response.getContentAsString())
        );
    }

    @Test
    void create() throws Exception {
        // Arrange
        when(userService.save(userRequest)).thenReturn(userResponse);

        // Act
        MockHttpServletResponse response = mockMvc.perform(
                        post(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        var res = mapper.readValue(response.getContentAsString(), UserResponse.class);

        // Assert

        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertNotNull(response.getContentAsString()),
                () -> assertEquals(userResponse, res)
        );
    }

    @Test
    void update() throws Exception {
        // Arrange
        when(userService.update(UUID.fromString("c671d981-bd6f-4e75-b7cc-fd3ca96582d5"), userRequest)).thenReturn(userResponse);

        // Act
        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint + "/{id}", UUID.fromString("c671d981-bd6f-4e75-b7cc-fd3ca96582d5"))
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(userRequest)))
                .andReturn().getResponse();

        var res = mapper.readValue(response.getContentAsString(), UserResponse.class);

        // Assert
        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(userResponse, res)
        );
    }

//    @Test
//    void patchUser() throws Exception {
//        // Arrange
//        when(userService.patch(UUID.fromString("c671d981-bd6f-4e75-b7cc-fd3ca96582d5"), userRequest)).thenReturn(userResponse);
//
//        // Act
//        MockHttpServletResponse response = mockMvc.perform(
//                        patch(myEndpoint + "/{id}", UUID.fromString("c671d981-bd6f-4e75-b7cc-fd3ca96582d5"))
//                                .accept(MediaType.APPLICATION_JSON)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(mapper.writeValueAsString(userRequest)))
//                .andReturn().getResponse();
//
//        var res = mapper.readValue(response.getContentAsString(), UserResponse.class);
//
//        // Assert
//        assertAll(
//                () -> assertEquals(200, response.getStatus()),
//                () -> assertEquals(userResponse, res)
//        );
//    }

    @Test
    void deleteUser() throws Exception {
        // Arrange
        doNothing().when(userService).deleteById(UUID.fromString("c671d981-bd6f-4e75-b7cc-fd3ca96582d5"));
        // Act
        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndpoint + "/{id}", UUID.fromString("c671d981-bd6f-4e75-b7cc-fd3ca96582d5"))
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertAll(
                () -> assertEquals(204, response.getStatus())
        );
    }

}
