package com.nullers.restbookstore.rest.orders.exceptions;

public class OrderException extends RuntimeException{
    public OrderException(String message) {
        super(message);
    }
}
