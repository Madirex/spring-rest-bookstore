package com.nullers.restbookstore.rest.publisher.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Clase PublisherDto
 *
 * @author jaimesalcedo1
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublisherData {
    @Schema(name = "ID", example = "1")
    private Long id;
    @Schema(name = "Nombre", example = "Planeta")
    private String name;
    @Schema(name = "Imagen", example = "https://proassetspdlcom.cdnstatics2.com/usuaris/editorial/logo/d8253153-6647-454f-884e-b923429307f3-planeta.svg")
    private String image;
    @Schema(name = "Fecha de creación", example = "2021-03-05T11:11:11")
    private LocalDateTime createdAt;
    @Schema(name = "Fecha de actualización", example = "2021-03-05T11:11:11")
    private LocalDateTime updatedAt;
}
