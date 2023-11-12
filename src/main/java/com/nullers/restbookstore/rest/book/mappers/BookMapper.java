package com.nullers.restbookstore.rest.book.mappers;

import com.nullers.restbookstore.NOADD.Publisher;
import com.nullers.restbookstore.rest.book.dto.CreateBookDTO;
import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.dto.UpdateBookDTO;
import com.nullers.restbookstore.rest.book.models.Book;

import java.util.List;

/**
 * Clase BookMapper
 */
public interface BookMapper {
    Book toBook(CreateBookDTO dto, Publisher publisher);

    Book toBook(Book book, UpdateBookDTO dto, Publisher publisher);

    GetBookDTO toGetBookDTO(Book book);

    List<GetBookDTO> toBookList(List<Book> dto);
}
