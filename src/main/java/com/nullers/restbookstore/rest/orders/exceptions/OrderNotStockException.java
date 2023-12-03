package com.nullers.restbookstore.rest.orders.exceptions;

/**
 * Clase OrderNotStockException
 */
public class OrderNotStockException extends RuntimeException {
    /**
     * Constructor OrderNotStockException
     *
     * @param id id del producto
     */
    public OrderNotStockException(Long id) {
        super("El producto con id " + id + " no tiene stock");
    }
}
