package com.nullers.restbookstore.rest.auth.exceptions;

import com.nullers.restbookstore.manager.error.exceptions.ResponseExceptionBadRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception para cuando el usuario no existe
 *
 * @Author Binwei Wang
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserPasswordsDiffers extends ResponseExceptionBadRequest {
    /**
     * Constructor
     *
     * @param message Mensaje de error
     */
    public UserPasswordsDiffers(String message) {
        super(message);
    }
}
