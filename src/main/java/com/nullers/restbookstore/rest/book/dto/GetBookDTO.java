package com.nullers.restbookstore.rest.book.dto;

import com.nullers.restbookstore.rest.publisher.dto.PublisherData;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Class GetBookDTO
 *
 * @Author Madirex
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetBookDTO {
    private Long id;
    private String name;
    private PublisherData publisher;
    private String category;


    @Setter
    private String image;
    private String description;
    private Double price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean active;
}