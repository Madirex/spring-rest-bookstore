package com.nullers.restbookstore.rest.category.controller;

import com.nullers.restbookstore.pagination.models.PageResponse;
import com.nullers.restbookstore.pagination.util.PaginationLinksUtils;
import com.nullers.restbookstore.rest.category.dto.CategoryCreateDTO;
import com.nullers.restbookstore.rest.category.model.Category;
import com.nullers.restbookstore.rest.category.services.CategoryServiceJpa;
import com.nullers.restbookstore.rest.common.PageableRequest;
import com.nullers.restbookstore.rest.common.PageableUtil;
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
    @GetMapping
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
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getCategoryById(id));
    }

    /**
     * Método para crear una categoría
     *
     * @param categoryCreateDTO categoría a crear
     * @return ResponseEntity<Category> con la categoría creada
     */
    @PostMapping
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
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable UUID id, @Valid @RequestBody CategoryCreateDTO categoryCreateDTO) {
        return ResponseEntity.ok(service.updateCategory(id, categoryCreateDTO));
    }

    /**
     * Método para eliminar una categoría
     *
     * @param id id de la categoría
     * @return ResponseEntity<Void>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
