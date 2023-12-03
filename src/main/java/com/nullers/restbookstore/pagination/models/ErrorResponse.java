package com.nullers.restbookstore.pagination.models;

/**
 * ErrorResponse
 *
 * @param status the error status
 * @param msg    the error message
 */
public record ErrorResponse(int status, String msg) {
}
