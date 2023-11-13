package com.nullers.restbookstore.publisher.dto;

import com.nullers.restbookstore.publisher.models.Book;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

/**
 * Clase CreatePublisherDto
 *
 * @author jaimesalcedo1
 * */
@Getter
@Builder
public class CreatePublisherDto {
    @NotBlank(message = "el nombre no puede estar vacío")
    private String name;
    @NotBlank(message = "la imagen no puede estar vacía")
    private String image;
}
