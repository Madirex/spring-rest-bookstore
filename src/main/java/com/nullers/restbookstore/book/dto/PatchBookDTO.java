package com.nullers.restbookstore.book.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * Class PatchBookDTO
 *
 * @Author Madirex
 */
@Getter
@Builder
public class PatchBookDTO {
    private String name;

    private String publisherId;

    private String image;

    private String description;
}