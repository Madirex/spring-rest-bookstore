package com.nullers.restbookstore.client.mappers;

import com.nullers.restbookstore.client.dto.ClientCreateDto;
import com.nullers.restbookstore.client.models.Client;

public class ClientCreateMapper {

    public static Client toEntity(ClientCreateDto dto){
        return Client.builder()
                .name(dto.getName())
                .surname(dto.getSurname())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .build();
    }

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
