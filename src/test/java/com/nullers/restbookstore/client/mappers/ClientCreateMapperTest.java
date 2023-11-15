package com.nullers.restbookstore.client.mappers;

import com.nullers.restbookstore.rest.client.dto.ClientCreateDto;
import com.nullers.restbookstore.rest.client.mappers.ClientCreateMapper;
import com.nullers.restbookstore.rest.client.models.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientCreateMapperTest {

    Client client = Client.builder()
            .name("Daniel")
            .surname("García")
            .email("daniel@gmail.com")
            .phone("123456789")
            .address("Calle Falsa 123")
            .image("https://via.placeholder.com/150")
            .build();

    ClientCreateDto clientCreateDto = ClientCreateDto.builder()
            .name("Daniel")
            .surname("García")
            .email("daniel@gmail.com")
            .phone("123456789")
            .address("Calle Falsa 123")
            .build();


    @Test
    void toEntity() {
        Client client = ClientCreateMapper.toEntity(clientCreateDto);

        assertAll(
                () -> assertEquals(this.client.getName(), client.getName()),
                () -> assertEquals(this.client.getSurname(), client.getSurname()),
                () -> assertEquals(this.client.getEmail(), client.getEmail()),
                () -> assertEquals(this.client.getPhone(), client.getPhone()),
                () -> assertEquals(this.client.getAddress(), client.getAddress()),
                () -> assertEquals(this.client.getImage(), client.getImage())
        );
    }

    @Test
    void toDto() {
        ClientCreateDto clientCreateDto = ClientCreateMapper.toDto(client);

        assertAll(
                () -> assertEquals(this.clientCreateDto.getName(), clientCreateDto.getName()),
                () -> assertEquals(this.clientCreateDto.getSurname(), clientCreateDto.getSurname()),
                () -> assertEquals(this.clientCreateDto.getEmail(), clientCreateDto.getEmail()),
                () -> assertEquals(this.clientCreateDto.getPhone(), clientCreateDto.getPhone()),
                () -> assertEquals(this.clientCreateDto.getAddress(), clientCreateDto.getAddress())
        );
    }

}
