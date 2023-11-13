package com.nullers.restbookstore.publisher.repositories;

import com.nullers.restbookstore.publisher.models.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Interface PublisherRepository
 *
 * @author jaimesalcedo1
 * */
@Repository
public interface PublisherRepository extends JpaRepository<Publisher, UUID>, JpaSpecificationExecutor<Publisher> {

}