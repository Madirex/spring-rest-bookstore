package com.nullers.restbookstore.rest.category.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class CategoryConflictException
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class CategoryConflictException extends RuntimeException {
    /**
     * Constructor CategoryConflictException
     *
     * @param msg Mensaje de error
     */
    public CategoryConflictException(String msg) {
        super(msg);
    }
}
