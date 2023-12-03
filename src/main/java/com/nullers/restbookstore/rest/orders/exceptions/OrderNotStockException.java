package com.nullers.restbookstore.rest.orders.exceptions;

import com.nullers.restbookstore.manager.error.exceptions.ResponseExceptionNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Clase OrderNotStockException
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotStockException extends ResponseExceptionNotFound {
    /**
     * Constructor OrderNotStockException
     *
     * @param id id del producto
     */
    public OrderNotStockException(Long id) {
        super("El producto con id " + id + " no tiene stock");
    }
}
