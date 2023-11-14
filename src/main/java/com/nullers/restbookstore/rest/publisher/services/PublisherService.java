package com.nullers.restbookstore.rest.publisher.services;

import com.nullers.restbookstore.rest.publisher.dto.CreatePublisherDto;
import com.nullers.restbookstore.rest.publisher.dto.PublisherDTO;

import java.util.List;
import java.util.UUID;

/**
 * Interface PublisherService
 *
 * @author jaimesalcedo1
 * */
public interface PublisherService {
    List<PublisherDTO> findAll();

    PublisherDTO findById(UUID id);

    PublisherDTO save(CreatePublisherDto publisher);


    PublisherDTO update(UUID id, CreatePublisherDto client);


    PublisherDTO addBookPublisher(UUID id, Long bookId);

    PublisherDTO removeBookPublisher(UUID id, Long bookId);

    void deleteById(UUID id);

    void deleteAll();
}
