package com.nullers.restbookstore.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * StorageBadRequest
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StorageBadRequest extends StorageException {
    @Serial
    private static final long serialVersionUID = 81248974575434657L;

    /**
     * StorageBadRequest
     *
     * @param msg Mensaje de error
     */
    public StorageBadRequest(String msg) {
        super(msg);
    }
}