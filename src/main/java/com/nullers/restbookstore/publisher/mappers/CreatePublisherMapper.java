package com.nullers.restbookstore.publisher.mappers;

import com.nullers.restbookstore.publisher.dto.CreatePublisherDto;
import com.nullers.restbookstore.publisher.models.Publisher;

import java.time.LocalDateTime;

public class CreatePublisherMapper {

    /**
     * Clase CreatePublisherMapper
     *
     * @author jaimesalcedo1
     * */
    private CreatePublisherMapper(){}

    /**
     * mapea un dto a objeto Publisher
     *
     * @param dto dto con los datos de CreatePublisherDto a mapear
     * @return Publisher mapeado
     * */
    public static Publisher toPublisher(CreatePublisherDto dto){
        return Publisher.builder()
                .name(dto.getName())
                .image(dto.getImage())
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
    }

    /**
     * mapea un Publisher a dto
     *
     * @param publisher publisher a mapear
     * @return CreatePublisherDto mapeado
     * */
    public static CreatePublisherDto toDto(Publisher publisher){
        return CreatePublisherDto.builder()
                .name(publisher.getName())
                .image(publisher.getImage())
                .build();
    }
}
