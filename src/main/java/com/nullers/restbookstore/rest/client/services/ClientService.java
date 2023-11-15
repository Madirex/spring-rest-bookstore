package com.nullers.restbookstore.rest.client.services;

import com.nullers.restbookstore.rest.client.dto.ClientCreateDto;
import com.nullers.restbookstore.rest.client.dto.ClientDto;
import com.nullers.restbookstore.rest.client.dto.ClientUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ClientService {

    Page<ClientDto> findAll(
            Optional<String> name,
            Optional<String> surname,
            Optional<String> email,
            Optional<String> phone,
            Optional<String> address,
            Pageable pageable
    );

    ClientDto findById(UUID id);

    ClientDto save(ClientCreateDto client);


    ClientDto update(UUID id, ClientUpdateDto client);

    Optional<ClientDto> findByEmail(String email);

    ClientDto addBookToClient(UUID id, UUID bookId);

    ClientDto removeBookOfClient(UUID id, UUID bookId);

    void deleteById(UUID id);

    void deleteAll();

}
