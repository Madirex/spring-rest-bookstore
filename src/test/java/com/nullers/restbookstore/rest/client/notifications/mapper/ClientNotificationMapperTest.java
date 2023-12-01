package com.nullers.restbookstore.rest.client.notifications.mapper;

import com.nullers.restbookstore.rest.book.model.Book;
import com.nullers.restbookstore.rest.category.model.Categoria;
import com.nullers.restbookstore.rest.client.model.Client;
import com.nullers.restbookstore.rest.client.notifications.dto.ClientNotificationResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClientNotificationMapperTest {

    private final ClientNotificationMapper clientNotificationMapper = new ClientNotificationMapper();
    private Categoria categoria = Categoria.builder()
            .id(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5c8"))
            .build();

    private final ClientNotificationResponse clientNotificationResponse = new ClientNotificationResponse(
            UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"),
            "Daniel",
            "Garcia",
            "daniel@gmail.com",
            "123456789",
            "Calle Falsa 123",
            "https://via.placeholder.com/150",
            List.of(Book.builder().id(1L).name("hobbit").active(true).image("https://img.jpg").price(50.0).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).description("prueba desc").category(categoria).build())
    );



    private final Client clientTest = Client.builder()
            .id(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"))
            .name("Daniel")
            .surname("Garcia")
            .email("daniel@gmail.com")
            .phone("123456789")
            .address("Calle Falsa 123")
            .image("https://via.placeholder.com/150")
            .books(List.of(Book.builder().id(1L).name("hobbit").active(true).category(categoria).image("https://img.jpg").price(50.0).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).description("prueba desc").build()))
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
                () -> assertEquals(clientNotificationResponse.books().get(0).getName(), clientNot.books().get(0).getName()),
                () -> assertEquals(clientNotificationResponse.books().get(0).getId(), clientNot.books().get(0).getId())
        );
    }



}
