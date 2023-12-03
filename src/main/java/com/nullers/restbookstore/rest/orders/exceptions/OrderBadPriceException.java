package com.nullers.restbookstore.rest.orders.exceptions;

import com.nullers.restbookstore.manager.error.exceptions.ResponseExceptionBadRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Clase OrderBadPriceException
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderBadPriceException extends ResponseExceptionBadRequest {
    /**
     * Constructor OrderBadPriceException
     *
     * @param id id del producto
     */
    public OrderBadPriceException(Long id) {
        super("El precio del producto con id " + id + " no es correcto");
    }
}
