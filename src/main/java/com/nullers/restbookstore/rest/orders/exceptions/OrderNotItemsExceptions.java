package com.nullers.restbookstore.rest.orders.exceptions;

import com.nullers.restbookstore.manager.error.exceptions.ResponseExceptionBadRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Clase OrderNotFoundException
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderNotItemsExceptions extends ResponseExceptionBadRequest {
    /**
     * Constructor OrderNotFoundException
     *
     * @param id id del pedido
     */
    public OrderNotItemsExceptions(String id) {
        super("El pedido con id " + id + " no tiene items");
    }
}
