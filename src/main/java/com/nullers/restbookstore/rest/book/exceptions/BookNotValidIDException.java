package com.nullers.restbookstore.rest.book.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class BookNotValidIDException
 *
 * @Author Madirex
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookNotValidIDException extends BookException {
    /**
     * Constructor BookNotValidIDException
     *
     * @param message Mensaje de error
     */
    public BookNotValidIDException(String message) {
        super("ID no válido - " + message);
    }
}