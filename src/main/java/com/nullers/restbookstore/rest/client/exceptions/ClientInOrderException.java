package com.nullers.restbookstore.rest.client.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

/**
 * Class ClientInOrderException
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ClientInOrderException extends RuntimeException {
    /**
     * Constructor ClientInOrderException
     *
     * @param id id del cliente
     */
    public ClientInOrderException(UUID id) {
        super("El cliente con id " + id + " tiene pedidos asociados");
    }
}
