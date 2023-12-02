package com.nullers.restbookstore.rest.client.notifications.mapper;

import com.nullers.restbookstore.rest.category.model.Categoria;
import com.nullers.restbookstore.rest.common.Address;
import com.nullers.restbookstore.rest.client.model.Client;
import com.nullers.restbookstore.rest.client.notifications.dto.ClientNotificationResponse;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClientNotificationMapperTest {

    private final ClientNotificationMapper clientNotificationMapper = new ClientNotificationMapper();

    Address address = Address.builder()
            .street("Calle Falsa 123")
            .city("Springfield")
            .country("USA")
            .province("Springfield")
            .number("123")
            .PostalCode("12345")
            .build();
    private Categoria categoria = Categoria.builder()
            .id(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5c8"))
            .build();

    private final ClientNotificationResponse clientNotificationResponse = new ClientNotificationResponse(
            UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"),
            "Daniel",
            "Garcia",
            "daniel@gmail.com",
            "123456789",
            address,
            "https://via.placeholder.com/150"
    );



    private final Client clientTest = Client.builder()
            .id(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"))
            .name("Daniel")
            .surname("Garcia")
            .email("daniel@gmail.com")
            .phone("123456789")
            .address(address)
            .image("https://via.placeholder.com/150")
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
                () -> assertEquals(clientNotificationResponse.image(), clientNot.image())
        );
    }



}
