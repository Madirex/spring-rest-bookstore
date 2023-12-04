package com.nullers.restbookstore.rest.client.notifications.dto;

import com.nullers.restbookstore.rest.common.Address;

import java.util.UUID;

/**
 * Clase ClientNotificationResponse
 *
 * @param id      The identifier
 * @param name    The name
 * @param surname The surname
 * @param email   The email
 * @param phone   The phone
 * @param address The address
 * @param image   The image
 */
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