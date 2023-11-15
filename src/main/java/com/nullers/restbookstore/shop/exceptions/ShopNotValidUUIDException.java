package com.nullers.restbookstore.shop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción para cuando un UUID de una tienda no es válido.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ShopNotValidUUIDException extends ShopException {
    public ShopNotValidUUIDException(String message) {
        super("UUID no válido - " + message);
    }
}
