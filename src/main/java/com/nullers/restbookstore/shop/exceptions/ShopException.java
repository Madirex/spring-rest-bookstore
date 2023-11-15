package com.nullers.restbookstore.shop.exceptions;

/**
 * Clase base para excepciones de Shop.
 */
public abstract class ShopException extends RuntimeException {
    protected ShopException(String message) {
        super(message);
    }
}
