package com.nullers.restbookstore.book.controllers;

import com.nullers.restbookstore.NOADD.PublisherNotFoundException;
import com.nullers.restbookstore.NOADD.PublisherNotValidIDException;
import com.nullers.restbookstore.book.dto.CreateBookDTO;
import com.nullers.restbookstore.book.dto.GetBookDTO;
import com.nullers.restbookstore.book.dto.PatchBookDTO;
import com.nullers.restbookstore.book.dto.UpdateBookDTO;
import com.nullers.restbookstore.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.book.exceptions.BookNotValidUUIDException;
import com.nullers.restbookstore.book.services.BookServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * Constructor de la clase
     *
     * @param service Servicio de Book
     */
    @Autowired
    public BookRestControllerImpl(BookServiceImpl service) {
        this.service = service;
    }

    /**
     * Método para obtener todos los Books
     *
     * @param publisher Publisher por la que filtrar
     * @return ResponseEntity con el código de estado
     */
    @GetMapping
    @Override
    public ResponseEntity<List<GetBookDTO>> getAllBook(@Valid @RequestParam(required = false) String publisher) {
        if (publisher != null && !publisher.isEmpty()) {
            return ResponseEntity.ok(service.getAllBookFilterByPublisher(service.getAllBook(), publisher));
        } else {
            return ResponseEntity.ok(service.getAllBook());
        }
    }

    /**
     * Método para obtener un Book por su UUID
     *
     * @param id UUID del Book en formato String
     * @return ResponseEntity con el código de estado
     * @throws BookNotFoundException Si no se ha encontrado el Book con el UUID indicado
     */
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<GetBookDTO> getBookById(@Valid @PathVariable String id) throws BookNotFoundException {
        try {
            return ResponseEntity.ok(service.getBookById(id));
        } catch (BookNotValidUUIDException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El UUID del Book no es válido: " + e.getMessage());
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
     * @param id   UUID del Book en formato String
     * @param book Objeto UpdateBookDTO con los campos a actualizar
     * @return ResponseEntity con el código de estado
     * @throws BookNotFoundException     Si no se ha encontrado el Book con el UUID indicado
     * @throws BookNotValidUUIDException Si el UUID del Book no es válido
     */
    @PutMapping("/{id}")
    @Override
    public ResponseEntity<GetBookDTO> putBook(@Valid @PathVariable String id, @Valid @RequestBody UpdateBookDTO book)
            throws BookNotFoundException, BookNotValidUUIDException {
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
     * @param id   UUID del Book en formato String
     * @param book Objeto PatchBookDTO con los campos a actualizar
     * @return ResponseEntity con el código de estado
     */
    @PatchMapping("/{id}")
    @Override
    public ResponseEntity<GetBookDTO> patchBook(@Valid @PathVariable String id, @Valid @RequestBody PatchBookDTO book)
            throws BookNotFoundException, BookNotValidUUIDException {
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
     * @param id UUID del Book en formato String
     * @return ResponseEntity con el código de estado
     * @throws BookNotFoundException Si no se ha encontrado el Book con el UUID indicado
     */
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<String> deleteBook(@Valid @PathVariable String id) throws BookNotFoundException {
        try {
            service.deleteBook(id);
        } catch (BookNotValidUUIDException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El UUID del Book no es válido: " + e.getMessage());
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
     * @return Error
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
     * @param id   UUID del Book en formato String
     * @param file Fichero a subir
     * @return ResponseEntity con el código de estado
     */
    @PatchMapping(value = "/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GetBookDTO> newBookImg(
            @PathVariable String id,
            @RequestPart("file") MultipartFile file) throws BookNotValidUUIDException, PublisherNotFoundException,
            BookNotFoundException, PublisherNotValidIDException, IOException {
        if (!file.isEmpty()) {
            return ResponseEntity.ok(service.updateImage(id, file, true));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha enviado una imagen para el Book");
        }
    }
}