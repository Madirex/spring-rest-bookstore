package com.nullers.restbookstore.rest.shop.exceptions;

/**
 * Clase  ShopExceptio
 *
 * @author alexdor00
 */
public abstract class ShopException extends RuntimeException {

    /**
     * Constructor ShopException
     *
     *  @param message Mensaje de error
     */
    protected ShopException(String message) {
        super(message);
    }
}

