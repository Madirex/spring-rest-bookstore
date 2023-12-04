package com.nullers.restbookstore.rest.client.exceptions;

import com.nullers.restbookstore.manager.error.exceptions.ResponseExceptionBadRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class ClientBadRequest
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClientBadRequest extends ResponseExceptionBadRequest {
    /**
     * Constructor ClientBadRequest
     *
     * @param message Mensaje de error
     */
    public ClientBadRequest(String message) {
        super(message);
    }
}
