package com.nullers.restbookstore.rest.category.services;


import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.services.BookServiceImpl;
import com.nullers.restbookstore.rest.category.dto.CategoriaCreateDto;
import com.nullers.restbookstore.rest.category.exceptions.CategoriaConflictException;
import com.nullers.restbookstore.rest.category.exceptions.CategoriaNotFoundException;
import com.nullers.restbookstore.rest.category.mappers.CategoriaCreateMapper;
import com.nullers.restbookstore.rest.category.models.Categoria;
import com.nullers.restbookstore.rest.category.repository.CategoriasRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@CacheConfig(cacheNames = "categories")
public class CategoriaServiceJpaImpl implements CategoriaServiceJpa{

    CategoriasRepositoryJpa repository;

    BookServiceImpl bookService;

    @Autowired
    public CategoriaServiceJpaImpl(CategoriasRepositoryJpa repository, BookServiceImpl bookService) {
        this.repository = repository;
        this.bookService = bookService;
    }

    @Override
    @Cacheable
    public Page<Categoria> getAll(Optional<String> nombre, Optional<Boolean> activa, Pageable pageable){
        Specification<Categoria> specName = (((root, query, criteriaBuilder) -> nombre.map(value -> criteriaBuilder.like(root.get("nombre"), "%" + value + "%")).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)))));

        Specification<Categoria> specActive = (((root, query, criteriaBuilder) -> activa.map(value -> criteriaBuilder.equal(root.get("activa"), value)).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)))));

        Specification<Categoria> criterio = Specification.where(specName).and(specActive);
        return repository.findAll(criterio, pageable);
    }

    @Override
    @Cacheable(key = "#id")
    public Categoria getCategoriaById(UUID id) {
    return repository.findById(id).orElseThrow(() -> new CategoriaNotFoundException(id));
    }

    @Override
    @Cacheable(key = "#nombre")
    public Categoria getCategoriaByNombre(String nombre) {
        return repository.findByNombre(nombre).orElseThrow(() -> new CategoriaNotFoundException(nombre));
    }

    @Cacheable(key = "#result.id")
    public Categoria createCategoria(CategoriaCreateDto categoriaCreateDto) {
        repository.findByNombre(categoriaCreateDto.getNombre()).ifPresent(categoria -> {
            throw new CategoriaConflictException("Ya existe una categoria con el nombre: " + categoriaCreateDto.getNombre());
        });
        return repository.save(CategoriaCreateMapper.toEntity(categoriaCreateDto));
    }

    @Override
    @Cacheable(key = "#id")
    public Categoria updateCategoria(UUID id, CategoriaCreateDto categoriaCreateDto) {
        Categoria categoria = repository.findById(id).orElseThrow(() -> new CategoriaNotFoundException(id));
        repository.findByNombre(categoriaCreateDto.getNombre()).ifPresent(categoria1 -> {
            System.out.println(categoria.getNombre() + " " + categoria.getId());
            System.out.println(categoria1.getNombre() + " " + categoria1.getId());
            if (!categoria1.getId().equals(id)) {
                throw new CategoriaConflictException("Ya existe una categoria con el nombre: " + categoriaCreateDto.getNombre());
            }
        });
        return repository.save(CategoriaCreateMapper.toEntity(categoriaCreateDto, categoria));
    }


    @Override
    @Cacheable(key = "#id")
    public void deleteById(UUID id) {
        Categoria categoria = repository.findById(id).orElseThrow(() -> new CategoriaNotFoundException(id));
        Page<GetBookDTO> book =  bookService.getAllBook(Optional.empty(), Optional.empty(), Optional.of(categoria.getNombre()), PageRequest.of(0, 10000));
        if(!book.getContent().isEmpty()){
            throw new CategoriaConflictException("No se puede eliminar la categoria porque tiene libros asociados");
        }
        repository.deleteById(id);

    }

}
