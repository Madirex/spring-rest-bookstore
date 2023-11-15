package com.nullers.restbookstore.rest.client.exceptions;

import com.nullers.restbookstore.rest.client.models.responses.ErrorResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${spring.servlet.multipart.max-request-size}")
    private String maxSize;

    /**
     * Handle MaxUploadSizeExceededException
     * @param ex  exception
     * @param request request
     * @return ResponseEntity<ErrorResponse> response
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSizeException(
            MaxUploadSizeExceededException ex,
            WebRequest request) {

        String message = "El tamaño del archivo supera el límite permitido. (" + maxSize + ")";
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST
        );
    }

}
