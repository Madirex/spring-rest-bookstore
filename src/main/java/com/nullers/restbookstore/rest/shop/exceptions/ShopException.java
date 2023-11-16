package com.nullers.restbookstore.rest.shop.exceptions;

/**
 * Clase abstracta ShopExceptio
 *
 * @author alexdor00
 */
public abstract class ShopException extends RuntimeException {

    /**
     * Constructor para crear una nueva ShopException con un mensaje específico.
     * Este mensaje describe la causa  de la excepción que se ha producido.
     *
     * @param message El mensaje que describe la excepción.
     */
    protected ShopException(String message) {
        super(message);
    }
}

