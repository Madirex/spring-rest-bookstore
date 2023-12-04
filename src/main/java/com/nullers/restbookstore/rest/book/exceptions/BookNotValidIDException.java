package com.nullers.restbookstore.rest.book.exceptions;

import com.nullers.restbookstore.manager.error.exceptions.ResponseExceptionBadRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class BookNotValidIDException
 *
 * @Author Madirex
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookNotValidIDException extends ResponseExceptionBadRequest {
    /**
     * Constructor BookNotValidIDException
     *
     * @param message Mensaje de error
     */
    public BookNotValidIDException(String message) {
        super("ID no válido - " + message);
    }
}