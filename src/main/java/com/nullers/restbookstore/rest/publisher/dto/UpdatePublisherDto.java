package com.nullers.restbookstore.rest.publisher.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Clase UpdatePublisherDto
 *
 * @author jaimesalcedo1
 * */
public class UpdatePublisherDto {
    @NotBlank(message = "el nombre no puede estar vacío")
    @Schema(name = "Nombre", example = "Planeta")
    private String name;
    @NotBlank(message = "la imagen no puede estar vacía")
    @Schema(name = "Imagen", example = "https://proassetspdlcom.cdnstatics2.com/usuaris/editorial/logo/d8253153-6647-454f-884e-b923429307f3-planeta.svg")
    private String image;
}
