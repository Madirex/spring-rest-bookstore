package com.nullers.restbookstore.book.mappers;

import com.nullers.restbookstore.book.dto.GetBookDTO;
import com.nullers.restbookstore.book.notifications.BookNotificationResponse;
import org.springframework.stereotype.Component;

/**
 * BookNotificationMapper
 *
 * @Author Madirex
 */
@Component
public class BookNotificationMapper {
    /**
     * Método que convierte un GetBookDTO a un BookNotificationResponse
     *
     * @param book GetBookDTO
     * @return BookNotificationResponse
     */
    public BookNotificationResponse toBookNotificationDto(GetBookDTO book) {
        return new BookNotificationResponse(
                book.getId().toString(),
                book.getName(),
                book.getPublisher().toString(),
                book.getImage(),
                book.getDescription(),
                book.getCreatedAt().toString(),
                book.getUpdatedAt().toString()
        );
    }
}