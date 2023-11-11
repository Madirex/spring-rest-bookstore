package com.nullers.restbookstore.book.services;

import com.nullers.restbookstore.NOADD.PublisherNotFoundException;
import com.nullers.restbookstore.NOADD.PublisherNotValidIDException;
import com.nullers.restbookstore.book.dto.CreateBookDTO;
import com.nullers.restbookstore.book.dto.GetBookDTO;
import com.nullers.restbookstore.book.dto.PatchBookDTO;
import com.nullers.restbookstore.book.dto.UpdateBookDTO;
import com.nullers.restbookstore.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.book.exceptions.BookNotValidUUIDException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Interface BookService
 */
public interface BookService {
    List<GetBookDTO> getAllBook();

    GetBookDTO getBookById(String id) throws BookNotValidUUIDException, BookNotFoundException;

    GetBookDTO postBook(CreateBookDTO book) throws PublisherNotFoundException, PublisherNotValidIDException;

    GetBookDTO putBook(String id, UpdateBookDTO book) throws BookNotValidUUIDException, BookNotFoundException, PublisherNotFoundException, PublisherNotValidIDException;

    GetBookDTO patchBook(String id, PatchBookDTO book) throws BookNotValidUUIDException, BookNotFoundException, PublisherNotFoundException, PublisherNotValidIDException;

    void deleteBook(String id) throws BookNotFoundException, BookNotValidUUIDException;

    GetBookDTO updateImage(String id, MultipartFile image, Boolean withUrl) throws BookNotFoundException, BookNotValidUUIDException, PublisherNotFoundException, PublisherNotValidIDException, IOException;
}
