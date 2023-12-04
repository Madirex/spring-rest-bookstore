package com.nullers.restbookstore.rest.publisher.controller;

import com.nullers.restbookstore.pagination.models.PageResponse;
import com.nullers.restbookstore.pagination.util.PaginationLinksUtils;
import com.nullers.restbookstore.rest.common.PageableRequest;
import com.nullers.restbookstore.rest.common.PageableUtil;
import com.nullers.restbookstore.rest.publisher.dto.CreatePublisherDto;
import com.nullers.restbookstore.rest.publisher.dto.PublisherDTO;
import com.nullers.restbookstore.rest.publisher.services.PublisherServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @GetMapping
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
    @GetMapping("/{id}")
    public ResponseEntity<PublisherDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(publisherService.findById(id));
    }

    /**
     * Método que crea un publisher
     *
     * @param publisherDto publisher a crear
     * @return ResponseEntity<PublisherDto>
     */
    @PostMapping
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
    @PutMapping("/{id}")
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
    @PatchMapping("/books/{id}")
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
    @PatchMapping("/books/remove/{id}")
    public ResponseEntity<PublisherDTO> updatePatchBookDelete(@PathVariable Long id, @RequestParam Long bookId) {
        return ResponseEntity.ok(publisherService.removeBookPublisher(id, bookId));
    }

    /**
     * Método que elimina un publisher
     *
     * @param id id del publisher a eliminar
     * @return ResponseEntity<Void>
     */
    @DeleteMapping("/{id}")
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
    @PatchMapping(value = "/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
