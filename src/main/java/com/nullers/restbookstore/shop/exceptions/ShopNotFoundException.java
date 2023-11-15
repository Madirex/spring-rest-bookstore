package com.nullers.restbookstore.shop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción para cuando una tienda no se encuentra.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ShopNotFoundException extends ShopException {
    public ShopNotFoundException(String message) {
        super("Tienda no encontrada - " + message);
    }
}
