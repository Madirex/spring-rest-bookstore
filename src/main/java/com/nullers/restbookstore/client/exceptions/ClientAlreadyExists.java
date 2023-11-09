package com.nullers.restbookstore.client.exceptions;

public class ClientAlreadyExists extends ClientException{
    public ClientAlreadyExists(String key, Object value) {
        super("Client con "+key+": " + value + " ya existe");
    }
}
