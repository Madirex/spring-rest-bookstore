package com.nullers.restbookstore.pagination.models;

/**
 * ErrorResponse
 *
 * @param status the error status
 * @param msg    the error message
 * @param uri    the request uri
 */
public record ErrorResponse(int status, String msg, String uri) {
}
