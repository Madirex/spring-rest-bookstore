package com.nullers.restbookstore.rest.shop.exceptions;

/**
 * Clase ShopHasOrders
 */
public class ShopHasOrders extends ShopException {
    /**
     * Constructor para crear una nueva ShopHasOrders con un mensaje específico.
     *
     * @param message El mensaje que describe la excepción.
     */
    public ShopHasOrders(String message) {
        super(message);
    }
}
