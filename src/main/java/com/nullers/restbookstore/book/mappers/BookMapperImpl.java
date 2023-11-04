package com.nullers.restbookstore.book.mappers;

import com.nullers.restbookstore.NOADD.Publisher;
import com.nullers.restbookstore.book.dto.CreateBookDTO;
import com.nullers.restbookstore.book.dto.GetBookDTO;
import com.nullers.restbookstore.book.dto.UpdateBookDTO;
import com.nullers.restbookstore.book.models.Book;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Clase BookMapper
 *
 * @Author Madirex
 */
@Component
public class BookMapperImpl implements BookMapper {

    /**
     * Mapea un CreateBookDTO en Book
     *
     * @param dto       CreateBookDTO a mapear
     * @param publisher Publisher a mapear
     * @return Book mapeado
     */
    public Book toBook(CreateBookDTO dto, Publisher publisher) {
        return Book.builder()
                .id(UUID.randomUUID())
                .name(dto.getName())
                .publisher(publisher)
                .image(dto.getImage())
                .description(dto.getDescription())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Mapea un UpdateBookDTO en Book
     *
     * @param book      Book a mapear
     * @param dto       UpdateBookDTO a mapear
     * @param publisher Publisher a mapear
     * @return Book mapeado
     */
    public Book toBook(Book book, UpdateBookDTO dto, Publisher publisher) {
        return Book.builder()
                .id(book.getId())
                .name(dto.getName())
                .publisher(publisher)
                .image(dto.getImage())
                .description(dto.getDescription())
                .createdAt(book.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Mapea un Book en GetBookDTO
     *
     * @param book Book a mapear
     * @return GetBookDTO mapeado
     */
    public GetBookDTO toGetBookDTO(Book book) {
        return GetBookDTO.builder()
                .id(book.getId())
                .name(book.getName())
                .publisher(book.getPublisher())
                .image(book.getImage())
                .description(book.getDescription())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }

    /**
     * Mapea una lista de Book en GetBookDTO
     *
     * @param dto Lista de Book a mapear
     * @return Lista de GetBookDTO mapeados
     */
    public List<GetBookDTO> toBookList(List<Book> dto) {
        return dto.stream()
                .map(this::toGetBookDTO)
                .toList();
    }
}
