package com.nullers.restbookstore.rest.client.repositories;

import com.nullers.restbookstore.rest.client.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID>, JpaSpecificationExecutor<Client> {

    public Optional<Client> getClientByEmailEqualsIgnoreCase(String email);


}
