package com.nullers.restbookstore.publisher.mappers;

import com.nullers.restbookstore.publisher.dto.CreatePublisherDto;
import com.nullers.restbookstore.publisher.models.Publisher;

import java.time.LocalDateTime;

public class CreatePublisherMapper {

    private CreatePublisherMapper(){}
    public static Publisher toPublisher(CreatePublisherDto dto){
        return Publisher.builder()
                .name(dto.getName())
                .image(dto.getImage())
                .created_at(LocalDateTime.now())
                .updated_at(LocalDateTime.now())
                .build();
    }

    public static CreatePublisherDto toDto(Publisher publisher){
        return CreatePublisherDto.builder()
                .name(publisher.getName())
                .image(publisher.getImage())
                .build();
    }
}
