package com.nullers.restbookstore.manager.error;

import com.nullers.restbookstore.pagination.exceptions.PageNotValidException;
import com.nullers.restbookstore.pagination.models.ErrorResponse;
import com.nullers.restbookstore.rest.category.exceptions.CategoriaNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manejador de excepciones globales
 */
@ControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(CategoriaNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCategoryNotFound(CategoriaNotFoundException exception) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
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
//                ,getCurrentHttpRequest().getRequestURI() //TODO: Agregar URI
        );
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    /**
     * Manejador de excepciones de tamaño de fichero excedido
     *
     * @param ex Excepción
     * @return Respuesta HTTP
     */
    @ExceptionHandler(FileSizeLimitExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public ResponseEntity<String> handleFileSizeLimitExceeded(FileSizeLimitExceededException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("El tamaño del archivo excede el límite permitido. Máximo permitido: " + ex.getPermittedSize());
    }

    /**
     * Manejador de excepciones de referencia a una propiedad no existente
     *
     * @param ex Excepción
     * @return Respuesta HTTP
     */
    @ExceptionHandler(PropertyReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlePropertyReferenceException(PropertyReferenceException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al procesar la propiedad en la consulta: " + ex.getPropertyName());
    }

    /**
     * Manejador de excepciones de paginación
     *
     * @param ex Excepción
     * @return Respuesta
     */
    @ExceptionHandler(PageNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlePageNotValidException(PageNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    /**
     * Manejador de excepciones de HttpMessageNotWritableException
     *
     * @param ex Excepción
     * @return Respuesta
     */
    @ExceptionHandler(HttpMessageNotWritableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleHttpMessageNotWritableException(HttpMessageNotWritableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
    }

    /**
     * Manejador de excepciones de MissingPathVariableException
     *
     * @param ex Excepción
     * @return Respuesta
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<String> handleMissingPathVariable(MissingPathVariableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("No has enviado todos los parámetros necesarios para la consulta en el Path");
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
