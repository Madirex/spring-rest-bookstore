package com.nullers.restbookstore.rest.book.controllers;

import com.nullers.restbookstore.pagination.models.PageResponse;
import com.nullers.restbookstore.pagination.util.PaginationLinksUtils;
import com.nullers.restbookstore.rest.book.dto.CreateBookDTO;
import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.dto.PatchBookDTO;
import com.nullers.restbookstore.rest.book.dto.UpdateBookDTO;
import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.book.exceptions.BookNotValidIDException;
import com.nullers.restbookstore.rest.book.services.BookServiceImpl;
import com.nullers.restbookstore.rest.common.PageableRequest;
import com.nullers.restbookstore.rest.publisher.exceptions.PublisherIDNotValid;
import com.nullers.restbookstore.rest.publisher.exceptions.PublisherNotFound;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

/**
 * Clase BookRestControllerImpl
 *
 * @Author Madirex
 */
@RestController
@RequestMapping("/api/books")
public class BookRestControllerImpl implements BookRestController {

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
    @Operation(summary = "Obtiene todos los libros", description = "Obtiene una lista de libros")
    @Parameter(name = "publisher", description = "Publisher del libro", example = "Madirex")
    @Parameter(name = "maxPrice", description = "Precio máximo", example = "12.2")
    @Parameter(name = "category", description = "Categoría del libro", example = "Terror")
    @Parameter(name = "page", description = "Número de página", example = "0")
    @Parameter(name = "size", description = "Tamaño de la página", example = "10")
    @Parameter(name = "orderBy", description = "Campo de ordenación", example = "id")
    @Parameter(name = "order", description = "Dirección de ordenación", example = "asc")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de libros"),
            @ApiResponse(responseCode = "400", description = "Petición no válida")
    })
    @GetMapping()
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PageResponse<GetBookDTO>> getAllBook(
            @Valid @RequestParam(required = false) Optional<String> publisher,
            @RequestParam(required = false) Optional<Double> maxPrice,
            @RequestParam(required = false) Optional<String> category,
            @Valid PageableRequest pageableRequest,
            HttpServletRequest request
    ) {
        String orderBy = pageableRequest.getOrderBy();
        String order = pageableRequest.getOrder();
        Integer page = pageableRequest.getPage();
        Integer size = pageableRequest.getSize();
        Sort sort = order.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(orderBy).ascending() : Sort.by(orderBy).descending();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<GetBookDTO> pageResult = service.getAllBook(publisher, maxPrice, category, PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, orderBy, order));
    }

    /**
     * Método para obtener un Book por su ID
     *
     * @param id ID del Book
     * @return ResponseEntity con el código de estado
     * @throws BookNotFoundException Si no se ha encontrado el Book con el ID indicado
     */
    @Operation(summary = "Busca un libro dada su ID", description = "Busca un libro dada su ID")
    @Parameter(name = "id", description = "Identificador del libro", example = "1", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro encontrado"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado"),
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Override
    public ResponseEntity<GetBookDTO> getBookById(@Valid @PathVariable Long id) throws BookNotFoundException {
        return ResponseEntity.ok(service.getBookById(id));
    }

    /**
     * Método para crear un Book
     *
     * @param book Objeto CreateBookDTO con los campos a crear
     * @return ResponseEntity con el código de estado
     */
    @Operation(summary = "Crea un libro", description = "Crea un libro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Libro creado"),
            @ApiResponse(responseCode = "400", description = "Libro no válido"),
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<GetBookDTO> postBook(@Valid @RequestBody CreateBookDTO book) {
        GetBookDTO bookDTO = service.postBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookDTO);
    }

    /**
     * Método para actualizar un Book
     *
     * @param id   ID del Book
     * @param book Objeto UpdateBookDTO con los campos a actualizar
     * @return ResponseEntity con el código de estado
     * @throws BookNotFoundException   Si no se ha encontrado el Book con el ID indicado
     * @throws BookNotValidIDException Si el ID del Book no es válido
     */
    @Operation(summary = "Actualiza un libro", description = "Actualiza un libro")
    @Parameter(name = "id", description = "Identificador del libro", example = "1", required = true)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Libro a actualizar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro actualizado"),
            @ApiResponse(responseCode = "400", description = "Libro no válido"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado"),
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<GetBookDTO> putBook(@Valid @PathVariable Long id, @Valid @RequestBody UpdateBookDTO book)
            throws BookNotFoundException, BookNotValidIDException {
        return ResponseEntity.ok(service.putBook(id, book));
    }

    /**
     * Método para actualizar parcialmente un Book
     *
     * @param id   ID del Book
     * @param book Objeto PatchBookDTO con los campos a actualizar
     * @return ResponseEntity con el código de estado
     * @throws BookNotFoundException   Si no se ha encontrado el Book con el ID indicado
     * @throws BookNotValidIDException Si el ID del Book no es válido
     */
    @Operation(summary = "Actualiza un libro parcialmente", description = "Actualiza un libro parcialmente")
    @Parameter(name = "id", description = "Identificador del libro", example = "1", required = true)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Libro a actualizar parcialmente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro actualizado"),
            @ApiResponse(responseCode = "400", description = "Libro no válido"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado"),
    })
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<GetBookDTO> patchBook(@Valid @PathVariable Long id, @Valid @RequestBody PatchBookDTO book)
            throws BookNotFoundException, BookNotValidIDException {
        return ResponseEntity.ok(service.patchBook(id, book));
    }

    /**
     * Método para eliminar un Book
     *
     * @param id ID del Book
     * @return ResponseEntity con el código de estado
     * @throws BookNotFoundException Si no se ha encontrado el Book con el ID indicado
     */
    @Operation(summary = "Borra un libro", description = "Borra un libro")
    @Parameter(name = "id", description = "Identificador del libro", example = "1", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Libro borrado"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado"),
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<String> deleteBook(@Valid @PathVariable Long id) throws BookNotFoundException {
        service.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Método para subir una imagen a un Book
     *
     * @param id   ID del Book
     * @param file Fichero a subir
     * @return ResponseEntity con el código de estado
     * @throws BookNotFoundException Si no se ha encontrado el Book con el ID indicado
     * @throws PublisherNotFound     Si no se ha encontrado el Publisher con el ID indicado
     * @throws BookNotFoundException Si no se ha encontrado el Book con el ID indicado
     * @throws PublisherIDNotValid   Si el ID del Publisher no es válido
     * @throws IOException           Si ha habido un error al subir la imagen
     */
    @Operation(summary = "Actualiza la imagen de un libro", description = "Actualiza la imagen de un libro")
    @Parameter(name = "file", description = "Fichero a subir", required = true)
    @Parameter(name = "id", description = "Identificador del libro", example = "1", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro actualizado"),
            @ApiResponse(responseCode = "400", description = "Libro no válido"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado"),
    })
    @PatchMapping(value = "/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GetBookDTO> newBookImg(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file) throws BookNotValidIDException, PublisherNotFound,
            BookNotFoundException, PublisherIDNotValid, IOException {
        if (!file.isEmpty()) {
            return ResponseEntity.ok(service.updateImage(id, file, true));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha enviado una imagen para el Book");
        }
    }
}