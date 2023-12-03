package com.nullers.restbookstore.rest.category.services;


import com.nullers.restbookstore.rest.book.repository.BookRepository;
import com.nullers.restbookstore.rest.category.dto.CategoriaCreateDto;
import com.nullers.restbookstore.rest.category.exceptions.CategoriaConflictException;
import com.nullers.restbookstore.rest.category.exceptions.CategoriaNotFoundException;
import com.nullers.restbookstore.rest.category.mappers.CategoriaCreateMapper;
import com.nullers.restbookstore.rest.category.model.Category;
import com.nullers.restbookstore.rest.category.repository.CategoriasRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@CacheConfig(cacheNames = "categories")
public class CategoriaServiceJpaImpl implements CategoriaServiceJpa {

    CategoriasRepositoryJpa repository;
    private final BookRepository bookRepository;

    @Autowired
    public CategoriaServiceJpaImpl(CategoriasRepositoryJpa repository, BookRepository bookRepository) {
        this.repository = repository;
        this.bookRepository = bookRepository;
    }

    @Override
    @Cacheable
    public Page<Category> getAll(Optional<String> name, Optional<Boolean> isActive, Pageable pageable) {
        Specification<Category> specName = (((root, query, criteriaBuilder) -> name.map(value -> criteriaBuilder.like(root.get("nombre"), "%" + value + "%")).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)))));

        Specification<Category> specActive = (((root, query, criteriaBuilder) -> isActive.map(value -> criteriaBuilder.equal(root.get("activa"), value)).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)))));

        Specification<Category> criterio = Specification.where(specName).and(specActive);
        return repository.findAll(criterio, pageable);
    }

    @Override
    @Cacheable(key = "#id")
    public Category getCategoriaById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new CategoriaNotFoundException(id));
    }

    @Override
    @Cacheable(key = "#name")
    public Category getCategoriaByName(String name) {
        return repository.findByName(name).orElseThrow(() -> new CategoriaNotFoundException(name));
    }

    @Cacheable(key = "#result.id")
    public Category createCategoria(CategoriaCreateDto categoriaCreateDto) {
        repository.findByName(categoriaCreateDto.getName()).ifPresent(categoria -> {
            throw new CategoriaConflictException("Ya existe una categoria con el nombre: " + categoriaCreateDto.getName());
        });
        return repository.save(CategoriaCreateMapper.toEntity(categoriaCreateDto));
    }

    @Override
    @Cacheable(key = "#id")
    public Category updateCategoria(UUID id, CategoriaCreateDto categoriaCreateDto) {
        Category category = repository.findById(id).orElseThrow(() -> new CategoriaNotFoundException(id));
        repository.findByName(categoriaCreateDto.getName()).ifPresent(categoria1 -> {
            System.out.println(category.getName() + " " + category.getId());
            System.out.println(categoria1.getName() + " " + categoria1.getId());
            if (!categoria1.getId().equals(id)) {
                throw new CategoriaConflictException("Ya existe una categoria con el nombre: " + categoriaCreateDto.getName());
            }
        });
        return repository.save(CategoriaCreateMapper.toEntity(categoriaCreateDto, category));
    }


    @Override
    @Cacheable(key = "#id")
    public void deleteById(UUID id) {
        Category category = repository.findById(id).orElseThrow(() -> new CategoriaNotFoundException(id));
        if (!bookRepository.findByCategory_Name(category.getName()).isEmpty()) {
            throw new CategoriaConflictException("No se puede eliminar la categoría porque tiene libros asociados");
        }
        repository.deleteById(id);

    }

}
