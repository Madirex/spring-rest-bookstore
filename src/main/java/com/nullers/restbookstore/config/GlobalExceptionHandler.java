package com.nullers.restbookstore.config;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.hibernate.QueryException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileSizeLimitExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public ResponseEntity<String> handleFileSizeLimitExceeded(FileSizeLimitExceededException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("El tamaño del archivo excede el límite permitido. Máximo permitido: " + ex.getPermittedSize());
    }

    @ExceptionHandler(PropertyReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlePropertyReferenceException(PropertyReferenceException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al procesar la propiedad en la consulta: " + ex.getPropertyName());
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleInternalServerError(Exception ex) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body("Error al procesar la solicitud.");
//    }
}
