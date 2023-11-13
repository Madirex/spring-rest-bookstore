package com.nullers.restbookstore.rest.book.controllers;

import com.nullers.restbookstore.NOADD.PublisherNotFoundException;
import com.nullers.restbookstore.NOADD.PublisherNotValidIDException;
import com.nullers.restbookstore.rest.book.dto.CreateBookDTO;
import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.dto.PatchBookDTO;
import com.nullers.restbookstore.rest.book.dto.UpdateBookDTO;
import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.book.exceptions.BookNotValidIDException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * Interface BookRestController
 *
 * @Author Madirex
 */
public interface BookRestController {

    ResponseEntity<GetBookDTO> getBookById(@Valid @PathVariable Long id)
            throws BookNotValidIDException, BookNotFoundException;

    ResponseEntity<GetBookDTO> postBook(@Valid @RequestBody CreateBookDTO book)
            throws PublisherNotFoundException, PublisherNotValidIDException;

    ResponseEntity<GetBookDTO> putBook(@Valid @PathVariable Long id, @Valid @RequestBody UpdateBookDTO book)
            throws BookNotValidIDException, BookNotFoundException, PublisherNotFoundException, PublisherNotValidIDException;

    ResponseEntity<GetBookDTO> patchBook(@Valid @PathVariable Long id, @Valid @RequestBody PatchBookDTO book)
            throws BookNotValidIDException, BookNotFoundException, PublisherNotFoundException, PublisherNotValidIDException;

    ResponseEntity<String> deleteBook(@Valid @PathVariable Long id) throws BookNotValidIDException, BookNotFoundException;

    Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex);
}
