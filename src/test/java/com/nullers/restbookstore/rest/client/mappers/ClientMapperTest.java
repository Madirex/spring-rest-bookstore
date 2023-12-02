package com.nullers.restbookstore.rest.client.mappers;

import com.nullers.restbookstore.rest.book.mappers.BookMapperImpl;
import com.nullers.restbookstore.rest.client.dto.ClientDto;
import com.nullers.restbookstore.rest.common.Address;
import com.nullers.restbookstore.rest.client.model.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientMapperTest {

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

    ClientDto clientDto = ClientDto.builder()
            .name("Daniel")
            .surname("García")
            .email("daniel@gmail.com")
            .phone("123456789")
            .address(address)
            .image("https://via.placeholder.com/150")
            .build();

    @Test
    void toEntityTest(){
        Client clientMapped = ClientMapper.toEntity(clientDto);
        assertAll(
                () -> assertEquals(client.getName(), clientMapped.getName()),
                () -> assertEquals(client.getSurname(), clientMapped.getSurname()),
                () -> assertEquals(client.getEmail(), clientMapped.getEmail()),
                () -> assertEquals(client.getPhone(), clientMapped.getPhone()),
                () -> assertEquals(client.getAddress(), clientMapped.getAddress()),
                () -> assertEquals(client.getImage(), clientMapped.getImage())
        );
    }

    @Test
    void toDto(){
        ClientDto clientDtoMapped = ClientMapper.toDto(client);
        assertAll(
                () -> assertEquals(clientDto.getName(), clientDtoMapped.getName()),
                () -> assertEquals(clientDto.getSurname(), clientDtoMapped.getSurname()),
                () -> assertEquals(clientDto.getEmail(), clientDtoMapped.getEmail()),
                () -> assertEquals(clientDto.getPhone(), clientDtoMapped.getPhone()),
                () -> assertEquals(clientDto.getAddress(), clientDtoMapped.getAddress()),
                () -> assertEquals(clientDto.getImage(), clientDtoMapped.getImage())
        );
    }


}
