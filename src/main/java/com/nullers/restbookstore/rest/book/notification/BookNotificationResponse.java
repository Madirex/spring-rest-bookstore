package com.nullers.restbookstore.rest.book.notification;

/**
 * BookNotificationResponse
 *
 * @Author Madirex
 */
public record BookNotificationResponse(
        Long id,
        String name,
        String publisher,
        String image,
        String description,
        String createdAt,
        String updatedAt,
        Double price,
        Boolean active,
        String category
) {
}