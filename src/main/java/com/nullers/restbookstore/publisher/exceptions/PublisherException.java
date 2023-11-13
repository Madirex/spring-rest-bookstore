package com.nullers.restbookstore.publisher.exceptions;

/**
 * Clase PublisherException
 *
 * @author jaimesalcedo1
 * */
public abstract class PublisherException extends RuntimeException{
    protected PublisherException(String message){
        super(message);
    }
}
