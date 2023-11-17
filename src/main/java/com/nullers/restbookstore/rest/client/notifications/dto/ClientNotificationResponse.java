package com.nullers.restbookstore.rest.client.notifications.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nullers.restbookstore.rest.book.model.Book;

import java.util.List;
import java.util.UUID;

public record ClientNotificationResponse(
        UUID id,
        String name,
        String surname,
        String email,
        String phone,
        String address,
        String image,
        @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
        List<Book> books
) {

}
