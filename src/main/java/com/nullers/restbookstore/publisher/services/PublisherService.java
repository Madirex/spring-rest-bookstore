package com.nullers.restbookstore.publisher.services;

import com.nullers.restbookstore.publisher.dto.CreatePublisherDto;
import com.nullers.restbookstore.publisher.dto.PublisherDto;

import java.util.List;
import java.util.UUID;

/**
 * Interface PublisherService
 *
 * @author jaimesalcedo1
 * */
public interface PublisherService {
    List<PublisherDto> findAll();

    PublisherDto findById(UUID id);

    PublisherDto save(CreatePublisherDto publisher);


    PublisherDto update(UUID id, CreatePublisherDto client);


    PublisherDto addBookPublisher(UUID id, UUID bookId);

    PublisherDto removeBookPublisher(UUID id, UUID bookId);

    void deleteById(UUID id);

    void deleteAll();
}
