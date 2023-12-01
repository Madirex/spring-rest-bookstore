package com.nullers.restbookstore.rest.orders.exceptions;

import org.bson.types.ObjectId;

public class OrderNotItemsExceptions extends OrderException{
    public OrderNotItemsExceptions(String id) {
        super("El pedido con id " + id + " no tiene items");
    }
}
