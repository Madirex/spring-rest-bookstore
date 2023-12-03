package com.nullers.restbookstore.rest.client.mappers;

import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.mappers.BookMapperImpl;
import com.nullers.restbookstore.rest.book.model.Book;
import com.nullers.restbookstore.rest.client.dto.ClientDto;
import com.nullers.restbookstore.rest.client.model.Client;
import com.nullers.restbookstore.rest.publisher.mappers.PublisherMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
/**
 * Mapea los datos de un cliente
 * @author daniel
 * @see ClientDto
 * @see Client
 */
public class ClientMapper {

    private static BookMapperImpl bookMapper = new BookMapperImpl();
    private static PublisherMapper publisherMapper = new PublisherMapper();

    /**
     * Mapea los datos de un dto a un cliente
     * @param dto Cliente a mapear
     * @return Cliente mapeado
     */
    public static Client toEntity(ClientDto dto){
        return Client.builder()
                .id(dto.getId())
                .name(dto.getName())
                .surname(dto.getSurname())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .image(dto.getImage())
                .build();
    }

    /**
     * Mapea los datos de un cliente a un dto
     * @param entity Cliente a mapear
     * @return ClientDto mapeado
     */
    public static ClientDto toDto(Client entity){
        return ClientDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .surname(entity.getSurname())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .image(entity.getImage())
                .build();
    }

}
