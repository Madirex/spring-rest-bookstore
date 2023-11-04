package com.nullers.restbookstore.book.repositories;

import com.nullers.restbookstore.book.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Interface BookRepository
 */
@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {

}
