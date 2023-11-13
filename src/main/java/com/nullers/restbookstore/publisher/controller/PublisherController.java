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


    @GetMapping
    public ResponseEntity<List<PublisherDto>> getAll() {
        return ResponseEntity.ok(publisherService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<PublisherDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(publisherService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PublisherDto> create(@Valid @RequestBody CreatePublisherDto publisherDto) {
        return ResponseEntity.ok(publisherService.save(publisherDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherDto> update(@PathVariable UUID id,@Valid @RequestBody CreatePublisherDto publisherDto) {
        return ResponseEntity.ok(publisherService.update(id, publisherDto));
    }

    @PatchMapping("/books/{id}")
    public ResponseEntity<PublisherDto> updatePatchBook(@PathVariable UUID id, @RequestParam UUID bookId) {
        PublisherDto publisherDto = publisherService.addBookPublisher(id, bookId);
        ResponseEntity<PublisherDto> publisher = ResponseEntity.ok(publisherDto);
        System.out.println(publisher);
        return publisher;
    }

    @PatchMapping("/books/remove/{id}")
    public ResponseEntity<PublisherDto> updatePatchBookDelete(@PathVariable UUID id, @PathVariable UUID bookId) {
        return ResponseEntity.ok(publisherService.removeBookPublisher(id, bookId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        publisherService.deleteById(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ExceptionHandler(PublisherNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlePublisherNotFound(PublisherNotFound exception){
        return new com.nullers.restbookstore.publisher.models.responses.ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

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
