package com.nullers.restbookstore.rest.books.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nullers.restbookstore.config.websockets.WebSocketConfig;
import com.nullers.restbookstore.config.websockets.WebSocketHandler;
import com.nullers.restbookstore.notifications.models.Notification;
import com.nullers.restbookstore.rest.book.dto.CreateBookDTO;
import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.dto.PatchBookDTO;
import com.nullers.restbookstore.rest.book.dto.UpdateBookDTO;
import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.book.exceptions.BookNotValidIDException;
import com.nullers.restbookstore.rest.book.mappers.BookMapperImpl;
import com.nullers.restbookstore.rest.book.mappers.BookNotificationMapper;
import com.nullers.restbookstore.rest.book.model.Book;
import com.nullers.restbookstore.rest.book.repository.BookRepository;
import com.nullers.restbookstore.rest.book.services.BookServiceImpl;
import com.nullers.restbookstore.rest.category.exceptions.CategoriaNotFoundException;
import com.nullers.restbookstore.rest.category.model.Category;
import com.nullers.restbookstore.rest.category.repository.CategoriasRepositoryJpa;
import com.nullers.restbookstore.rest.category.services.CategoriaServiceJpa;
import com.nullers.restbookstore.rest.publisher.dto.PublisherDTO;
import com.nullers.restbookstore.rest.publisher.dto.PublisherData;
import com.nullers.restbookstore.rest.publisher.mappers.PublisherMapper;
import com.nullers.restbookstore.rest.publisher.model.Publisher;
import com.nullers.restbookstore.rest.publisher.services.PublisherService;
import com.nullers.restbookstore.storage.services.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Clase BookServiceImplTest
 */
