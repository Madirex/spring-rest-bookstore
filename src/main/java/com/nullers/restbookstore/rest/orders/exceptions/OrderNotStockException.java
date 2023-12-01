package com.nullers.restbookstore.rest.orders.exceptions;

public class OrderNotStockException extends OrderException{
    public OrderNotStockException(Long id) {
        super("El producto con id " + id + " no tiene stock");
    }
}
