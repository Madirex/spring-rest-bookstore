package com.nullers.restbookstore.rest.book.services;

import com.nullers.restbookstore.NOADD.PublisherNotFoundException;
import com.nullers.restbookstore.NOADD.PublisherNotValidIDException;
import com.nullers.restbookstore.rest.book.dto.CreateBookDTO;
import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.dto.PatchBookDTO;
import com.nullers.restbookstore.rest.book.dto.UpdateBookDTO;
import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.book.exceptions.BookNotValidUUIDException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Interface BookService
 */
public interface BookService {
    Page<GetBookDTO> getAllBook(Optional<String> category, Optional<Double> maxPrice, PageRequest pageable);

    GetBookDTO getBookById(String id) throws BookNotValidUUIDException, BookNotFoundException;

    GetBookDTO postBook(CreateBookDTO book) throws PublisherNotFoundException, PublisherNotValidIDException;

    GetBookDTO putBook(String id, UpdateBookDTO book) throws BookNotValidUUIDException, BookNotFoundException, PublisherNotFoundException, PublisherNotValidIDException;

    GetBookDTO patchBook(String id, PatchBookDTO book) throws BookNotValidUUIDException, BookNotFoundException, PublisherNotFoundException, PublisherNotValidIDException;

    void deleteBook(String id) throws BookNotFoundException, BookNotValidUUIDException;

    GetBookDTO updateImage(String id, MultipartFile image, Boolean withUrl) throws BookNotFoundException, BookNotValidUUIDException, PublisherNotFoundException, PublisherNotValidIDException, IOException;
}
