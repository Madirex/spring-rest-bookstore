package com.nullers.restbookstore.rest.orders.exceptions;

public class OrderBadPriceException extends OrderException{
    public OrderBadPriceException(Long id) {
        super("El precio del producto con id " + id + " no es correcto");
    }
}
