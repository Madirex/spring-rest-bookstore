package com.nullers.restbookstore.rest.book.exceptions;

import com.nullers.restbookstore.manager.error.exceptions.ResponseExceptionNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class BookNotFoundException
 *
 * @Author Madirex
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookNotFoundException extends ResponseExceptionNotFound {
    /**
     * Constructor BookNotFoundException
     *
     * @param message Mensaje de error
     */
    public BookNotFoundException(String message) {
        super("Libro no encontrado - " + message);
    }
}