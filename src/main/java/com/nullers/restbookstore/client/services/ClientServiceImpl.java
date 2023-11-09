package com.nullers.restbookstore.client.services;

import com.nullers.restbookstore.NOADD.models.Book;
import com.nullers.restbookstore.NOADD.repository.BookRepositoryJpa;
import com.nullers.restbookstore.client.dto.ClientCreateDto;
import com.nullers.restbookstore.client.dto.ClientDto;
import com.nullers.restbookstore.client.exceptions.ClientAlreadyExists;
import com.nullers.restbookstore.client.exceptions.ClientNotFound;
import com.nullers.restbookstore.client.mappers.ClientCreateMapper;
import com.nullers.restbookstore.client.mappers.ClientMapper;
import com.nullers.restbookstore.client.models.Client;
import com.nullers.restbookstore.client.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClientServiceImpl implements ClientService{

    private final ClientRepository clientRepository;

    private final BookRepositoryJpa bookRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, BookRepositoryJpa bookRepository) {
        this.clientRepository = clientRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<ClientDto> findAll() {
        return clientRepository.findAll().stream().map(ClientMapper::toDto).toList();
    }

    @Override
    public ClientDto findById(UUID id) {
        return clientRepository.findById(id).map(ClientMapper::toDto).orElseThrow(()-> new ClientNotFound("id",id));
    }

    @Override
    public ClientDto findByEmail(String email){
        return clientRepository.getClientByEmail(email).map(ClientMapper::toDto).orElseThrow(()-> new ClientAlreadyExists("email",email));
    }

    @Override
    public ClientDto save(ClientCreateDto client) {
        findByEmail(client.getEmail()); //Hace throw si no existe y comprobamos en controllador
        return ClientMapper.toDto(clientRepository.save(ClientCreateMapper.toEntity(client)));
    }

    @Override
    public ClientDto update(UUID id, ClientCreateDto client) {
        ClientDto clientToUpdate = findById(id);
        clientToUpdate.setName(client.getName());
        clientToUpdate.setSurname(client.getSurname());
        clientToUpdate.setEmail(client.getEmail());
        clientToUpdate.setPhone(client.getPhone());
        clientToUpdate.setAddress(client.getAddress());
        return ClientMapper.toDto(clientRepository.save(ClientMapper.toEntity(clientToUpdate)));
    }


    @Override
    public ClientDto addBookOfClient(UUID id, UUID bookId) {
        Book bookToAdd = bookRepository.getById(bookId);
        ClientDto clientToUpdate = findById(id);
        clientToUpdate.getBooks().add(bookToAdd);
        return clientToUpdate;
    }

    @Override
    public ClientDto removeBookOfClient(UUID id, UUID bookId) {
        Book bookToRemove = bookRepository.getById(bookId);
        ClientDto clientToUpdate = findById(id);
        clientToUpdate.getBooks().remove(bookToRemove);
        return clientToUpdate;
    }

    @Override
    public void deleteById(UUID id) {
        ClientDto clientToDelete = findById(id);
        clientRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        clientRepository.deleteAll();
    }
}
