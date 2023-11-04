package com.nullers.restbookstore.book.mappers;

import com.nullers.restbookstore.NOADD.Publisher;
import com.nullers.restbookstore.book.dto.CreateBookDTO;
import com.nullers.restbookstore.book.dto.GetBookDTO;
import com.nullers.restbookstore.book.dto.UpdateBookDTO;
import com.nullers.restbookstore.book.models.Book;

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
