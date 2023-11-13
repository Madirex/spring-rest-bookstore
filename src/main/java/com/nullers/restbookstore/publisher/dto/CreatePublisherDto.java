package com.nullers.restbookstore.publisher.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class CreatePublisherDto {
    @NotBlank(message = "el nombre no puede estar vacío")
    private String name;
}
