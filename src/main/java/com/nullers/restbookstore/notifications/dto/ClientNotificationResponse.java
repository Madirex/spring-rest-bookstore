package com.nullers.restbookstore.notifications.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nullers.restbookstore.NOADD.models.Book;

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
    @Override
    public String toString() {
        return "ClientNotificationResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", image='" + image + '\'' +
                ", books=" + books +
                '}';
    }
}
