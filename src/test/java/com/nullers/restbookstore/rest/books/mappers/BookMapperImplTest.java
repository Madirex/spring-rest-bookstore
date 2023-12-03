package com.nullers.restbookstore.rest.books.mappers;

import com.nullers.restbookstore.rest.book.dto.CreateBookDTO;
import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.dto.UpdateBookDTO;
import com.nullers.restbookstore.rest.book.mappers.BookMapperImpl;
import com.nullers.restbookstore.rest.book.mappers.BookNotificationMapper;
import com.nullers.restbookstore.rest.book.model.Book;
import com.nullers.restbookstore.rest.category.model.Category;
import com.nullers.restbookstore.rest.publisher.dto.PublisherData;
import com.nullers.restbookstore.rest.publisher.model.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Clase BookMapperImplTest
 */
class BookMapperImplTest {
    private BookMapperImpl bookMapperImpl;
    private BookNotificationMapper bookNotificationMapper;

    /**
     * Método setUp para inicializar los objetos
     */
    @BeforeEach
    void setUp() {
        bookMapperImpl = new BookMapperImpl();
        bookNotificationMapper = new BookNotificationMapper();
    }

    /**
     * Test para comprobar que el mapeo de CreateBook a Book es correcto
     */
    @Test
    void testCreateBookDTOToBook() {
        var book = CreateBookDTO.builder()
                .name("nombre")
                .publisherId(1L)
                .price(2.2)
                .image("imagen")
                .description("descripción")
                .build();
        var mapped = bookMapperImpl.toBook(book, Publisher.builder().id(1L).build());
        assertAll("Book properties",
                () -> assertEquals(book.getName(), mapped.getName(), "El nombre debe coincidir"),
                () -> assertEquals(book.getPublisherId(), mapped.getPublisher().getId(),
                        "El Publisher debe coincidir"),
                () -> assertEquals(book.getPrice(), mapped.getPrice(), "El precio debe coincidir"),
                () -> assertEquals(book.getImage(), mapped.getImage(), "La imagen debe coincidir"),
                () -> assertEquals(book.getDescription(), mapped.getDescription(), "La descripción debe coincidir")
        );
    }

    /**
     * Test para comprobar que el mapeo de un UpdateBook a DTO es correcto
     */
    @Test
    void testUpdateBookDTOToBook() {
        var bookToEdit = Book.builder()
                .id(1L)
                .name("nombre")
                .publisher(Publisher.builder().id(1L).build())
                .price(2.2)
                .image("imagen")
                .description("descripción")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .active(true)
                .build();
        var book = UpdateBookDTO.builder()
                .name("nombre")
                .publisherId(1L)
                .price(2.2)
                .image("imagen")
                .description("descripción")
                .build();
        var mapped = bookMapperImpl.toBook(bookToEdit, book, Publisher.builder().id(1L).build());
        assertAll("Book properties",
                () -> assertNotNull(mapped.getId(), "El ID no debe ser nulo"),
                () -> assertEquals(book.getName(), mapped.getName(), "El nombre debe coincidir"),
                () -> assertEquals(book.getPublisherId(), mapped.getPublisher().getId(), "El Publisher debe coincidir"),
                () -> assertEquals(book.getPrice(), mapped.getPrice(), "El precio debe coincidir"),
                () -> assertEquals(book.getImage(), mapped.getImage(), "La imagen debe coincidir"),
                () -> assertEquals(book.getDescription(), mapped.getDescription(), "La descripción debe coincidir")
        );
    }

    /**
     * Test para comprobar que el mapeo de un Book a DTO es correcto
     */
    @Test
    void testToGetBookDTO() {
        var book = Book.builder()
                .id(1L)
                .name("nombre")
                .publisher(Publisher.builder().id(1L).build())
                .price(2.2)
                .image("imagen")
                .description("descripción")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .active(true)
                .category(Category.builder().name("test").build())
                .build();
        var mapped = bookMapperImpl.toGetBookDTO(book, PublisherData.builder().id(1L).build());
        assertAll("Book properties",
                () -> assertNotNull(mapped.getId(), "El ID no debe ser nulo"),
                () -> assertEquals(book.getName(), mapped.getName(), "El nombre debe coincidir"),
                () -> assertEquals(book.getPublisher().getId(), mapped.getPublisher().getId(), "El Publisher debe coincidir"),
                () -> assertEquals(book.getPrice(), mapped.getPrice(), "El precio debe coincidir"),
                () -> assertEquals(book.getImage(), mapped.getImage(), "La imagen debe coincidir"),
                () -> assertEquals(book.getDescription(), mapped.getDescription(), "La descripción debe coincidir"));
    }

    /**
     * Test para comprobar que el mapeo de un Book a DTO es correcto
     */
    @Test
    void bookToBookDTO() {
        var book = Book.builder()
                .id(1L)
                .name("nombre")
                .publisher(Publisher.builder().id(1L).build())
                .price(2.2)
                .image("imagen")
                .description("descripción")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .active(true)
                .category(Category.builder().name("test").name("dsfsdf").build())
                .build();
        var mapped = bookMapperImpl.toGetBookDTO(book);
        assertAll("Book properties",
                () -> assertEquals(book.getName(), mapped.getName(), "El nombre debe coincidir"),
                () -> assertEquals(book.getPrice(), mapped.getPrice(), "El precio debe coincidir"),
                () -> assertEquals(book.getImage(), mapped.getImage(), "La imagen debe coincidir"),
                () -> assertEquals(book.getDescription(), mapped.getDescription(), "La descripción debe coincidir"));
    }

