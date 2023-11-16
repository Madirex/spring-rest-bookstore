package com.nullers.restbookstore.rest.publisher.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

/**
 * Clase CreatePublisherDto
 *
 * @author jaimesalcedo1
 */
@Getter
@Builder
public class CreatePublisherDto {
    @NotBlank(message = "el nombre no puede estar vacío")
    private String name;
    @NotBlank(message = "la imagen no puede estar vacía")
    private String image;
    @Builder.Default
    private Boolean active = true;
}
