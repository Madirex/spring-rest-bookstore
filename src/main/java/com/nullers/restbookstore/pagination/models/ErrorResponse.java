package com.nullers.restbookstore.pagination.models;

/**
 * ErrorResponse
 *
 * @param status the error status
 * @param error  the error message
 * @param uri    the request uri
 */
public record ErrorResponse(int status, String error, String uri) {
}
