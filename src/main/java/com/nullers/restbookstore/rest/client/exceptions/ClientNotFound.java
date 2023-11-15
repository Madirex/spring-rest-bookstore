package com.nullers.restbookstore.rest.client.exceptions;

public class ClientNotFound extends ClientException{
    public ClientNotFound(String key, Object value) {
        super("Client con "+key+": " + value + " no existe");
    }
}
