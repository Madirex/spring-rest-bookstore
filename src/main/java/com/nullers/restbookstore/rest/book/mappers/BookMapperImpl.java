package com.nullers.restbookstore.rest.book.mappers;

import com.nullers.restbookstore.rest.book.dto.CreateBookDTO;
import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.dto.UpdateBookDTO;
import com.nullers.restbookstore.rest.book.model.Book;
import com.nullers.restbookstore.rest.category.model.Categoria;
import com.nullers.restbookstore.rest.publisher.dto.PublisherData;
import com.nullers.restbookstore.rest.publisher.model.Publisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

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
                .name(dto.getName())
                .publisher(publisher)
                .price(dto.getPrice())
                .image(dto.getImage())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .active(true)
                .stock(dto.getStock())
                .build();
    }

    /**
     * Mapea un GetBookDTO en Book
     * @param dto GetBookDTO a mapear
     * @return Book mapeado
     */

    public Book toBook(GetBookDTO dto) {
        return Book.builder()
                .id(dto.getId())
                .name(dto.getName())
                .price(dto.getPrice())
                .image(dto.getImage())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .active(true)
                .stock(dto.getStock())
                .build();
    }

    /**
     * Mapea un CreateBookDTO en Book
     * @param dto CreateBookDTO a mapear
     * @param publisher Publisher a mapear
     * @param category Categoría a mapear
     * @return Book mapeado
     */
    public Book toBook(CreateBookDTO dto, Publisher publisher, Categoria category) {
        return Book.builder()
                .name(dto.getName())
                .publisher(publisher)
                .price(dto.getPrice())
                .image(dto.getImage())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .active(true)
                .category(category)
                .stock(dto.getStock())
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
                .price(dto.getPrice())
                .image(dto.getImage())
                .description(dto.getDescription())
                .createdAt(book.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .active(book.getActive())
                .stock(dto.getStock())
                .build();
    }

    /**
     * Mapea un UpdateBookDTO en Book
     * @param book Book a mapear
     * @param dto UpdateBookDTO a mapear
     * @param publisher Publisher a mapear
     * @param category Categoría a mapear
     * @return Book mapeado
     */
    public Book toBook(Book book, UpdateBookDTO dto, Publisher publisher, Categoria category) {
        return Book.builder()
                .id(book.getId())
                .name(dto.getName())
                .publisher(publisher)
                .price(dto.getPrice())
                .image(dto.getImage())
                .description(dto.getDescription())
                .createdAt(book.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .active(book.getActive())
                .category(category)
                .stock(dto.getStock())
                .build();
    }

    /**
     * Mapea un Book en GetBookDTO
     *
     * @param book Book a mapear
     * @return GetBookDTO mapeado
     */
    public GetBookDTO toGetBookDTO(Book book, PublisherData publisherData) {
        return GetBookDTO.builder()
                .id(book.getId())
                .name(book.getName())
                .publisher(publisherData)
                .price(book.getPrice())
                .image(book.getImage())
                .description(book.getDescription())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .active(book.getActive())
                .category(book.getCategory().getNombre())
                .stock(book.getStock())
                .build();
    }

    /**
     * Mapea un Book en GetBookDTO
     * @param book Book a mapear
     * @return GetBookDTO mapeado
     */
    public GetBookDTO toGetBookDTO(Book book) {
        return GetBookDTO.builder()
                .id(book.getId())
                .name(book.getName())
                .price(book.getPrice())
                .image(book.getImage())
                .description(book.getDescription())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .active(book.getActive())
                .category(book.getCategory().getNombre())
                .stock(book.getStock())
                .build();
    }

    /**
     * Mapea una lista de Book en GetBookDTO
     *
     * @param books         Lista de Book a mapear
     * @param publisherData PublisherData
     * @return Lista de GetBookDTO mapeados
     */
    public List<GetBookDTO> toBookList(List<Book> books, List<PublisherData> publisherData) {
        return IntStream.range(0, books.size())
                .mapToObj(index -> toGetBookDTO(books.get(index), publisherData.get(index)))
                .toList();
    }

}
