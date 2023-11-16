package com.nullers.restbookstore.rest.publisher.dto;

import com.nullers.restbookstore.rest.book.models.Book;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
public class PatchPublisherDto {

    private String name;
    private String image;
    private Boolean active;
}
