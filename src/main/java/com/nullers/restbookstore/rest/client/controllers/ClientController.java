package com.nullers.restbookstore.rest.client.controllers;

import com.nullers.restbookstore.pagination.models.PageResponse;
import com.nullers.restbookstore.pagination.util.PaginationLinksUtils;
import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.client.dto.ClientCreateDto;
import com.nullers.restbookstore.rest.client.dto.ClientDto;
import com.nullers.restbookstore.rest.client.dto.ClientUpdateDto;
import com.nullers.restbookstore.rest.client.exceptions.ClientAlreadyExists;
import com.nullers.restbookstore.rest.client.exceptions.ClientBadRequest;
import com.nullers.restbookstore.rest.client.exceptions.ClientInOrderException;
import com.nullers.restbookstore.rest.client.exceptions.ClientNotFound;
import com.nullers.restbookstore.pagination.models.ErrorResponse;
import com.nullers.restbookstore.rest.client.services.ClientServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.*;

/**
 * Controlador de Clientes
 *
 * @author daniel
 * @see ClientServiceImpl
 * @see PaginationLinksUtils
 * @see ClientCreateDto
 * @see ClientDto
 */
@RestController
@RequestMapping("/api/clients")
@PreAuthorize("hasRole('ADMIN')")
public class ClientController {


    private ClientServiceImpl clientService;
    private final PaginationLinksUtils paginationLinksUtils;

    private final List<String> contentTypesAllowed = List.of("image/png", "image/jpeg");


    @Autowired
    public ClientController(ClientServiceImpl clientService, PaginationLinksUtils paginationLinksUtils) {
        this.clientService = clientService;
        this.paginationLinksUtils = paginationLinksUtils;
    }


    /**
     * Obtiene todos los clientes
     *
     * @param name    nombre del cliente
     * @param surname apellido del cliente
     * @param email   email del cliente
     * @param phone   teléfono del cliente
     * @param address dirección del cliente
     * @param page    número de página
     * @param size    tamaño de la página
     * @param sortBy  campo por el que se ordena
     * @param order   orden de la página
     * @param request petición
     * @return ResponseEntity<PageResponse < ClientDto>> con los clientes
     */
    @GetMapping
    public ResponseEntity<PageResponse<ClientDto>> getAll(
            @RequestParam(required = false) Optional<String> name,
            @RequestParam(required = false) Optional<String> surname,
            @RequestParam(required = false) Optional<String> email,
            @RequestParam(required = false) Optional<String> phone,
            @RequestParam(required = false) Optional<String> address,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String order,
            HttpServletRequest request
    ) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("El numero de pagina no debe ser menor a 0 y el tamano de la pagina debe ser mayor que 0");
        }
        Sort sort = order.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<ClientDto> pageResult = clientService.findAll(name, surname, email, phone, address, pageable);
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, order));
    }


    /**
     * Obtiene un cliente por su id
     *
     * @param id id del cliente
     * @return ResponseEntity<ClientDto> con el cliente
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(clientService.findById(id));
    }

    /**
     * Obtiene un cliente por su email
     *
     * @param email email del cliente
     * @return ResponseEntity<ClientDto> con el cliente
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ClientDto> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(clientService.findByEmail(email).orElseThrow(() -> new ClientNotFound("email", email)));
    }

    /**
     * Crear un cliente
     *
     * @param clientDto cliente a crear
     * @return ResponseEntity<ClientDto> con el cliente creado
     */
    @PostMapping
    public ResponseEntity<ClientDto> create(@Valid @RequestBody ClientCreateDto clientDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.save(clientDto));
    }

    /**
     * Actualiza un cliente
     *
     * @param id        id del cliente
     * @param clientDto datos del cliente a actualizar
     * @return ResponseEntity<ClientDto> con el cliente actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> update(@PathVariable UUID id, @Valid @RequestBody ClientUpdateDto clientDto) {
        return ResponseEntity.ok(clientService.update(id, clientDto));
    }

    /**
     * Elimina un cliente
     *
     * @param id id del cliente
     * @return ResponseEntity<Void>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        clientService.deleteById(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Actualiza la imagen de un cliente
     *
     * @param id   id del cliente
     * @param file imagen del cliente
     * @return ResponseEntity<ClientDto> con el cliente
     */
    @PatchMapping(value = "{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClientDto> updatePatchImage(@PathVariable UUID id, @RequestPart("file") MultipartFile file) throws IOException {
        if (!file.isEmpty() && contentTypesAllowed.contains(file.getContentType())) {
            return ResponseEntity.ok(clientService.updateImage(id, file));
        } else {
            throw new ClientBadRequest("El archivo no es una imagen o esta vacio");
        }
    }

    /**
     * Manejador de excepciones de ClientNotFound
     *
     * @param exception excepción
     * @return ErrorResponse con el mensaje de error
     */
    @ExceptionHandler(ClientNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleClientNotFound(ClientNotFound exception) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }


    /**
     * Manejador de excepciones de ClientAlreadyExists
     *
     * @param exception excepción
     * @return ErrorResponse con el mensaje de error
     */
    @ExceptionHandler(ClientAlreadyExists.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleClientAlreadyExists(ClientAlreadyExists exception) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), exception.getMessage());
    }
    

    /**
     * Manejador de excepciones de ClientBadRequest
     *
     * @param exception excepción
     * @return ErrorResponse con el mensaje de error
     */
    @ExceptionHandler(ClientBadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleClientBadRequest(ClientBadRequest exception) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }


    @ExceptionHandler(ClientInOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleClientInOrderException(ClientInOrderException exception) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }


    /**
     * Manejador de excepciones de IllegalArgumentException
     *
     * @param exception excepción
     * @return ErrorResponse con el mensaje de error
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegelAlrgumentException(IllegalArgumentException exception) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    /**
     * Manejador de excepciones de MethodArgumentNotValidException
     *
     * @param ex excepción
     * @return ResponseEntity<Map < String, Object>> con el mensaje de error
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

}
