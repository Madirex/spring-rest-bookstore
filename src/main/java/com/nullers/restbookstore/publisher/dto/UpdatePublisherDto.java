package com.nullers.restbookstore.publisher.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdatePublisherDto {
    @NotBlank(message = "el nombre no puede estar vacío")
    private String name;
}
