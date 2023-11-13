package com.nullers.restbookstore.rest.book.controllers;

import com.nullers.restbookstore.NOADD.PublisherNotFoundException;
import com.nullers.restbookstore.NOADD.PublisherNotValidIDException;
import com.nullers.restbookstore.rest.book.dto.CreateBookDTO;
import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.dto.PatchBookDTO;
import com.nullers.restbookstore.rest.book.dto.UpdateBookDTO;
import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.book.exceptions.BookNotValidIDException;
import com.nullers.restbookstore.utils.pagination.PageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;

/**
 * Interface BookRestController
 *
 * @Author Madirex
 */
public interface BookRestController {

    public ResponseEntity<PageResponse<GetBookDTO>> getAllBook(
            @Valid @RequestParam(required = false) Optional<String> publisher,
            @RequestParam(required = false) Optional<Double> maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    );

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
