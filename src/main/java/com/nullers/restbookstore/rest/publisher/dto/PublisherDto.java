package com.nullers.restbookstore.rest.publisher.dto;

import com.nullers.restbookstore.rest.book.models.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Clase PublisherDto
 *
 * @author jaimesalcedo1
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublisherDTO {
    private Long id;
    private String name;
    private String image;
    private Set<Book> books;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
