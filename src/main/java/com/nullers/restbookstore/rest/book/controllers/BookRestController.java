package com.nullers.restbookstore.rest.book.controllers;

import com.nullers.restbookstore.NOADD.PublisherNotFoundException;
import com.nullers.restbookstore.NOADD.PublisherNotValidIDException;
import com.nullers.restbookstore.rest.book.dto.CreateBookDTO;
import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.dto.PatchBookDTO;
import com.nullers.restbookstore.rest.book.dto.UpdateBookDTO;
import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.book.exceptions.BookNotValidUUIDException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Interface BookRestController
 *
 * @Author Madirex
 */
public interface BookRestController {
    ResponseEntity<List<GetBookDTO>> getAllBook(@Valid @RequestParam(required = false) String category);

    ResponseEntity<GetBookDTO> getBookById(@Valid @PathVariable String id)
            throws BookNotValidUUIDException, BookNotFoundException;

    ResponseEntity<GetBookDTO> postBook(@Valid @RequestBody CreateBookDTO book)
            throws PublisherNotFoundException, PublisherNotValidIDException;

    ResponseEntity<GetBookDTO> putBook(@Valid @PathVariable String id, @Valid @RequestBody UpdateBookDTO book)
            throws BookNotValidUUIDException, BookNotFoundException, PublisherNotFoundException, PublisherNotValidIDException;

    ResponseEntity<GetBookDTO> patchBook(@Valid @PathVariable String id, @Valid @RequestBody PatchBookDTO book)
            throws BookNotValidUUIDException, BookNotFoundException, PublisherNotFoundException, PublisherNotValidIDException;

    ResponseEntity<String> deleteBook(@Valid @PathVariable String id) throws BookNotValidUUIDException, BookNotFoundException;

    Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex);
}
