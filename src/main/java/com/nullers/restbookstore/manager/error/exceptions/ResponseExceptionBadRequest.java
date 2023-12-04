package com.nullers.restbookstore.manager.error.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Class ResponseException
 *
 * @Author Madirex
 */
public abstract class ResponseExceptionBadRequest extends RuntimeException implements ResponseExceptionInterface {
    /**
     * Constructor ResponseException
     *
     * @param message Mensaje de error
     */
    protected ResponseExceptionBadRequest(String message) {
        super(message);
    }

    /**
     * Devuelve el HttpStatus
     *
     * @return HttpStatus
     */
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
