package com.nullers.restbookstore.rest.client.mappers;

import com.nullers.restbookstore.rest.client.dto.ClientCreateDto;
import com.nullers.restbookstore.rest.common.Address;
import com.nullers.restbookstore.rest.client.model.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientCreateMapperTest {

    Address address = Address.builder()
            .street("Calle Falsa 123")
            .city("Springfield")
            .country("USA")
            .province("Springfield")
            .number("123")
            .PostalCode("12345")
            .build();

    Client client = Client.builder()
            .name("Daniel")
            .surname("García")
            .email("daniel@gmail.com")
            .phone("123456789")
            .address(address)
            .image("https://via.placeholder.com/150")
            .build();

    ClientCreateDto clientCreateDto = ClientCreateDto.builder()
            .name("Daniel")
            .surname("García")
            .email("daniel@gmail.com")
            .phone("123456789")
            .address(address)
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
