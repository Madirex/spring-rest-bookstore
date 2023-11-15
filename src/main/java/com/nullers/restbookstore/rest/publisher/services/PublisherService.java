package com.nullers.restbookstore.rest.publisher.services;

import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.publisher.dto.CreatePublisherDto;
import com.nullers.restbookstore.rest.publisher.dto.PublisherDTO;
import com.nullers.restbookstore.rest.publisher.exceptions.PublisherIDNotValid;
import com.nullers.restbookstore.rest.publisher.exceptions.PublisherNotFound;
import org.springframework.cache.annotation.CachePut;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Interface PublisherService
 *
 * @author jaimesalcedo1
 */
public interface PublisherService {
    List<PublisherDTO> findAll();

    PublisherDTO findById(Long id);

    PublisherDTO save(CreatePublisherDto publisher);


    PublisherDTO update(Long id, CreatePublisherDto client);


    PublisherDTO addBookPublisher(Long id, Long bookId);

    PublisherDTO removeBookPublisher(Long id, Long bookId);

    void deleteById(Long id);

    void deleteAll();


    PublisherDTO updateImage(Long id, MultipartFile image, Boolean withUrl) throws BookNotFoundException,
            PublisherNotFound, PublisherIDNotValid, IOException;
}
