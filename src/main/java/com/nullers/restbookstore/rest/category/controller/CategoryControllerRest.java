package com.nullers.restbookstore.rest.category.controller;

import com.nullers.restbookstore.pagination.models.PageResponse;
import com.nullers.restbookstore.pagination.util.PaginationLinksUtils;
import com.nullers.restbookstore.rest.category.dto.CategoryCreateDTO;
import com.nullers.restbookstore.rest.category.model.Category;
import com.nullers.restbookstore.rest.category.services.CategoryServiceJpa;
import com.nullers.restbookstore.rest.common.PageableRequest;
import com.nullers.restbookstore.rest.common.PageableUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;
import java.util.UUID;

/**
 * Clase CategoryControllerRest
 */
@RestController
@RequestMapping("/api/categories")
@PreAuthorize("hasRole('ADMIN')")
public class CategoryControllerRest {

    private final CategoryServiceJpa service;

    private final PaginationLinksUtils paginationLinksUtils;

    /**
     * Constructor
     *
     * @param service              servicio de categoría
     * @param paginationLinksUtils utilidad de paginación
     */
    @Autowired
    public CategoryControllerRest(CategoryServiceJpa service, PaginationLinksUtils paginationLinksUtils) {
        this.service = service;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    /**
     * Método para obtener todas las categorías
     *
     * @param name            nombre por el que filtrar
     * @param isActive        activa o no
     * @param pageableRequest paginación
     * @param request         petición
     * @return ResponseEntity<PageResponse < Category>> con las categorías
     */
    @Operation(summary = "Obtiene todas las categorías", description = "Busca todas las categorías")
    @Parameter(name = "name", description = "Nombre de la categoría", example = "Terror")
    @Parameter(name = "isActive", description = "categoría activa", example = "true")
    @Parameter(name = "page", description = "Número de página", example = "0")
    @Parameter(name = "size", description = "Tamaño de la página", example = "10")
    @Parameter(name = "orderBy", description = "Campo de ordenación", example = "id")
    @Parameter(name = "order", description = "Dirección de ordenación", example = "asc")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Página de categorías"),
            @ApiResponse(responseCode = "400", description = "Petición no válida")
    })
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageResponse<Category>> getCategories(
            @RequestParam(required = false) Optional<String> name,
            @RequestParam(required = false) Optional<Boolean> isActive,
            @Valid PageableRequest pageableRequest,
            HttpServletRequest request
    ) {
        Pageable pageable = PageRequest.of(pageableRequest.getPage(), pageableRequest.getSize(), PageableUtil.getSort(pageableRequest));
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<Category> result = service.getAll(name, isActive, pageable);
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(result, uriBuilder))
                .body(PageResponse.of(result, pageableRequest.getOrderBy(), pageableRequest.getOrder()));
    }

    /**
     * Método para obtener una categoría por ID
     *
     * @param id id de la categoría
     * @return ResponseEntity<Category> con la categoría
     */
    @Operation(summary = "Busca una categoría por ID", description = "Busca una categoría por ID")
    @Parameter(name = "id", description = "Identificador de la categoría", example = "23ebd873-4667-4679-bfc8-9f126cc7b04f", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @ApiResponse(responseCode = "400", description = "ID por el que filtrar no válido"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Category> getCategory(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getCategoryById(id));
    }

    /**
     * Método para crear una categoría
     *
     * @param categoryCreateDTO categoría a crear
     * @return ResponseEntity<Category> con la categoría creada
     */
    @Operation(summary = "Crea una categoría", description = "Crea una categoría")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoría creada"),
            @ApiResponse(responseCode = "400", description = "Categoría no válida"),
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> addCategory(@Valid @RequestBody CategoryCreateDTO categoryCreateDTO) {
        return ResponseEntity.ok(service.createCategory(categoryCreateDTO));
    }

    /**
     * Método para actualizar una categoría
     *
     * @param id                id de la categoría
     * @param categoryCreateDTO categoría a actualizar
     * @return ResponseEntity<Category> con la categoría actualizada
     */
    @Operation(summary = "Actualiza una categoría", description = "Actualiza una categoría")
    @Parameter(name = "id", description = "Identificador de la categoría", example = "23ebd873-4667-4679-bfc8-9f126cc7b04f", required = true)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Categoría a actualizar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada"),
            @ApiResponse(responseCode = "400", description = "Categoría no válida"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> updateCategory(@PathVariable UUID id, @Valid @RequestBody CategoryCreateDTO categoryCreateDTO) {
        return ResponseEntity.ok(service.updateCategory(id, categoryCreateDTO));
    }

    /**
     * Método para eliminar una categoría
     *
     * @param id id de la categoría
     * @return ResponseEntity<Void>
     */
    @Operation(summary = "Borra una categoría", description = "Borra una categoría")
    @Parameter(name = "id", description = "Identificador de la categoría", example = "23ebd873-4667-4679-bfc8-9f126cc7b04f", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoría borrada"),
            @ApiResponse(responseCode = "400", description = "Identificador de categoría no válido"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
