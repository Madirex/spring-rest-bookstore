package com.nullers.restbookstore.client.exceptions;

import java.util.UUID;

public class ClientNotFound extends ClientException{
    public ClientNotFound(String key, Object value) {
        super("Client con "+key+": " + value + " no existe");
    }
}
