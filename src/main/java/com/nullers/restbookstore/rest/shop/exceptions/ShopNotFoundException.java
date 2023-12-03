package com.nullers.restbookstore.rest.shop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class ShopNotFoundException
 *
 * @author alexdor00
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ShopNotFoundException extends ShopException {

    /**
     * Constructor ShopHasOrders
     *
     * @param message Mensaje de error
     */
    public ShopNotFoundException(String message) {
        super("Tienda no encontrada - " + message);
    }
}
