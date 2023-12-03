package com.nullers.restbookstore.rest.category.services;


import com.nullers.restbookstore.rest.book.repository.BookRepository;
import com.nullers.restbookstore.rest.category.dto.CategoryCreateDTO;
import com.nullers.restbookstore.rest.category.exceptions.CategoryConflictException;
import com.nullers.restbookstore.rest.category.exceptions.CategoryNotFoundException;
import com.nullers.restbookstore.rest.category.mappers.CategoryCreateMapper;
import com.nullers.restbookstore.rest.category.model.Category;
import com.nullers.restbookstore.rest.category.repository.CategoryRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Clase CategoryServiceJpaImpl
 */
@Service
@CacheConfig(cacheNames = "categories")
public class CategoryServiceJpaImpl implements CategoryServiceJpa {

    CategoryRepositoryJpa repository;
    private final BookRepository bookRepository;

    /**
     * Constructor
     *
     * @param repository     repositorio de categorías
     * @param bookRepository repositorio de libros
     */
    @Autowired
    public CategoryServiceJpaImpl(CategoryRepositoryJpa repository, BookRepository bookRepository) {
        this.repository = repository;
        this.bookRepository = bookRepository;
    }

    /**
     * Método para obtener todas las categorías
     *
     * @param name     nombre por el que filtrar
     * @param isActive activa o no
     * @param pageable paginación
     * @return categorías
     */
    @Override
    @Cacheable
    public Page<Category> getAll(Optional<String> name, Optional<Boolean> isActive, Pageable pageable) {
        Specification<Category> specName = ((root, query, criteriaBuilder) -> name.map(value -> criteriaBuilder.like(root.get("nombre"), "%" + value + "%")).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))));
        Specification<Category> specActive = ((root, query, criteriaBuilder) -> isActive.map(value -> criteriaBuilder.equal(root.get("activa"), value)).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))));
        Specification<Category> cr = Specification.where(specName).and(specActive);
        return repository.findAll(cr, pageable);
    }

    /**
     * Método para obtener una categoría por su ID
     *
     * @param id id de la categoría
     * @return categoría
     */
    @Override
    @Cacheable(key = "#id")
    public Category getCategoryById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
    }

    /**
     * Método para obtener una categoría por nombre
     *
     * @param name nombre de la categoría
     * @return categoría
     */
    @Override
    @Cacheable(key = "#name")
    public Category getCategoryByName(String name) {
        return repository.findByName(name).orElseThrow(() -> new CategoryNotFoundException(name));
    }

    /**
     * Método para crear una categoría
     *
     * @param categoryCreateDTO datos de la categoría
     * @return categoría creada
     */
    @Cacheable(key = "#result.id")
    public Category createCategory(CategoryCreateDTO categoryCreateDTO) {
        repository.findByName(categoryCreateDTO.getName()).ifPresent(category -> {
            throw new CategoryConflictException("Ya existe una categoría con el nombre: " + categoryCreateDTO.getName());
        });
        return repository.save(CategoryCreateMapper.toEntity(categoryCreateDTO));
    }

    /**
     * Método para actualizar una categoría
     *
     * @param id                id de la categoría
     * @param categoryCreateDTO datos de la categoría
     * @return categoría actualizada
     */
    @Override
    @Cacheable(key = "#id")
    public Category updateCategory(UUID id, CategoryCreateDTO categoryCreateDTO) {
        Category category = repository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        repository.findByName(categoryCreateDTO.getName()).ifPresent(category1 -> {
            if (!category1.getId().equals(id)) {
                throw new CategoryConflictException("Ya existe una categoría con el nombre: " + categoryCreateDTO.getName());
            }
        });
        return repository.save(CategoryCreateMapper.toEntity(categoryCreateDTO, category));
    }

    /**
     * Método para eliminar una categoría
     *
     * @param id id de la categoría
     */
    @Override
    @Cacheable(key = "#id")
    public void deleteById(UUID id) {
        Category category = repository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        if (!bookRepository.findByCategory_Name(category.getName()).isEmpty()) {
            throw new CategoryConflictException("No se puede eliminar la categoría porque tiene libros asociados");
        }
        repository.deleteById(id);
    }
}
