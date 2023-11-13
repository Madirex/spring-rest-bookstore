package com.nullers.restbookstore.publisher.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Clase UpdatePublisherDto
 *
 * @author jaimesalcedo1
 * */
public class UpdatePublisherDto {
    @NotBlank(message = "el nombre no puede estar vacío")
    private String name;
    @NotBlank(message = "la imagen no puede estar vacía")
    private String image;
}
