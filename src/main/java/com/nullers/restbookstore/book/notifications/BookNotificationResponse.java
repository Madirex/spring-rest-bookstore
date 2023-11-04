package com.nullers.restbookstore.book.notifications;

/**
 * FunkoNotificationResponse
 *
 * @Author Madirex
 */
public record BookNotificationResponse(
        String id,
        String name,
        String publisher,
        String image,
        String description,
        String createdAt,
        String updatedAt
) {
}