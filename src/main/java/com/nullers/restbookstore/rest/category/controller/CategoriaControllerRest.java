package com.nullers.restbookstore.rest.category.controller;


import com.nullers.restbookstore.pagination.utils.PageResponse;
import com.nullers.restbookstore.pagination.utils.PaginationLinksUtils;
import com.nullers.restbookstore.rest.category.dto.CategoriaCreateDto;
import com.nullers.restbookstore.rest.category.exceptions.CategoriaConflictException;
import com.nullers.restbookstore.rest.category.exceptions.CategoriaNotFoundException;
import com.nullers.restbookstore.rest.category.models.Categoria;
import com.nullers.restbookstore.rest.category.services.CategoriaServiceJpa;
import com.nullers.restbookstore.rest.publisher.models.responses.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@RestController
@RequestMapping("/api/categories")
public class CategoriaControllerRest {

    private final CategoriaServiceJpa service;

    private final PaginationLinksUtils paginationLinksUtils;

    @Autowired
    public CategoriaControllerRest(CategoriaServiceJpa service, PaginationLinksUtils paginationLinksUtils) {
        this.service = service;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    @GetMapping
    public ResponseEntity<PageResponse<Categoria>> getCategorias(
            @RequestParam(required = false) Optional<String> nombre,
            @RequestParam(required = false) Optional<Boolean> activa,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            HttpServletRequest request
    ){
        Sort sort = order.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<Categoria> result = service.getAll(nombre, activa, pageable);
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(result, uriBuilder))
                .body(PageResponse.of(result, sortBy, order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getCategoria(@PathVariable UUID id){
        return ResponseEntity.ok(service.getCategoriaById(id));
    }

    @PostMapping
    public ResponseEntity<Categoria> addCategoria(@Valid @RequestBody CategoriaCreateDto categoriaCreateDto){
        return ResponseEntity.ok(service.createCategoria(categoriaCreateDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable UUID id, @Valid @RequestBody CategoriaCreateDto categoriaCreateDto){
        return ResponseEntity.ok(service.updateCategoria(id, categoriaCreateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable UUID id){
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(CategoriaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFunkoNotFound(CategoriaNotFoundException exception){
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }


    /**
     * Manejador de excepciones de IllegalArgumentException
     * @param exception excepción
     * @return ErrorResponse con el mensaje de error
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegelAlrgumentException(IllegalArgumentException exception){
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }


    @ExceptionHandler(CategoriaConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleCategoriaConflictException(CategoriaConflictException exception){
        return new ErrorResponse(HttpStatus.CONFLICT.value(), exception.getMessage());
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
