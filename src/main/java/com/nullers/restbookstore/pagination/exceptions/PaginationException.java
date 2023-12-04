package com.nullers.restbookstore.pagination.exceptions;

/**
 * Class PaginationException
 */
public abstract class PaginationException extends RuntimeException {
    /**
     * Constructor PaginationException
     *
     * @param message Mensaje de error
     */
    protected PaginationException(String message) {
        super(message);
    }
}