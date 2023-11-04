package com.nullers.restbookstore.book.dto;

import com.nullers.restbookstore.NOADD.Publisher;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Class GetBookDTO
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetBookDTO {
    private UUID id;
    private String name;
    private Publisher publisher;

    @Setter
    private String image;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}