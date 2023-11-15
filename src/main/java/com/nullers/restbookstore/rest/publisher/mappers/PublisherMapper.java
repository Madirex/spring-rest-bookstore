package com.nullers.restbookstore.rest.publisher.mappers;

import com.nullers.restbookstore.rest.publisher.dto.PublisherDTO;
import com.nullers.restbookstore.rest.publisher.models.Publisher;
import org.springframework.stereotype.Component;

/**
 * Clase PublisherMapper
 *
 * @author jaimesalcedo1
 * */
@Component
public class PublisherMapper {

    private PublisherMapper(){}

    /**
     * mapea un dto a objeto Publisher
     *
     * @param dto dto  a mapear
     * @return Publisher mapeado
     * */
    public Publisher toPublisher(PublisherDTO dto){
        return Publisher.builder()
                .name(dto.getName())
                .image(dto.getImage())
                .books(dto.getBooks())
                .build();
    }

    /**
     * mapea un Publisher a DTO
     *
     * @param publisher publisher a mapear
     * @return PublisherDto mapeado
     * */
    public PublisherDTO toDto(Publisher publisher){
        return PublisherDTO.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .image(publisher.getImage())
                .books(publisher.getBooks())
                .createdAt(publisher.getCreatedAt())
                .updatedAt(publisher.getUpdatedAt())
                .build();
    }
}
