package com.nullers.restbookstore.rest.client.mappers;

import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.mappers.BookMapperImpl;
import com.nullers.restbookstore.rest.book.models.Book;
import com.nullers.restbookstore.rest.client.dto.ClientDto;
import com.nullers.restbookstore.rest.client.models.Client;
import com.nullers.restbookstore.rest.publisher.dto.PublisherData;
import com.nullers.restbookstore.rest.publisher.mappers.PublisherMapper;
import org.springframework.beans.factory.annotation.Autowired;
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


    public static Client toEntity(ClientDto dto, List<Book> books){
        return Client.builder()
                .id(dto.getId())
                .name(dto.getName())
                .surname(dto.getSurname())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .books(books)
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
                .books(entity.getBooks().stream().map((b) -> {
                    var publisher = publisherMapper.toPublisherData(b.getPublisher());
                    return bookMapper.toGetBookDTO(b, publisher);
                }).toList())
                .build();
    }

    public static ClientDto toDto(Client entity, List<GetBookDTO> books){
        return ClientDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .surname(entity.getSurname())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .image(entity.getImage())
                .books(books)
                .build();
    }



}
