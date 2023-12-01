package com.nullers.restbookstore.rest.client.exceptions;

import java.util.UUID;

public class ClientInOrderException extends ClientException{
        public ClientInOrderException(UUID id) {
            super("El cliente con id " + id + " tiene pedidos asociados");
        }
}
