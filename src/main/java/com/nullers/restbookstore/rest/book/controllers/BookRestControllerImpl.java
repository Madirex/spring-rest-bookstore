package com.nullers.restbookstore.rest.book.controllers;

import com.nullers.restbookstore.NOADD.PublisherNotFoundException;
import com.nullers.restbookstore.NOADD.PublisherNotValidIDException;
import com.nullers.restbookstore.rest.book.dto.CreateBookDTO;
import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.dto.PatchBookDTO;
import com.nullers.restbookstore.rest.book.dto.UpdateBookDTO;
import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.book.exceptions.BookNotValidIDException;
import com.nullers.restbookstore.rest.book.services.BookServiceImpl;
import com.nullers.restbookstore.utils.pagination.PageResponse;
import com.nullers.restbookstore.utils.pagination.PaginationLinksUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Clase BookRestControllerImpl
 *
 * @Author Madirex
 */
@RestController
@RequestMapping("/api/books")
public class BookRestControllerImpl implements BookRestController {

    public static final String PUBLISHER_ID_NOT_VALID_MSG = "ID de publisher no válido: ";
    private final BookServiceImpl service;
    private final PaginationLinksUtils paginationLinksUtils;

    /**
     * Constructor de la clase
     *
     * @param service              Servicio de Book
     * @param paginationLinksUtils Utilidades para la paginación
     */
    @Autowired
    public BookRestControllerImpl(BookServiceImpl service, PaginationLinksUtils paginationLinksUtils) {
        this.service = service;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    /**
     * Método para obtener todos los Books
     *
     * @param publisher Publisher por la que filtrar
     * @return ResponseEntity con el código de estado
     */
    @GetMapping
    public ResponseEntity<PageResponse<GetBookDTO>> getAllBook(
            @Valid @RequestParam(required = false) Optional<String> publisher,
            @RequestParam(required = false) Optional<Double> maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        Sort sort = direction.equalsIgnoreCase(
                Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<GetBookDTO> pageResult = service.getAllBook(publisher, maxPrice, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    /**
     * Método para obtener un Book por su ID
     *
     * @param id ID del Book en formato String
     * @return ResponseEntity con el código de estado
     * @throws BookNotFoundException Si no se ha encontrado el Book con el ID indicado
     */
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<GetBookDTO> getBookById(@Valid @PathVariable Long id) throws BookNotFoundException {
        try {
            return ResponseEntity.ok(service.getBookById(id));
        } catch (BookNotValidIDException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del Book no es válido: " + e.getMessage());
        }
    }

    /**
     * Método para crear un Book
     *
     * @param book Objeto CreateBookDTO con los campos a crear
     * @return ResponseEntity con el código de estado
     */
    @PostMapping
    @Override
    public ResponseEntity<GetBookDTO> postBook(@Valid @RequestBody CreateBookDTO book) {
        try {
            GetBookDTO bookDTO = service.postBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(bookDTO);
        } catch (PublisherNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El publisher no se encuentra: " + e.getMessage());
        } catch (PublisherNotValidIDException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, PUBLISHER_ID_NOT_VALID_MSG + e.getMessage());
        }
    }

    /**
     * Método para actualizar un Book
     *
     * @param id   ID del Book en formato String
     * @param book Objeto UpdateBookDTO con los campos a actualizar
     * @return ResponseEntity con el código de estado
     * @throws BookNotFoundException   Si no se ha encontrado el Book con el ID indicado
     * @throws BookNotValidIDException Si el ID del Book no es válido
     */
    @PutMapping("/{id}")
    @Override
    public ResponseEntity<GetBookDTO> putBook(@Valid @PathVariable Long id, @Valid @RequestBody UpdateBookDTO book)
            throws BookNotFoundException, BookNotValidIDException {
        try {
            return ResponseEntity.ok(service.putBook(id, book));
        } catch (PublisherNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El Publisher no se encuentra: " + e.getMessage());
        } catch (PublisherNotValidIDException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, PUBLISHER_ID_NOT_VALID_MSG + e.getMessage());
        }
    }

    /**
     * Método para actualizar parcialmente un Book
     *
     * @param id   ID del Book en formato String
     * @param book Objeto PatchBookDTO con los campos a actualizar
     * @return ResponseEntity con el código de estado
     * @throws BookNotFoundException   Si no se ha encontrado el Book con el ID indicado
     * @throws BookNotValidIDException Si el ID del Book no es válido
     */
    @PatchMapping("/{id}")
    @Override
    public ResponseEntity<GetBookDTO> patchBook(@Valid @PathVariable Long id, @Valid @RequestBody PatchBookDTO book)
            throws BookNotFoundException, BookNotValidIDException {
        try {
            return ResponseEntity.ok(service.patchBook(id, book));
        } catch (PublisherNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El Publisher no se encuentra: " + e.getMessage());
        } catch (PublisherNotValidIDException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, PUBLISHER_ID_NOT_VALID_MSG + e.getMessage());
        }
    }

    /**
     * Método para eliminar un Book
     *
     * @param id ID del Book en formato String
     * @return ResponseEntity con el código de estado
     * @throws BookNotFoundException Si no se ha encontrado el Book con el ID indicado
     */
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<String> deleteBook(@Valid @PathVariable Long id) throws BookNotFoundException {
        try {
            service.deleteBook(id);
        } catch (BookNotValidIDException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID del Book no es válido: " + e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * Método para manejar las excepciones de validación
     *
     * @param ex Excepción
     * @return Mapa con los errores
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @Override
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    /**
     * Método para manejar las excepciones de ResponseStatusException
     *
     * @param ex Excepción
     * @return Error en ResponseEntity (mensaje y código de estado)
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        HttpStatusCode httpStatus = ex.getStatusCode();
        String mensaje = ex.getReason();
        return new ResponseEntity<>(mensaje, httpStatus);
    }

    /**
     * Método para subir una imagen a un Book
     *
     * @param id   ID del Book en formato String
     * @param file Fichero a subir
     * @return ResponseEntity con el código de estado
     */
    @PatchMapping(value = "/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GetBookDTO> newBookImg(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file) throws BookNotValidIDException, PublisherNotFoundException,
            BookNotFoundException, PublisherNotValidIDException, IOException {
        if (!file.isEmpty()) {
            return ResponseEntity.ok(service.updateImage(id, file, true));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha enviado una imagen para el Book");
        }
    }
}