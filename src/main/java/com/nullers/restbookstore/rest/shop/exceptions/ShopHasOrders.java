package com.nullers.restbookstore.rest.shop.exceptions;

import com.nullers.restbookstore.manager.error.exceptions.ResponseExceptionBadRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Clase ShopHasOrders
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ShopHasOrders extends ResponseExceptionBadRequest {
    /**
     * Constructor para crear una nueva ShopHasOrders con un mensaje específico.
     *
     * @param message El mensaje que describe la excepción.
     */
    public ShopHasOrders(String message) {
        super(message);
    }
}
