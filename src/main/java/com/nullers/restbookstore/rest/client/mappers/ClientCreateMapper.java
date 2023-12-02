package com.nullers.restbookstore.rest.client.mappers;

import com.nullers.restbookstore.rest.client.dto.ClientCreateDto;
import com.nullers.restbookstore.rest.client.model.Client;

/**
 * Mapea los datos de un cliente a un dto y viceversa
 * @see Client
 * @see ClientCreateDto
 * @author daniel
 */
public class ClientCreateMapper {

    /**
     * Mapea los datos de un dto a una entidad
     * @param dto dto con los datos del ClientCreateDto
     * @return Client con los datos del dto
     */
    public static Client toEntity(ClientCreateDto dto){
        return Client.builder()
                .name(dto.getName())
                .surname(dto.getSurname())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .build();
    }

    /**
     * Mapea los datos de una entidad a un dto
     * @param entity entidad con los datos del Client
     * @return ClientCreateDto con los datos de la entidad
     */
    public static ClientCreateDto toDto(Client entity){
        return ClientCreateDto.builder()
                .name(entity.getName())
                .surname(entity.getSurname())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .build();
    }

}
