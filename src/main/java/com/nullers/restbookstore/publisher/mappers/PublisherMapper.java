package com.nullers.restbookstore.publisher.mappers;

import com.nullers.restbookstore.publisher.dto.CreatePublisherDto;
import com.nullers.restbookstore.publisher.dto.PublisherDto;
import com.nullers.restbookstore.publisher.models.Publisher;

import java.time.LocalDateTime;

/**
 * Clase PublisherMapper
 *
 * @author jaimesalcedo1
 * */
public class PublisherMapper {

    private PublisherMapper(){}

    /**
     * mapea un dto a objeto Publisher
     *
     * @param dto dto  a mapear
     * @return Publisher mapeado
     * */
    public static Publisher toPublisher(PublisherDto dto){
        return Publisher.builder()
                .name(dto.getName())
                .image(dto.getImage())
                .books(dto.getBooks())
                .build();
    }

    /**
     * mapea un Publisher a dto
     *
     * @param publisher publisher a mapear
     * @return PublisherDto mapeado
     * */
    public static PublisherDto toDto(Publisher publisher){
        return PublisherDto.builder()
                .id(publisher.getId())
                .name(publisher.getName())
                .image(publisher.getImage())
                .books(publisher.getBooks())
                .created_at(publisher.getCreated_at())
                .updated_at(publisher.getUpdated_at())
                .build();
    }
}
