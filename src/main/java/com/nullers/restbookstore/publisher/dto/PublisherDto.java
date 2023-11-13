package com.nullers.restbookstore.publisher.dto;

import com.nullers.restbookstore.publisher.models.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublisherDto {

    private UUID id;
    private String name;
    private String image;
    private Set<Book> books;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
