package com.nullers.restbookstore.client.mappers;

import com.nullers.restbookstore.client.dto.ClientDto;
import com.nullers.restbookstore.client.models.Client;

public class ClientMapper {

    public static Client toEntity(ClientDto dto){
        return Client.builder()
                .id(dto.getId())
                .name(dto.getName())
                .surname(dto.getSurname())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .qrImage(dto.getQrImage())
                .build();
    }

    public static ClientDto toDto(Client entity){
        return ClientDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .surname(entity.getSurname())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .address(entity.getAddress())
                .qrImage(entity.getQrImage())
                .build();
    }

}
