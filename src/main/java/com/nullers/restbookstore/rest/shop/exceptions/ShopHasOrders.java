package com.nullers.restbookstore.rest.shop.exceptions;

import com.nullers.restbookstore.manager.error.exceptions.ResponseExceptionNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Clase ShopHasOrders
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ShopHasOrders extends ResponseExceptionNotFound {
    /**
     * Constructor para crear una nueva ShopHasOrders con un mensaje específico.
     *
     * @param message El mensaje que describe la excepción.
     */
    public ShopHasOrders(String message) {
        super(message);
    }
}
