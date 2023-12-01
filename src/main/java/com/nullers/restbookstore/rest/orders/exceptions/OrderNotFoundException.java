package com.nullers.restbookstore.rest.orders.exceptions;

import org.bson.types.ObjectId;

public class OrderNotFoundException extends OrderException{
    public OrderNotFoundException(ObjectId id) {
        super("El pedido con id " + id + " no existe");
    }
}
