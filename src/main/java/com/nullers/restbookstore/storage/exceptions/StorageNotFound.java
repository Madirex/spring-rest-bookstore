package com.nullers.restbookstore.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

/**
 * StorageNotFound
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class StorageNotFound extends StorageException {
    @Serial
    private static final long serialVersionUID = 81248974575434657L;

    /**
     * StorageNotFound
     *
     * @param msg Mensaje de error
     */
    public StorageNotFound(String msg) {
        super(msg);
    }
}