    /**
     * Test para comprobar que el mapeo de un UpdateBook a DTO es correcto
     */
    @Test
    void updateBookToBookDTO() {
        var book = Book.builder()
                .id(1L)
                .name("nombre")
                .publisher(Publisher.builder().id(1L).build())
                .price(2.2)
                .image("imagen")
                .description("descripción")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .active(true)
                .category(Category.builder().name("test").build())
                .build();
        var updatedBook = UpdateBookDTO.builder()
                .name("nombre")
                .publisherId(1L)
                .price(2.2)
                .image("imagen")
                .description("descripción")
                .build();
        var mapped = bookMapperImpl.toBook(book, updatedBook, Publisher.builder().id(1L).build(), Category.builder()
                .build());
        assertAll("Book properties",
                () -> assertNotNull(mapped.getId(), "El ID no debe ser nulo"),
                () -> assertEquals(book.getName(), mapped.getName(), "El nombre debe coincidir"),
                () -> assertEquals(book.getPublisher().getId(), mapped.getPublisher().getId(), "El Publisher debe coincidir"),
                () -> assertEquals(book.getPrice(), mapped.getPrice(), "El precio debe coincidir"),
                () -> assertEquals(book.getImage(), mapped.getImage(), "La imagen debe coincidir"),
                () -> assertEquals(book.getDescription(), mapped.getDescription(), "La descripción debe coincidir"));
    }

    /**
     * Test para comprobar que el mapeo de un CreateBook a DTO es correcto
     */
    @Test
    void createBookToBookDTO() {
        var book = CreateBookDTO.builder()
                .category(UUID.randomUUID().toString())
                .name("nombre")
                .price(2.2)
                .publisherId(1L)
                .image("imagen")
                .description("descripción")
                .build();
        var mapped = bookMapperImpl.toBook(book, Publisher.builder().id(1L).build(), Category.builder().build());
        assertAll("Book properties",
                () -> assertEquals(book.getName(), mapped.getName(), "El nombre debe coincidir"),
                () -> assertEquals(book.getPrice(), mapped.getPrice(), "El precio debe coincidir"),
                () -> assertEquals(book.getImage(), mapped.getImage(), "La imagen debe coincidir"),
                () -> assertEquals(book.getDescription(), mapped.getDescription(), "La descripción debe coincidir"));
    }

    /**
     * Test para comprobar que el mapeo de GetBookDTO se realiza a Book
     */
    @Test
    void getBookDTOToBook() {
        var book = GetBookDTO.builder()
                .name("nombre")
                .price(2.2)
                .image("imagen")
                .description("descripción")
                .category(UUID.randomUUID().toString())
                .publisher(PublisherData.builder().id(1L).build())
                .build();
        var mapped = bookMapperImpl.toBook(book);
        assertAll("Book properties",
                () -> assertEquals(book.getName(), mapped.getName(), "El nombre debe coincidir"),
                () -> assertEquals(book.getPrice(), mapped.getPrice(), "El precio debe coincidir"),
                () -> assertEquals(book.getImage(), mapped.getImage(), "La imagen debe coincidir"),
                () -> assertEquals(book.getDescription(), mapped.getDescription(), "La descripción debe coincidir"));
    }

    /**
     * Test para comprobar mapeo a GetBookDTOList
     */
    @Test
    void testToGetBookDTOList() {
        List<Book> list = new ArrayList<>();
        list.add(Book.builder()
                .id(1L)
                .name("nombre")
                .publisher(Publisher.builder().id(1L).build())
                .price(2.2)
                .image("imagen")
                .description("descripción")
                .category(Category.builder().name("test").build())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .active(true)
                .build());
        var mapped = bookMapperImpl.toBookList(list, List.of(PublisherData.builder().id(1L).build()));
        assertAll("Book properties",
                () -> assertNotNull(mapped.get(0).getId(), "El ID no debe ser nulo"),
                () -> assertEquals(1, mapped.size(), "La lista debe contener 1 elemento"),
                () -> assertEquals(list.get(0).getName(), mapped.get(0).getName(), "El nombre debe coincidir"),
                () -> assertEquals(list.get(0).getPublisher().getId(), mapped.get(0).getPublisher().getId(),
                        "El Publisher debe coincidir"),
                () -> assertEquals(list.get(0).getPrice(), mapped.get(0).getPrice(), "El precio debe coincidir"),
                () -> assertEquals(list.get(0).getImage(), mapped.get(0).getImage(), "La imagen debe coincidir"),
                () -> assertEquals(list.get(0).getDescription(), mapped.get(0).getDescription(),
                        "La descripción debe coincidir"));
    }

    /**
     * Test para comprobar mapeo a BookNotificationDTO
     */
    @Test
    void testToBookNotificationDTO() {
        var book = GetBookDTO.builder()
                .id(1L)
                .name("nombre")
                .publisher(PublisherData.builder().id(1L).build())
                .price(2.2)
                .image("imagen")
                .description("descripción")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .active(true)
                .build();
        var mapped = bookNotificationMapper.toBookNotificationDto(book);
        assertAll("Book properties",
                () -> assertNotNull(mapped.id(), "El ID no debe ser nulo"),
                () -> assertEquals(book.getName(), mapped.name(), "El nombre debe coincidir"),
                () -> assertEquals(book.getPublisher().toString(), mapped.publisher(), "El Publisher debe coincidir"),
                () -> assertEquals(book.getPrice(), mapped.price(), "El precio debe coincidir"),
                () -> assertEquals(book.getImage(), mapped.image(), "La imagen debe coincidir"),
                () -> assertEquals(book.getDescription(), mapped.description(), "La descripción debe coincidir"));
    }

}
