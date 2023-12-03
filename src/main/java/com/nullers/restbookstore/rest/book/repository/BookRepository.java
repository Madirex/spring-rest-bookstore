package com.nullers.restbookstore.rest.book.repository;

import com.nullers.restbookstore.rest.book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface BookRepository
 *
 * @Author Madirex
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    /**
     * Busca libros que contengan una categoría
     *
     * @param category nombre de la categoría
     * @return lista de libros
     */
    List<Book> findByCategory_Name(String category);
}
