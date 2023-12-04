package com.nullers.restbookstore.manager.error;

import com.mongodb.MongoTimeoutException;
import com.nullers.restbookstore.manager.error.exceptions.ResponseExceptionBadRequest;
import com.nullers.restbookstore.manager.error.exceptions.ResponseExceptionConflict;
import com.nullers.restbookstore.manager.error.exceptions.ResponseExceptionNotFound;
import com.nullers.restbookstore.pagination.exceptions.PageNotValidException;
import com.nullers.restbookstore.pagination.models.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handler de excepciones globales
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.servlet.multipart.max-request-size}")
    private String maxSize;

    /**
     * Método para manejar las excepciones de validación
     *
     * @param ex Excepción
     * @return Mapa con los errores
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", HttpStatus.BAD_REQUEST.value());
        response.put("errors", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Método para manejar las excepciones de tipo MethodArgumentTypeMismatchException
     *
     * @param ex Excepción
     * @return ResponseEntity con el código de estado
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentTypeMismatchException ex) {
        var errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
                , getCurrentHttpRequest().getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Manejar excepciones HttpMessageNotReadableException
     *
     * @return ResponseEntity con el código de estado
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleException() {
        var errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "El formato de la consulta enviada es incorrecta."
                , getCurrentHttpRequest().getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Timeout Mongo (puede sucede cuando MongoDB está deshabilitado)
     *
     * @param ex Excepción
     * @return ResponseEntity con el código de estado
     */
    @ExceptionHandler(MongoTimeoutException.class)
    public ResponseEntity<ErrorResponse> handleException(MongoTimeoutException ex) {
        var errorResponse = new ErrorResponse(
                HttpStatus.REQUEST_TIMEOUT.value(),
                ex.getMessage()
                , getCurrentHttpRequest().getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(errorResponse);
    }

    /**
     * ResponseExceptionBadRequest
     *
     * @param ex Excepción
     * @return ResponseEntity con el código de estado
     */
    @ExceptionHandler(ResponseExceptionBadRequest.class)
    public ResponseEntity<ErrorResponse> handleException(ResponseExceptionBadRequest ex) {
        var errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
                , getCurrentHttpRequest().getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * ResponseExceptionConflict
     *
     * @param ex Excepción
     * @return ResponseEntity con el código de estado
     */
    @ExceptionHandler(ResponseExceptionConflict.class)
    public ResponseEntity<ErrorResponse> handleException(ResponseExceptionConflict ex) {
        var errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage()
                , getCurrentHttpRequest().getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * ResponseExceptionNotFound
     *
     * @param ex Excepción
     * @return ResponseEntity con el código de estado
     */
    @ExceptionHandler(ResponseExceptionNotFound.class)
    public ResponseEntity<ErrorResponse> handleException(ResponseExceptionNotFound ex) {
        var errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
                , getCurrentHttpRequest().getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    /**
     * Handle MaxUploadSizeExceededException
     *
     * @return ResponseEntity<ErrorResponse> response
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSizeException() {

        String message = "El tamaño del archivo supera el límite permitido. (" + maxSize + ")";
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message, getCurrentHttpRequest().getRequestURI()),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST
        );
    }

    /**
     * Método para manejar las excepciones de ResponseStatusException
     *
     * @param ex Excepción
     * @return Error en ResponseEntity (mensaje y código de estado)
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        HttpStatus httpStatus = HttpStatus.valueOf(ex.getStatusCode().value());
        var errorResponse = new ErrorResponse(
                httpStatus.value(),
                ex.getReason()
                , getCurrentHttpRequest().getRequestURI()
        );
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     * Handler de excepciones de tamaño de fichero excedido
     *
     * @param ex Excepción
     * @return Respuesta HTTP
     */
    @ExceptionHandler(FileSizeLimitExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public ResponseEntity<ErrorResponse> handleFileSizeLimitExceeded(FileSizeLimitExceededException ex) {
        var errorResponse = new ErrorResponse(
                HttpStatus.PAYLOAD_TOO_LARGE.value(),
                "El tamaño del archivo excede el límite permitido. Máximo permitido: " + ex.getPermittedSize()
                , getCurrentHttpRequest().getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(errorResponse);
    }

    /**
     * Handler de excepciones de referencia a una propiedad no existente
     *
     * @param ex Excepción
     * @return Respuesta HTTP
     */
    @ExceptionHandler(PropertyReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handlePropertyReferenceException(PropertyReferenceException ex) {
        var errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Error al procesar la propiedad en la consulta: " + ex.getPropertyName()
                , getCurrentHttpRequest().getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handler de excepciones de paginación
     *
     * @return Respuesta
     */
    @ExceptionHandler(PageNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handlePageNotValidException() {
        var errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                "Página no válida",
                getCurrentHttpRequest().getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    /**
     * Handler de excepciones de HttpMessageNotWritableException
     *
     * @return Respuesta
     */
    @ExceptionHandler(HttpMessageNotWritableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotWritableException() {
        var errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                "Error al escribir la respuesta",
                getCurrentHttpRequest().getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handler de excepciones de MissingPathVariableException
     *
     * @return Respuesta
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ErrorResponse> handleMissingPathVariable() {
        var errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                "No has enviado todos los parámetros necesarios para la consulta en el Path",
                getCurrentHttpRequest().getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handler de excepciones de IllegalArgumentException
     *
     * @param exception excepción
     * @return ErrorResponse con el mensaje de error
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        var errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage(),
                getCurrentHttpRequest().getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Método para obtener la petición HTTP actual
     *
     * @return HttpServletRequest
     */
    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes();
        return requestAttributes.getRequest();
    }
}
