package com.nullers.restbookstore.rest.publisher.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(name = "Nombre", example = "Planeta")
    private String name;
    @NotBlank(message = "la imagen no puede estar vacía")
    @Schema(name = "Imagen", example = "https://proassetspdlcom.cdnstatics2.com/usuaris/editorial/logo/d8253153-6647-454f-884e-b923429307f3-planeta.svg")
    private String image;
    @Builder.Default
    @Schema(name = "Editorial activa", example = "true")
    private Boolean active = true;
}
