package com.nullers.restbookstore.rest.books.controllers;

import com.nullers.restbookstore.pagination.util.PaginationLinksUtils;
import com.nullers.restbookstore.rest.book.controllers.BookRestControllerImpl;
import com.nullers.restbookstore.rest.book.model.Book;
import com.nullers.restbookstore.rest.book.services.BookServiceImpl;
import com.nullers.restbookstore.rest.category.model.Category;
import com.nullers.restbookstore.rest.publisher.model.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(properties = "spring.config.name=application-test")
class BookControllerWithoutMockMvcTest {


    @Mock
    private BookServiceImpl bookService;

    @Mock
    private PaginationLinksUtils paginationLinksUtils = new PaginationLinksUtils();

    @InjectMocks
    private BookRestControllerImpl bookController;


    private Publisher publisher = Publisher.builder()
            .id(1L)
            .name("Madirex")
            .books(Set.of(Book.builder().id(1L).name("asdasd").active(true).description("prueba desc").build()))
            .updatedAt(LocalDateTime.now())
            .createdAt(LocalDateTime.now())
            .build();

    private Category category = Category.builder()
            .id(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5c8"))
            .build();

    private final Book bookTest = Book.builder()
            .id(1L)
            .name("La mansión de las pesadillas")
            .image("https://via.placeholder.com/150")
            .publisher(publisher)
            .category(category)
            .description("prueba desc")
            .price(10.0)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .active(true)
            .build();


    private final Book bookTest2 = Book.builder()
            .id(1L)
            .name("Abre la mente, piensa diferente")
            .image("https://books.madirex.com/favicon.ico")
            .publisher(publisher)
            .category(category)
            .description("prueba desc")
            .price(10.0)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .active(true)
            .build();


    @BeforeEach
    void setUp() {
    }

//    @Test
//    void getAll() {
//        when(bookService.getAllBook(any(), any(), any(),
//                any(PageRequest.class))).thenReturn(new PageImpl(List.of(bookTest, bookTest2)));
//        MockHttpServletRequest requestMock = new MockHttpServletRequest();
//        requestMock.setRequestURI("/books");
//        requestMock.setServerPort(8080);
//
//        var res = bookController.getAllBook(
//                Optional.empty(), Optional.empty(), Optional.empty(),
//                0, 10, "id", "asc",
//                requestMock
//        );
//
//        assertAll(
//                () -> assertEquals(2, Objects.requireNonNull(res.getBody()).content().size()),
//                () -> assertEquals(200, res.getStatusCodeValue())
//        );
//
//        verify(bookService, times(1)).
//                getAllBook(any(), any(), any(), any(PageRequest.class));
//    }

}
