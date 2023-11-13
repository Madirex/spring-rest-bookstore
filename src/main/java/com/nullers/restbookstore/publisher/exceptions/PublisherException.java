package com.nullers.restbookstore.publisher.exceptions;

public abstract class PublisherException extends RuntimeException{
    protected PublisherException(String message){
        super(message);
    }
}
