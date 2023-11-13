package com.nullers.restbookstore.publisher.controller;

import com.nullers.restbookstore.publisher.dto.CreatePublisherDto;
import com.nullers.restbookstore.publisher.dto.PublisherDto;
import com.nullers.restbookstore.publisher.exceptions.PublisherNotFound;
import com.nullers.restbookstore.publisher.models.responses.ErrorResponse;
import com.nullers.restbookstore.publisher.services.PublisherServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Clase Controller
 *
 * @author jaimesalcedo1
 * */
@RestController
@RequestMapping("/publishers")
public class PublisherController {
    private PublisherServiceImpl publisherService;

    @Autowired
    public PublisherController(PublisherServiceImpl publisherService) {
        this.publisherService = publisherService;
    }


    /**
     * Metodo para obtener todas las editoriales
     *
     * @return ResponseEntity<List<PublisherDto>> con las editoriales
     * */
    @GetMapping
    public ResponseEntity<List<PublisherDto>> getAll() {
        return ResponseEntity.ok(publisherService.findAll());
    }

    /**
     * Metodo que obtiene una editorial dada su id
     *
     * @param id id por la que filtrar
     * @return ResponseEntity<PublisherDto>
     * */
    @GetMapping("/{id}")
    public ResponseEntity<PublisherDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(publisherService.findById(id));
    }

    /**
     * Metodo que crea un publisher
     *
     * @param publisherDto publisher a crear
     * @return ResponseEntity<PublisherDto>
     * */
    @PostMapping
    public ResponseEntity<PublisherDto> create(@Valid @RequestBody CreatePublisherDto publisherDto) {
        return ResponseEntity.ok(publisherService.save(publisherDto));
    }

    /**
     * metodo que actualiza un publisher
     *
     * @param publisherDto Publisher actualizado
     * @param id id del publisher a actualizar
     * @return ResponseEntity<PublisherDto>
     * */
    @PutMapping("/{id}")
    public ResponseEntity<PublisherDto> update(@PathVariable UUID id,@Valid @RequestBody CreatePublisherDto publisherDto) {
        return ResponseEntity.ok(publisherService.update(id, publisherDto));
    }

    /**
     * metodo que añade un libro a un publisher
     *
     * @param bookId id del libro
     * @param id id del publisher
     * @return ResponseEntity<PublisherDto>
     * */
    @PatchMapping("/books/{id}")
    public ResponseEntity<PublisherDto> updatePatchBook(@PathVariable UUID id, @RequestParam UUID bookId) {
        PublisherDto publisherDto = publisherService.addBookPublisher(id, bookId);
        ResponseEntity<PublisherDto> publisher = ResponseEntity.ok(publisherDto);
        System.out.println(publisher);
        return publisher;
    }

    /**
     * metodo que elimina un libro de un publisher
     *
     * @param bookId id del libro
     * @param id id del publisher
     * @return ResponseEntity<PublisherDto>
     * */
    @PatchMapping("/books/remove/{id}")
    public ResponseEntity<PublisherDto> updatePatchBookDelete(@PathVariable UUID id, @PathVariable UUID bookId) {
        return ResponseEntity.ok(publisherService.removeBookPublisher(id, bookId));
    }

    /**
     * metodo que elimina un publisher
     *
     * @param id id del publisher a eliminar
     * @return ResponseEntity<Void>
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        publisherService.deleteById(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * manejador de excepción PublisherNotFound
     *
     * @param exception excepción
     * @return ErrorResponse mensaje de error
     * */
    @ExceptionHandler(PublisherNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlePublisherNotFound(PublisherNotFound exception){
        return new com.nullers.restbookstore.publisher.models.responses.ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    /**
     * manejador de excepción MethodArgumentNotValid
     *
     * @param exception excepción
     * @return ResponseEntity<Map<String, Object>> con mensaje de error
     * */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", HttpStatus.BAD_REQUEST.value());
        response.put("errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
