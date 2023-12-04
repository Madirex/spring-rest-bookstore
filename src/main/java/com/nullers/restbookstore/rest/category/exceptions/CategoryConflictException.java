package com.nullers.restbookstore.rest.category.exceptions;

import com.nullers.restbookstore.manager.error.exceptions.ResponseExceptionConflict;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class CategoryConflictException
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class CategoryConflictException extends ResponseExceptionConflict {
    /**
     * Constructor CategoryConflictException
     *
     * @param msg Mensaje de error
     */
    public CategoryConflictException(String msg) {
        super(msg);
    }
}
