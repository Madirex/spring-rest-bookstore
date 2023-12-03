package com.nullers.restbookstore.rest.client.exceptions;

import com.nullers.restbookstore.manager.error.exceptions.ResponseExceptionNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class ClientNotFound
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClientNotFound extends ResponseExceptionNotFound {
    /**
     * Constructor ClientNotFound
     *
     * @param key   The key of the client
     * @param value The value of the client
     */
    public ClientNotFound(String key, Object value) {
        super("Client con " + key + ": " + value + " no existe");
    }
}
