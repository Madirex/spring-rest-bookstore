package com.nullers.restbookstore.rest.publisher.mappers;

import com.nullers.restbookstore.rest.publisher.dto.CreatePublisherDto;
import com.nullers.restbookstore.rest.publisher.dto.PublisherDTO;
import com.nullers.restbookstore.rest.publisher.dto.PublisherData;
import com.nullers.restbookstore.rest.publisher.model.Publisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Clase PublisherMapper
 *
 * @author jaimesalcedo1
 */
@Component
public class PublisherMapper {

    /**
     * Clase PublisherMapper
     */
    private PublisherMapper() {
    }

    /**
     * mapea un dto a objeto Publisher
     *
     * @param dto dto  a mapear
     * @return Publisher mapeado
     */
    public Publisher toPublisher(PublisherDTO dto) {
        return Publisher.builder()
                .id(dto.getId())
                .name(dto.getName())
                .image(dto.getImage())
                .books(dto.getBooks())
                .active(true)
                .build();
    }

    /**
     * mapea un Publisher a DTO
     *
     * @param publisher publisher a mapear
     * @return PublisherDto mapeado
     */
    public PublisherDTO toDto(Publisher publisher) {
        return PublisherDTO.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .image(publisher.getImage())
                .books(publisher.getBooks())
                .createdAt(publisher.getCreatedAt())
                .updatedAt(publisher.getUpdatedAt())
                .active(publisher.getActive())
                .build();
    }

    /**
     * mapea un Publisher a DTO
     *
     * @param publisher       publisher a mapear
     * @param publisherUpdate publisher a mapear
     * @return PublisherDto mapeado
     */
    public Publisher toPublisherModification(CreatePublisherDto publisher, PublisherDTO publisherUpdate) {
        var updatedPublisher = new Publisher();
        updatedPublisher.setId(publisherUpdate.getId());
        updatedPublisher.setName(publisher.getName());
        updatedPublisher.setImage(publisher.getImage());
        updatedPublisher.setCreatedAt(publisherUpdate.getCreatedAt());
        updatedPublisher.setUpdatedAt(LocalDateTime.now());
        updatedPublisher.setBooks(null);
        return updatedPublisher;
    }

    /**
     * mapea un Publisher a DTO
     *
     * @param publisher publisher a mapear
     * @return PublisherDto mapeado
     */
    public PublisherData toPublisherData(Publisher publisher) {
        return PublisherData.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .image(publisher.getImage())
                .createdAt(publisher.getCreatedAt())
                .updatedAt(publisher.getUpdatedAt())
                .build();
    }
}
