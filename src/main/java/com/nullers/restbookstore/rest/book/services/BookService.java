package com.nullers.restbookstore.rest.book.services;

import com.nullers.restbookstore.NOADD.PublisherNotFoundException;
import com.nullers.restbookstore.NOADD.PublisherNotValidIDException;
import com.nullers.restbookstore.rest.book.dto.CreateBookDTO;
import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.dto.PatchBookDTO;
import com.nullers.restbookstore.rest.book.dto.UpdateBookDTO;
import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.book.exceptions.BookNotValidIDException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * Interface BookService
 */
public interface BookService {
    Page<GetBookDTO> getAllBook(Optional<String> category, Optional<Double> maxPrice, PageRequest pageable);

    GetBookDTO getBookById(Long id) throws BookNotValidIDException, BookNotFoundException;

    GetBookDTO postBook(CreateBookDTO book) throws PublisherNotFoundException, PublisherNotValidIDException;

    GetBookDTO putBook(Long id, UpdateBookDTO book) throws BookNotValidIDException, BookNotFoundException, PublisherNotFoundException, PublisherNotValidIDException;

    GetBookDTO patchBook(Long id, PatchBookDTO book) throws BookNotValidIDException, BookNotFoundException, PublisherNotFoundException, PublisherNotValidIDException;

    void deleteBook(Long id) throws BookNotFoundException, BookNotValidIDException;

    GetBookDTO updateImage(Long id, MultipartFile image, Boolean withUrl) throws BookNotFoundException, BookNotValidIDException, PublisherNotFoundException, PublisherNotValidIDException, IOException;
}
