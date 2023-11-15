package com.nullers.restbookstore.client.notifications.mapper;

import com.nullers.restbookstore.NOADD.models.Book;
import com.nullers.restbookstore.rest.client.models.Client;
import com.nullers.restbookstore.rest.client.notifications.dto.ClientNotificationResponse;
import com.nullers.restbookstore.rest.client.notifications.mapper.ClientNotificationMapper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClientNotificationMapperTest {

    private final ClientNotificationMapper clientNotificationMapper = new ClientNotificationMapper();

    private final ClientNotificationResponse clientNotificationResponse = new ClientNotificationResponse(
            UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"),
            "Daniel",
            "Garcia",
            "daniel@gmail.com",
            "123456789",
            "Calle Falsa 123",
            "https://via.placeholder.com/150",
            List.of(Book.builder().id(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5e2")).name("hobbit").description("prueba desc").build())
    );

    private final Client clientTest = Client.builder()
            .id(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"))
            .name("Daniel")
            .surname("Garcia")
            .email("daniel@gmail.com")
            .phone("123456789")
            .address("Calle Falsa 123")
            .image("https://via.placeholder.com/150")
            .books(List.of(Book.builder().id(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5e2")).name("hobbit").description("prueba desc").build()))
            .build();

    @Test
    void toClientNotificationResponse(){
        var clientNot = clientNotificationMapper.toClientNotificationResponse(clientTest);

        assertAll(
                () -> assertEquals(clientNotificationResponse.id(), clientNot.id()),
                () -> assertEquals(clientNotificationResponse.name(), clientNot.name()),
                () -> assertEquals(clientNotificationResponse.surname(), clientNot.surname()),
                () -> assertEquals(clientNotificationResponse.email(), clientNot.email()),
                () -> assertEquals(clientNotificationResponse.phone(), clientNot.phone()),
                () -> assertEquals(clientNotificationResponse.address(), clientNot.address()),
                () -> assertEquals(clientNotificationResponse.image(), clientNot.image()),
                () -> assertEquals(clientNotificationResponse.books(), clientNot.books()),
                () -> assertEquals(clientNotificationResponse.toString(), clientNot.toString())
        );
    }



}
