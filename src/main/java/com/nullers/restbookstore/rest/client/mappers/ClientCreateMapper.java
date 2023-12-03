package com.nullers.restbookstore.rest.client.mappers;

import com.nullers.restbookstore.rest.client.dto.ClientCreateDto;
import com.nullers.restbookstore.rest.client.model.Client;

/**
 * Mapea los datos de un cliente a un DTO y viceversa
 *
 * @author Daniel
 * @see Client
 * @see ClientCreateDto
 */
public class ClientCreateMapper {

    /**
     * Constructor privado para evitar instancias
     */
    private ClientCreateMapper() {
        // Constructor privado para evitar instancias
    }

    /**
     * Mapea los datos de un DTO a una entidad
     *
     * @param dto DTO con los datos del ClientCreateDto
     * @return Client con los datos del DTO
     */
    public static Client toEntity(ClientCreateDto dto) {
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
     *
     * @param entity entidad con los datos del Client
     * @return ClientCreateDto con los datos de la entidad
     */
    public static ClientCreateDto toDto(Client entity) {
        return ClientCreateDto.builder()
                .name(entity.getName())
                .surname(entity.getSurname())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .build();
    }

}
