package com.nullers.restbookstore.book.exceptions;

/**
 * Class BookException
 *
 * @Author Madirex
 */
public abstract class BookException extends RuntimeException {
    /**
     * Constructor BookException
     *
     * @param message Mensaje de error
     */
    protected BookException(String message) {
        super(message);
    }
}