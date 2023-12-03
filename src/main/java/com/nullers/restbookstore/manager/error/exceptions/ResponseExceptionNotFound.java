package com.nullers.restbookstore.manager.error.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Class ResponseException
 *
 * @Author Madirex
 */
public abstract class ResponseExceptionNotFound extends RuntimeException implements ResponseExceptionInterface {
    /**
     * Constructor ResponseException
     *
     * @param message Mensaje de error
     */
    protected ResponseExceptionNotFound(String message) {
        super(message);
    }

    /**
     * Devuelve el HttpStatus
     *
     * @return HttpStatus
     */
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