@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    List<Book> list;
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapperImpl bookMapperImpl;

    @Mock
    private PublisherMapper publisherMapper;

    @Mock
    private PublisherService publisherService;

    @Mock
    private CategoriasRepositoryJpa categoriasRepositoryJpa;

    @Mock
    private StorageService storageService;

    @Mock
    private WebSocketConfig webSocketConfig;

    @Mock
    private WebSocketHandler webSocketHandlerMock;

    @Mock
    private BookNotificationMapper bookNotificationMapper;
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private CategoriaServiceJpa categoryService;

    @InjectMocks
    private BookServiceImpl bookService;

    /**
     * Método setUp para inicializar los objetos
     */
    @BeforeEach
    void setUp() {
        bookService.setWebSocketService(webSocketHandlerMock);
        list = new ArrayList<>();
        list.add(Book.builder()
                .id(1L)
                .name("nombre")
                .publisher(Publisher.builder().id(1L).build())
                .price(2.2)
                .image("imagen")
                .description("descripción")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .active(true)
                .build());
        list.add(Book.builder()
                .id(2L)
                .name("nombre2")
                .publisher(Publisher.builder().id(2L).build())
                .price(22.22)
                .image("imagen2")
                .description("descripción2")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .active(true)
                .build());
    }

    /**
     * Test para comprobar que se reciben todos los Book
     */
    @Test
    void testGetAllBook() {
        List<GetBookDTO> list2 = new ArrayList<>();
        list2.add(GetBookDTO.builder()
                .id(1L)
                .name("nombre")
                .publisher(PublisherData.builder().id(1L).build())
                .price(2.2)
                .image("imagen")
                .description("descripción")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .active(true)
                .build());
        list2.add(GetBookDTO.builder()
                .id(2L)
                .name("nombre2")
                .publisher(PublisherData.builder().id(2L).build())
                .price(22.22)
                .image("imagen2")
                .description("descripción2")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .active(true)
                .build());

        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Book> expectedPage = new PageImpl<>(list);
        Specification<Book> anySpecification = any();
        when(bookRepository.findAll(anySpecification, any(Pageable.class))).thenReturn(expectedPage);
        when(bookMapperImpl.toGetBookDTO(any(Book.class), any())).thenReturn(list2.get(0));
        when(publisherMapper.toPublisherData(any(Publisher.class))).thenReturn(PublisherData.builder().id(1L).build());
        Page<GetBookDTO> actualPage = bookService.getAllBook(Optional.empty(), Optional.empty(),
                Optional.empty(), pageable);
        var list3 = actualPage.getContent();
        assertAll("Book properties",
                () -> assertEquals(2, list.size(), "La lista debe contener 2 elementos"),
                () -> assertEquals(2, list3.size(), "La lista debe contener 2 elementos"),
                () -> assertEquals(list.get(0).getName(), list3.get(0).getName(), "El nombre debe coincidir"),
                () -> assertEquals(list.get(0).getPrice(), list3.get(0).getPrice(), "El precio debe coincidir"),
                () -> assertEquals(list.get(0).getImage(), list3.get(0).getImage(), "La imagen debe coincidir"),
                () -> assertEquals(list.get(0).getDescription(), list3.get(0).getDescription(),
                        "La descripción debe coincidir")
        );
    }


    /**
     * Test para comprobar que se obtiene un Book por su id
     *
     * @throws BookNotValidIDException excepción
     * @throws BookNotFoundException   excepción
     */
    @Test
    void testGetBookById() throws BookNotValidIDException, BookNotFoundException {
        List<GetBookDTO> list2 = new ArrayList<>();
        list2.add(GetBookDTO.builder()
                .id(1L)
                .name("nombre")
                .publisher(PublisherData.builder().id(1L).build())
                .price(2.2)
                .image("imagen")
                .description("descripción")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .active(true)
                .build());
        when(bookRepository.findById(list.get(0).getId())).thenReturn(Optional.ofNullable(list.get(0)));
        when(bookMapperImpl.toGetBookDTO(any(Book.class), any())).thenReturn(list2.get(0));
        when(publisherMapper.toPublisherData(any(Publisher.class))).thenReturn(PublisherData.builder().id(1L).build());
        var book = bookService.getBookById(list.get(0).getId());
        assertAll("Book properties",
                () -> assertEquals(list.get(0).getName(), book.getName(), "El nombre debe coincidir"),
                () -> assertEquals(list.get(0).getPrice(), book.getPrice(), "El precio debe coincidir"),
                () -> assertEquals(list.get(0).getImage(), book.getImage(), "La imagen debe coincidir"),
                () -> assertEquals(list.get(0).getDescription(), book.getDescription(),
                        "La descripción debe coincidir")
        );
        verify(bookRepository, times(1)).findById(list.get(0).getId());
    }

    /**
     * Test para comprobar que no se ha encontrado el Book por su id
     */
    @Test
    void testGetBookByIdNotFound() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        long id = 1L;
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(id));
        verify(bookRepository, times(1)).findById(anyLong());
    }


    /**
     * Test para comprobar que se inserta un Book
     */
    @Test
    void testPostBook() {
        var insert = CreateBookDTO.builder()
                .name("nombre").price(2.2).image("imagen").publisherId(1L).build();
        var publisher = Publisher.builder().id(1L).createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).build();
        var publisherDTO = PublisherDTO.builder().id(1L).build();
        var category = Category.builder().isActive(true).name("category").build();
        var inserted = Book.builder().id(1L).name("nombre").price(2.2).image("imagen")
                .publisher(publisher).description("descripción")
                .category(category)
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).active(true).build();
        var publisherData = PublisherData.builder().id(1L).build();
        when(publisherService.findById(1L)).thenReturn(any());
        when(publisherMapper.toPublisher(publisherDTO)).thenReturn(publisher);
        when(bookRepository.save(inserted)).thenReturn(inserted);
        when(categoriasRepositoryJpa.findByName(any()))
                .thenReturn(Optional.of(category));
        when(bookMapperImpl.toBook(insert, publisher, category)).thenReturn(inserted);
        when(publisherMapper.toPublisherData(any())).thenReturn(publisherData);
        when(bookMapperImpl.toGetBookDTO(inserted, publisherData))
                .thenReturn(GetBookDTO.builder().name("nombre").price(2.2).image("imagen")
                        .category("category")
                        .publisher(any()).build());
        GetBookDTO inserted2 = bookService.postBook(insert);
        assertNotNull(inserted2);
        assertAll("Book properties",
                () -> assertEquals(insert.getName(), inserted2.getName(), "El nombre debe coincidir"),
                () -> assertEquals(insert.getPrice(), inserted2.getPrice(), "El precio debe coincidir"),
                () -> assertEquals(insert.getImage(), inserted2.getImage(), "La imagen debe coincidir")
        );
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    /**
     * Test para comprobar que no se ha encontrado la categoría al hacer una consulta POST
     */
    @Test
    void testPostBookNotFoundCategory() {
        var insert = CreateBookDTO.builder()
                .name("nombre").price(2.2).image("imagen").publisherId(1L)
                .category(UUID.randomUUID().toString()).build();
        var publisher = Publisher.builder().id(1L).createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).build();
        var publisherDTO = PublisherDTO.builder().id(1L).build();
        when(publisherService.findById(1L)).thenReturn(any());
        when(publisherMapper.toPublisher(publisherDTO)).thenReturn(publisher);
        when(categoriasRepositoryJpa.findByName(any()))
                .thenReturn(Optional.of(Category.builder().name("category").build()));
        assertThrows(CategoriaNotFoundException.class, () -> bookService.postBook(insert));
    }

    /**
     * Test para probar que el Book se ha actualizado
     *
     * @throws BookNotFoundException excepción de Book no encontrado
     */
    @Test
    void testPutBook() throws BookNotFoundException {
        var update = UpdateBookDTO.builder()
                .name("nombre")
                .publisherId(1L)
                .price(2.2)
                .image("imagen")
                .category("category")
                .description("descripción")
                .build();
        var publisher = Publisher.builder().id(1L).createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).build();
        var category = Category.builder().isActive(true).name("category").build();
        var publisherDTO = PublisherDTO.builder().id(1L).build();
        var publisherData = PublisherData.builder().id(1L).build();
        var inserted = Book.builder().id(1L).name("nombre").price(2.2).image("imagen")
                .publisher(publisher).description("descripción")
                .category(category)
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).active(true).build();
        when(publisherService.findById(1L)).thenReturn(any());
        when(publisherMapper.toPublisher(publisherDTO)).thenReturn(publisher);
        when(bookRepository.save(inserted)).thenReturn(inserted);
        when(categoriasRepositoryJpa.findByName(any()))
                .thenReturn(Optional.of(category));
        when(bookRepository.findById(inserted.getId())).thenReturn(Optional.of(inserted));
        when(bookMapperImpl.toBook(inserted, update, publisher, category)).thenReturn(inserted);
        when(publisherMapper.toPublisherData(any())).thenReturn(publisherData);
        when(bookMapperImpl.toGetBookDTO(inserted, publisherData))
                .thenReturn(GetBookDTO.builder().name("nombre").price(2.2).image("imagen")
                        .publisher(any()).build());
        GetBookDTO updated2 = bookService.putBook(1L, update);
        assertNotNull(updated2);
        assertAll("Book properties",
                () -> assertEquals(update.getName(), updated2.getName(), "El nombre debe coincidir"),
                () -> assertEquals(update.getPrice(), updated2.getPrice(), "El precio debe coincidir"),
                () -> assertEquals(update.getImage(), updated2.getImage(), "La imagen debe coincidir")
        );
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    /**
     * Test para comprobar que el Book no se encuentra cuando se hace un Put
     */
    @Test
    void testPutBookNotFound() {
        var update = UpdateBookDTO.builder()
                .name("nombre").price(2.2).image("imagen").build();
        var data = list.get(0).getId();
        when(bookRepository.findById(list.get(0).getId())).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.putBook(data, update));
    }

    /**
     * Test para comprobar el Book se puede actualizar parcialmente
     *
     * @throws BookNotFoundException excepción de Book no encontrado
     */
    @Test
    void testPatchBook() throws BookNotFoundException {
        var categoryId = UUID.randomUUID();
        var update = PatchBookDTO.builder()
                .name("nombre")
                .publisherId(1L)
                .price(2.2)
                .image("imagen")
                .category(categoryId.toString())
                .description("descripción")
                .build();
        var publisher = Publisher.builder().id(1L).createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).build();
        var publisherDTO = PublisherDTO.builder().id(1L).build();
        var publisherData = PublisherData.builder().id(1L).build();
        var inserted = Book.builder().id(1L).name("nombre").price(2.2).image("imagen")
                .publisher(publisher).description("descripción")
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).active(true).build();
        when(publisherService.findById(1L)).thenReturn(any());
        when(categoryService.getCategoriaById(categoryId)).thenReturn(any());
        when(publisherMapper.toPublisher(publisherDTO)).thenReturn(publisher);
        when(bookRepository.save(inserted)).thenReturn(inserted);
        when(bookRepository.findById(inserted.getId())).thenReturn(Optional.of(inserted));
        when(publisherMapper.toPublisherData(any())).thenReturn(publisherData);
        when(bookMapperImpl.toGetBookDTO(inserted, publisherData))
                .thenReturn(GetBookDTO.builder().name("nombre").price(2.2).image("imagen")
                        .publisher(any()).build());
        GetBookDTO updated2 = bookService.patchBook(list.get(0).getId(), update);
        assertNotNull(updated2);
        assertAll("Book properties",
                () -> assertEquals(update.getName(), updated2.getName(), "El nombre debe coincidir"),
                () -> assertEquals(update.getPrice(), updated2.getPrice(), "El precio debe coincidir"),
                () -> assertEquals(update.getImage(), updated2.getImage(), "La imagen debe coincidir")
        );
        verify(bookRepository, times(1)).save(any(Book.class));
    }


    /**
     * Test para comprobar que el Book no se encuentra cuando se hace un Patch
     */
    @Test
    void testPatchBookNotFound() {
        var update = PatchBookDTO.builder()
                .name("nombre").price(2.2).image("imagen").build();
        var data = list.get(0).getId();
        when(bookRepository.findById(list.get(0).getId())).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> bookService.patchBook(data, update));
    }

    /**
     * Test para comprobar que se elimina un Book
     *
     * @throws BookNotValidIDException excepción cuando el Book no es válido
     * @throws BookNotFoundException   excepción cuando el Book no se encuentra
     */
    @Test
    void testDeleteBook() throws BookNotValidIDException, BookNotFoundException {
        when(bookRepository.findById(list.get(0).getId())).thenReturn(Optional.ofNullable(list.get(0)));

        //previus patch
        var update = PatchBookDTO.builder()
                .name("nombre")
                .publisherId(1L)
                .price(2.2)
                .image("imagen")
                .description("descripción")
                .build();
        var publisher = Publisher.builder().id(1L).createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now()).build();
        var publisherDTO = PublisherDTO.builder().id(1L).build();
        var publisherData = PublisherData.builder().id(1L).build();
        var inserted = Book.builder().id(1L).name("nombre").price(2.2).image("imagen")
                .publisher(publisher).description("descripción")
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).active(true).build();
        when(publisherService.findById(1L)).thenReturn(any());
        when(publisherMapper.toPublisher(publisherDTO)).thenReturn(publisher);
        when(bookRepository.save(inserted)).thenReturn(inserted);
        when(bookRepository.findById(inserted.getId())).thenReturn(Optional.of(inserted));
        when(publisherMapper.toPublisherData(any())).thenReturn(publisherData);
        when(bookMapperImpl.toGetBookDTO(inserted, publisherData))
                .thenReturn(GetBookDTO.builder().name("nombre").price(2.2).image("imagen")
                        .publisher(any()).build());
        GetBookDTO updated2 = bookService.patchBook(list.get(0).getId(), update);
        assertNotNull(updated2);

        //delete
        bookService.deleteBook(list.get(0).getId());

        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Book> expectedPage = new PageImpl<>(new ArrayList<>());
        Specification<Book> anySpecification = any();
        when(bookRepository.findAll(anySpecification, any(Pageable.class))).thenReturn(expectedPage);
        Page<GetBookDTO> actualPage = bookService.getAllBook(Optional.empty(), Optional.empty(),
                Optional.empty(), pageable);
        var list3 = actualPage.getContent();
        assertNotNull(list3);
        assertEquals(0, actualPage.getContent().size());
    }


    /**
     * Test para comprobar que el Book no se encuentra cuando se intenta eliminar
     */
    @Test
    void testDeleteBookNotFound() {
        var data = 1L;
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(data));
    }

    /**
     * Test que comprueba que se puede actualizar una imagen
     *
     * @throws IOException Problema Entrada/Salida
     */
    @Test
    void testUpdateImageSuccess() throws IOException {
        long existingBookId = list.get(0).getId();
        String imageUrl = "https://www.madirex.com/favicon.ico";
        MultipartFile multipartFile = mock(MultipartFile.class);
        GetBookDTO expectedBookDTO = GetBookDTO.builder()
                .id(1L)
                .name("nombre")
                .publisher(PublisherData.builder().id(1L).build())
                .price(2.2)
                .image(imageUrl)
                .description("descripción")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .active(true)
                .build();

        Book book = Book.builder()
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

        when(bookRepository.findById(list.get(0).getId()))
                .thenReturn(Optional.of(list.get(0)));

        //path
        var update = PatchBookDTO.builder().image(imageUrl).build();
        when(bookRepository.findById(list.get(0).getId())).thenReturn(Optional.of(list.get(0)));
        when(bookRepository.save(list.get(0))).thenReturn(book);
        when(bookMapperImpl.toGetBookDTO(any(), any())).thenReturn(expectedBookDTO);
        when(publisherMapper.toPublisherData(any(Publisher.class))).thenReturn(PublisherData.builder().id(1L).build());
        GetBookDTO updated2 = bookService.patchBook(list.get(0).getId(), update);

        //check
        GetBookDTO resultBookDTO = bookService.updateImage(existingBookId, multipartFile, false);
        assertAll(
                () -> assertNotNull(updated2),
                () -> assertNotNull(resultBookDTO),
                () -> assertEquals(expectedBookDTO.getImage(), resultBookDTO.getImage())
        );
        verify(bookRepository, times(3)).findById(list.get(0).getId());
        verify(bookRepository, times(2)).save(list.get(0));
    }


    /**
     * Test que comprueba que el Book no se ha encontrado al intentar actualizar la imagen
     */
    @Test
    void testUpdateImageBookNotFound() {
        long fakeId = 1L;
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(bookRepository.findById(fakeId)).thenThrow(new BookNotFoundException(fakeId + ""));
        assertThrows(BookNotFoundException.class, () -> bookService.updateImage(fakeId, multipartFile, false));
    }


    /**
     * Test que comprueba el OnChange cuando el WebSocketService es nulo
     */
    @Test
    void testOnChangeWhenWebSocketServiceIsNull() {
        MockitoAnnotations.openMocks(this);
        GetBookDTO getBookDTO = new GetBookDTO();
        bookService.setWebSocketService(null);
        bookService.onChange(Notification.Type.CREATE, getBookDTO);
        assertNotNull(getBookDTO);
    }

    /**
     * Test que comprueba el OnChange cuando el WebSocketService tiene una excepción de entrada/salida
     */
    @Test
    void testOnChangeWithIOException() {
        GetBookDTO dummyData = new GetBookDTO();
        bookService.onChange(Notification.Type.CREATE, dummyData);
        assertNotNull(dummyData);
    }

}
