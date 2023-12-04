package com.nullers.restbookstore.storage.exceptions;

import java.io.Serial;

/**
 * StorageException
 */
public abstract class StorageException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 81248974575434657L;

    /**
     * StorageException
     *
     * @param msg Mensaje de error
     */
    StorageException(String msg) {
        super(msg);
    }
}