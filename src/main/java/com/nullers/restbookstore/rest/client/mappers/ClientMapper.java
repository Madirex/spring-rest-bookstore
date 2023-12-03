package com.nullers.restbookstore.rest.client.mappers;

import com.nullers.restbookstore.rest.client.dto.ClientDto;
import com.nullers.restbookstore.rest.client.model.Client;
import org.springframework.stereotype.Component;

/**
 * Mapea los datos de un cliente
 *
 * @author Daniel
 * @see ClientDto
 * @see Client
 */
@Component
public class ClientMapper {

    /**
     * Constructor privado para evitar instancias
     */
    private ClientMapper() {
        // Constructor privado para evitar instancias
    }

    /**
     * Mapea los datos de un dto a un cliente
     *
     * @param dto Cliente a mapear
     * @return Cliente mapeado
     */
    public static Client toEntity(ClientDto dto) {
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
     *
     * @param entity Cliente a mapear
     * @return ClientDto mapeado
     */
    public static ClientDto toDto(Client entity) {
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
