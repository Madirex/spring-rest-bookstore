package com.nullers.restbookstore.rest.book.mappers;

import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.notifications.BookNotificationResponse;
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
                book.getId(),
                book.getName(),
                book.getPublisher().toString(),
                book.getImage(),
                book.getDescription(),
                book.getCreatedAt().toString(),
                book.getUpdatedAt().toString(),
                book.getActive(),
                book.getCategory()
        );
    }
}