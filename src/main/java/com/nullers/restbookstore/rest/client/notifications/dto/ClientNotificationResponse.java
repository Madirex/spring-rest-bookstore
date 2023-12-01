package com.nullers.restbookstore.rest.client.notifications.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nullers.restbookstore.rest.book.model.Book;
import com.nullers.restbookstore.rest.client.model.Address;

import java.util.List;
import java.util.UUID;

public record ClientNotificationResponse(
        UUID id,
        String name,
        String surname,
        String email,
        String phone,
        Address address,
        String image
) {

}
