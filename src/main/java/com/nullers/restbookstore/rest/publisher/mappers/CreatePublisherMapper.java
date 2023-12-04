package com.nullers.restbookstore.rest.publisher.mappers;

import com.nullers.restbookstore.rest.publisher.dto.CreatePublisherDto;
import com.nullers.restbookstore.rest.publisher.model.Publisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Clase CreatePublisherMapper
 */
@Component
public class CreatePublisherMapper {

    /**
     * Clase CreatePublisherMapper
     *
     * @author jaimesalcedo1
     */
    private CreatePublisherMapper() {
    }

    /**
     * mapea un dto a objeto Publisher
     *
     * @param dto dto con los datos de CreatePublisherDto a mapear
     * @return Publisher mapeado
     */
    public Publisher toPublisher(CreatePublisherDto dto) {
        return Publisher.builder()
                .name(dto.getName())
                .image(dto.getImage())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * mapea un Publisher a dto
     *
     * @param publisher publisher a mapear
     * @return CreatePublisherDto mapeado
     */
    public CreatePublisherDto toDto(Publisher publisher) {
        return CreatePublisherDto.builder()
                .name(publisher.getName())
                .image(publisher.getImage())
                .build();
    }

    /**
     * mapea un Publisher a DTO
     *
     * @param publisher publisher a mapear
     * @param image     imagen a mapear
     * @return CreatePublisherDto mapeado
     */
    public CreatePublisherDto toDtoOnlyImage(Publisher publisher, String image) {
        return CreatePublisherDto.builder()
                .name(publisher.getName())
                .image(image)
                .build();
    }
}
