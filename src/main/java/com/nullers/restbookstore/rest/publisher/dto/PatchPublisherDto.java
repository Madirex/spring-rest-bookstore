package com.nullers.restbookstore.rest.publisher.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PatchPublisherDto {

    @Schema(name = "Nombre", example = "Planeta")
    private String name;
    @Schema(name = "Imagen", example = "https://proassetspdlcom.cdnstatics2.com/usuaris/editorial/logo/d8253153-6647-454f-884e-b923429307f3-planeta.svg")
    private String image;
    @Schema(name = "Editorial activa", example = "true")
    private Boolean active;
}
