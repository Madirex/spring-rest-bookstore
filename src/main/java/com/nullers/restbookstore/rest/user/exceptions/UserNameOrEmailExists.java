package com.nullers.restbookstore.rest.user.exceptions;

import com.nullers.restbookstore.manager.error.exceptions.ResponseExceptionConflict;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción de usuario no encontrado
 *
 * @Author Binwei Wang
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class UserNameOrEmailExists extends ResponseExceptionConflict {
    /**
     * Constructor
     *
     * @param message mensaje de error
     */
    public UserNameOrEmailExists(String message) {
        super(message);
    }
}
