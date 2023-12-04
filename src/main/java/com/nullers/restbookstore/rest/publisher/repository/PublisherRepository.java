package com.nullers.restbookstore.rest.publisher.repository;

import com.nullers.restbookstore.rest.publisher.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Interface PublisherRepository
 *
 * @author jaimesalcedo1
 */
@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long>, JpaSpecificationExecutor<Publisher> {

}
