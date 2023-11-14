package com.nullers.restbookstore.pagination.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class PageNotValidException
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PageNotValidException extends PaginationException {
    /**
     * Constructor PageNotValidException
     *
     * @param message Mensaje de error
     */
    public PageNotValidException(String message) {
        super("Página no válida - " + message);
    }
}