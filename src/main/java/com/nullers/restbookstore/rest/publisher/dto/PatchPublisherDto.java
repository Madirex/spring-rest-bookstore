package com.nullers.restbookstore.rest.publisher.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PatchPublisherDto {

    private String name;
    private String image;
    private Boolean active;
}
