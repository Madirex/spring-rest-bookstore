package com.nullers.restbookstore.rest.orders.exceptions;

import com.nullers.restbookstore.manager.error.exceptions.ResponseExceptionNotFound;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Clase OrderNotFoundException
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends ResponseExceptionNotFound {
    /**
     * Constructor OrderNotFoundException
     *
     * @param id id del pedido
     */
    public OrderNotFoundException(ObjectId id) {
        super("El pedido con id " + id + " no existe");
    }
}
