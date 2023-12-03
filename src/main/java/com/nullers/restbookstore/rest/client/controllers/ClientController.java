package com.nullers.restbookstore.rest.client.controllers;

import com.nullers.restbookstore.pagination.models.PageResponse;
import com.nullers.restbookstore.pagination.util.PaginationLinksUtils;
import com.nullers.restbookstore.rest.client.dto.ClientCreateDto;
import com.nullers.restbookstore.rest.client.dto.ClientDto;
import com.nullers.restbookstore.rest.client.dto.ClientUpdateDto;
import com.nullers.restbookstore.rest.client.exceptions.ClientBadRequest;
import com.nullers.restbookstore.rest.client.exceptions.ClientNotFound;
import com.nullers.restbookstore.rest.client.services.ClientServiceImpl;
import com.nullers.restbookstore.rest.common.PageableRequest;
import com.nullers.restbookstore.rest.common.PageableUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controlador de Clientes
 *
 * @author Daniel
 * @see ClientServiceImpl
 * @see PaginationLinksUtils
 * @see ClientCreateDto
 * @see ClientDto
 */
@RestController
@RequestMapping("/api/clients")
@PreAuthorize("hasRole('ADMIN')")
public class ClientController {

    private final ClientServiceImpl clientService;
    private final PaginationLinksUtils paginationLinksUtils;

    private final List<String> contentTypesAllowed = List.of("image/png", "image/jpeg");

    /**
     * Constructor de ClientController
     *
     * @param clientService        servicio de clientes
     * @param paginationLinksUtils utilidades de paginación
     */
    @Autowired
    public ClientController(ClientServiceImpl clientService, PaginationLinksUtils paginationLinksUtils) {
        this.clientService = clientService;
        this.paginationLinksUtils = paginationLinksUtils;
    }


    /**
     * Obtiene todos los clientes
     *
     * @param name            nombre del cliente
     * @param surname         apellido del cliente
     * @param email           email del cliente
     * @param phone           teléfono del cliente
     * @param address         dirección del cliente
     * @param pageableRequest paginación
     * @param request         petición
     * @return ResponseEntity<PageResponse < ClientDto>> con los clientes
     */
    @GetMapping
    public ResponseEntity<PageResponse<ClientDto>> getAll(
            @RequestParam(required = false) Optional<String> name,
            @RequestParam(required = false) Optional<String> surname,
            @RequestParam(required = false) Optional<String> email,
            @RequestParam(required = false) Optional<String> phone,
            @RequestParam(required = false) Optional<String> address,
            @Valid PageableRequest pageableRequest,
            HttpServletRequest request
    ) {
        Pageable pageable = PageRequest.of(pageableRequest.getPage(), pageableRequest.getSize(),
                PageableUtil.getSort(pageableRequest));
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<ClientDto> pageResult = clientService.findAll(name, surname, email, phone, address, pageable);
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, pageableRequest.getOrderBy(), pageableRequest.getOrder()));
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
            throw new ClientBadRequest("El archivo no es una imagen o esta vacío");
        }
    }
}
