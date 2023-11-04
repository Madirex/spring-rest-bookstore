package com.nullers.restbookstore.book.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class BookNotFoundException
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookNotFoundException extends BookException {
    /**
     * Constructor BookNotFoundException
     *
     * @param message Mensaje de error
     */
    public BookNotFoundException(String message) {
        super("Libro no encontrado - " + message);
    }
}