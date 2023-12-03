package com.nullers.restbookstore.rest.client.services;

import com.nullers.restbookstore.rest.client.dto.ClientCreateDto;
import com.nullers.restbookstore.rest.client.dto.ClientDto;
import com.nullers.restbookstore.rest.client.dto.ClientUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Interfaz ClientService
 */
public interface ClientService {

    /**
     * Método para obtener todos los clientes
     *
     * @param name     nombre del cliente
     * @param surname  apellidos del cliente
     * @param email    email del cliente
     * @param phone    teléfono del cliente
     * @param address  dirección del cliente
     * @param pageable paginación
     * @return clientes
     */
    Page<ClientDto> findAll(
            Optional<String> name,
            Optional<String> surname,
            Optional<String> email,
            Optional<String> phone,
            Optional<String> address,
            Pageable pageable
    );

    /**
     * Método para obtener un cliente por su id
     *
     * @param id id del cliente
     * @return cliente
     */
    ClientDto findById(UUID id);

    /**
     * Método para crear un cliente
     *
     * @param client DTO del cliente
     * @return cliente
     */
    ClientDto save(ClientCreateDto client);


    /**
     * Método para actualizar un cliente por su id
     *
     * @param id     id del cliente
     * @param client DTO del cliente
     * @return cliente
     */
    ClientDto update(UUID id, ClientUpdateDto client);

    /**
     * Método para obtener un cliente por su email
     *
     * @param email email del cliente
     * @return cliente
     */
    Optional<ClientDto> findByEmail(String email);

    /**
     * Método para eliminar un cliente por su id
     *
     * @param id id del cliente
     */
    void deleteById(UUID id);

    /**
     * Método para eliminar todos los clientes
     */
    void deleteAll();

}
