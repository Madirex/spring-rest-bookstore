package com.nullers.restbookstore.rest.publisher.controller;

import com.nullers.restbookstore.pagination.models.PageResponse;
import com.nullers.restbookstore.pagination.util.PaginationLinksUtils;
import com.nullers.restbookstore.rest.common.PageableRequest;
import com.nullers.restbookstore.rest.common.PageableUtil;
import com.nullers.restbookstore.rest.publisher.dto.CreatePublisherDto;
import com.nullers.restbookstore.rest.publisher.dto.PublisherDTO;
import com.nullers.restbookstore.rest.publisher.services.PublisherServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
 * Clase Controller
 *
 * @author jaimesalcedo1
 */
@RestController
@RequestMapping("/api/publishers")
public class PublisherController {
    private final PublisherServiceImpl publisherService;
    private final PaginationLinksUtils paginationLinksUtils;

    /**
     * Constructor
     *
     * @param publisherService     Servicio de Publisher
     * @param paginationLinksUtils Utilidad de paginación
     */
    @Autowired
    public PublisherController(PublisherServiceImpl publisherService, PaginationLinksUtils paginationLinksUtils) {
        this.publisherService = publisherService;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    /**
     * Método para obtener todas las editoriales
     *
     * @param name            nombre por el que filtrar
     * @param pageableRequest paginación
     * @return ResponseEntity<List < PublisherDto>> con las editoriales
     */
    @Operation(summary = "Obtiene todas las editoriales", description = "Obtiene una lista de editoriales")
    @Parameter(name = "name", description = "nombre de la editorial", example = "Madirex")
    @Parameter(name = "page", description = "número de la pagina", example = "0")
    @Parameter(name = "size", description = "tamaño de la pagina", example = "10")
    @Parameter(name = "sortBy", description = "campo de ordenación", example = "id")
    @Parameter(name = "direction", description = "dirección de ordenación", example = "asc")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "página de editoriales"),
            @ApiResponse(responseCode = "400", description = "petición de editoriales no válida")
    })
    @GetMapping()
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PageResponse<PublisherDTO>> getAll(
            @Valid @RequestParam(required = false) Optional<String> name,
            @Valid PageableRequest pageableRequest,
            HttpServletRequest request
    ) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<PublisherDTO> pageRes = publisherService.findAll(name, PageRequest.of(pageableRequest.getPage(),
                pageableRequest.getSize(), PageableUtil.getSort(pageableRequest)));

        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageRes, uriBuilder))
                .body(PageResponse.of(pageRes, pageableRequest.getOrderBy(), pageableRequest.getOrder()));
    }

    /**
     * Método que obtiene una editorial dada su id
     *
     * @param id id por la que filtrar
     * @return ResponseEntity<PublisherDto>
     */
    @Operation(summary = "Obtiene una editorial dado un id", description = "Obtiene una editorial dado un id")
    @Parameter(name = "id", description = "id de la editorial", example = "1")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Publisher"),
            @ApiResponse(responseCode = "404", description = "Publisher no encontrado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PublisherDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(publisherService.findById(id));
    }

    /**
     * Método que crea un publisher
     *
     * @param publisherDto publisher a crear
     * @return ResponseEntity<PublisherDto>
     */
    @Operation(summary = "Crea una editorial", description = "Crea una editorial")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Editorial a crear", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Editorial creada"),
            @ApiResponse(responseCode = "400", description = "Editorial no válida")
    })
    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<PublisherDTO> create(@Valid @RequestBody CreatePublisherDto publisherDto) {
        return ResponseEntity.ok(publisherService.save(publisherDto));
    }

    /**
     * Método que actualiza un publisher
     *
     * @param publisherDto Publisher actualizado
     * @param id           id del publisher a actualizar
     * @return ResponseEntity<PublisherDto>
     */
    @Operation(summary = "Actualiza una editorial", description = "Actualiza una editorial")
    @Parameter(name = "id", description = "id de la editorial a actualizar", example = "1")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Editorial actualizada", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "editorial actualizada"),
            @ApiResponse(responseCode = "400", description = "editorial no válida"),
            @ApiResponse(responseCode = "404", description = "editorial no encontrada")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<PublisherDTO> update(@PathVariable Long id, @Valid @RequestBody CreatePublisherDto publisherDto) {
        return ResponseEntity.ok(publisherService.update(id, publisherDto));
    }

    /**
     * Método que añade un libro a un publisher
     *
     * @param bookId id del libro
     * @param id     id del publisher
     * @return ResponseEntity<PublisherDto>
     */
    @Operation(summary = "Añade un libro a una editorial", description = "Añade un libro a una editorial")
    @Parameter(name = "id", description = "id de la editorial a actualizar", example = "1", required = true)
    @Parameter(name = "bookId", description = "id del libro a añadir", example = "1", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro añadido"),
            @ApiResponse(responseCode = "400", description = "Editorial no válida"),
            @ApiResponse(responseCode = "404", description = "Editorial no encontrada"),
    })
    @PatchMapping("/books/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<PublisherDTO> updatePatchBook(@PathVariable Long id, @RequestParam Long bookId) {
        PublisherDTO publisherDto = publisherService.addBookPublisher(id, bookId);
        return ResponseEntity.ok(publisherDto);
    }

    /**
     * Método que elimina un libro de un publisher
     *
     * @param bookId id del libro
     * @param id     id del publisher
     * @return ResponseEntity<PublisherDto>
     */
    @Operation(summary = "Elimina un libro de una editorial", description = "Elimina un libro de una editorial")
    @Parameter(name = "id", description = "id de la editorial a actualizar", example = "1", required = true)
    @Parameter(name = "bookId", description = "id del libro a eliminar", example = "1", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Libro eliminado"),
            @ApiResponse(responseCode = "400", description = "Editorial no válida"),
            @ApiResponse(responseCode = "404", description = "Editorial no encontrada"),
    })
    @PatchMapping("/books/remove/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<PublisherDTO> updatePatchBookDelete(@PathVariable Long id, @RequestParam Long bookId) {
        return ResponseEntity.ok(publisherService.removeBookPublisher(id, bookId));
    }

    /**
     * Método que elimina un publisher
     *
     * @param id id del publisher a eliminar
     * @return ResponseEntity<Void>
     */
    @Operation(summary = "elimina una editorial", description = "Elimina una editorial")
    @Parameter(name = "id", description = "id de la editorial a eliminar", example = "1", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Editorial borrada"),
            @ApiResponse(responseCode = "404", description = "Editorial no encontrada"),
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        publisherService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Método para subir una imagen a un Publisher
     *
     * @param id   ID del Publisher
     * @param file Fichero a subir
     * @return ResponseEntity con el código de estado
     * @throws IOException Si no se ha podido subir la imagen
     */
    @Operation(summary = "Actualiza la imagen de una editorial", description = "Actualiza la imagen de una editorial")
    @Parameter(name = "id", description = "id de la editorial a actualizar", example = "1", required = true)
    @Parameter(name = "file", description = "archivo de imagen", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Editorial Actualizada"),
            @ApiResponse(responseCode = "400", description = "Editorial no válida"),
            @ApiResponse(responseCode = "404", description = "Editorial no encontrada"),
    })
    @PatchMapping(value = "/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<PublisherDTO> newPublisherImg(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            return ResponseEntity.ok(publisherService.updateImage(id, file, true));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha enviado una imagen para el Publisher");
        }
    }
}
