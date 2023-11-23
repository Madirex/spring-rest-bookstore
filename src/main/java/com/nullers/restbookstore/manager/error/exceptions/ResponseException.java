package com.nullers.restbookstore.manager.error.exceptions;

/**
 * Class ResponseException
 *
 * @Author Madirex
 */
public abstract class ResponseException extends RuntimeException implements ResponseExceptionInterface {
    /**
     * Constructor ResponseException
     *
     * @param message Mensaje de error
     */
    protected ResponseException(String message) {
        super(message);
    }
}
