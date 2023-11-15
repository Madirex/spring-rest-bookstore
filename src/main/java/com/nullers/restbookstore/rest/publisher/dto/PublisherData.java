package com.nullers.restbookstore.rest.publisher.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private Long id;
    private String name;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
