package com.nullers.restbookstore.rest.book.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class BookNotValidUUIDException
 *
 * @Author Madirex
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookNotValidUUIDException extends BookException {
    /**
     * Constructor BookNotValidUUIDException
     *
     * @param message Mensaje de error
     */
    public BookNotValidUUIDException(String message) {
        super("UUID no válido - " + message);
    }
}