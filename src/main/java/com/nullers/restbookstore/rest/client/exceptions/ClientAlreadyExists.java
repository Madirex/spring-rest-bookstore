package com.nullers.restbookstore.rest.client.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class ClientAlreadyExists
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ClientAlreadyExists extends RuntimeException {
    /**
     * Constructor ClientAlreadyExists
     *
     * @param key   The key of the client
     * @param value The value of the client
     */
    public ClientAlreadyExists(String key, Object value) {
        super("Client con " + key + ": " + value + " ya existe");
    }
}
