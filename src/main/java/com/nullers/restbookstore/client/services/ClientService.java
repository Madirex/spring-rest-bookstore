package com.nullers.restbookstore.client.services;

import com.nullers.restbookstore.client.dto.ClientCreateDto;
import com.nullers.restbookstore.client.dto.ClientDto;
import com.nullers.restbookstore.client.models.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientService {

    List<ClientDto> findAll();

    ClientDto findById(UUID id);

    ClientDto save(ClientCreateDto client);


    ClientDto update(UUID id, ClientCreateDto client);

    ClientDto findByEmail(String email);

    ClientDto addBookOfClient(UUID id, UUID bookId);

    ClientDto removeBookOfClient(UUID id, UUID bookId);

    void deleteById(UUID id);

    void deleteAll();

}
