package com.nullers.restbookstore.rest.client.notifications.dto;

import com.nullers.restbookstore.rest.common.Address;

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
