package com.nullers.restbookstore.rest.publisher.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nullers.restbookstore.rest.book.model.Book;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublisherDTO {
    private Long id;
    private String name;
    private String image;
    private Set<Book> books;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean active;
}
