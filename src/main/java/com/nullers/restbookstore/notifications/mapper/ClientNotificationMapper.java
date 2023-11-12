package com.nullers.restbookstore.notifications.mapper;

import com.nullers.restbookstore.client.models.Client;
import com.nullers.restbookstore.notifications.dto.ClientNotificationResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper para ClientNotificationResponse
 * @see ClientNotificationResponse
 * @see Client
 * @author daniel
 */
@Component
public class ClientNotificationMapper {

    /**
     * Mapea un objeto Client a un objeto ClientNotificationResponse
     * @param client Objeto Client
     * @return ClientNotificationResponse objeto
     */
    public ClientNotificationResponse toClientNotificationResponse(Client client) {
        return new ClientNotificationResponse(
                client.getId(),
                client.getName(),
                client.getSurname(),
                client.getEmail(),
                client.getPhone(),
                client.getAddress(),
                client.getImage(),
                client.getBooks()
        );
    }

}
