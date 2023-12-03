package com.nullers.restbookstore.rest.client.repository;

import com.nullers.restbookstore.rest.client.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Interfaz ClientRepository
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, UUID>, JpaSpecificationExecutor<Client> {

    /**
     * Método para obtener un cliente por su email
     *
     * @param email email del cliente
     * @return cliente
     */
    Optional<Client> getClientByEmailEqualsIgnoreCase(String email);

}
