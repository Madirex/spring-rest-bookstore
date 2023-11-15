package com.nullers.restbookstore.rest.books.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nullers.restbookstore.rest.book.dto.CreateBookDTO;
import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.dto.PatchBookDTO;
import com.nullers.restbookstore.rest.book.dto.UpdateBookDTO;
import com.nullers.restbookstore.rest.book.exceptions.BookNotValidIDException;
import com.nullers.restbookstore.rest.book.services.BookServiceImpl;
import com.nullers.restbookstore.rest.publisher.dto.PublisherData;
import com.nullers.restbookstore.rest.publisher.exceptions.PublisherIDNotValid;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Clase BookControllerImplTest
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class BookControllerImplTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    BookServiceImpl service;
    GetBookDTO book = GetBookDTO.builder()
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

    GetBookDTO book2 = GetBookDTO.builder()
            .id(2L)
            .name("nombre2")
            .publisher(PublisherData.builder().id(2L).build())
            .price(3.3)
            .image("imagen2")
            .description("descripción2")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .active(true)
            .build();
    String endpoint = "/api/books";

    /**
     * Constructor de BookControllerImplTest
     *
     * @param service servicio de Book
     */
    @Autowired
    public BookControllerImplTest(BookServiceImpl service) {
        this.service = service;
        mapper.registerModule(new JavaTimeModule());
    }

    /**
     * Test para comprobar que se obtienen todos los Books
     *
     * @throws Exception excepción
     */
    @Test
    void testGetAll() throws Exception {
        var bookList = List.of(book, book2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(bookList);
        when(service.getAllBook(Optional.empty(), Optional.empty(), pageable)).thenReturn(page);
        MockHttpServletResponse response = mockMvc.perform(get(endpoint)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("\"name\":" + "\"" + book.getName() + "\"")),
                () -> assertTrue(response.getContentAsString().contains("\"price\":" + book.getPrice())),
                () -> assertTrue(response.getContentAsString().contains("\"image\":" + "\"" + book.getImage() + "\"")),
                () -> assertTrue(response.getContentAsString().contains("\"name\":" + "\"" + book2.getName() + "\"")),
                () -> assertTrue(response.getContentAsString().contains("\"price\":" + book2.getPrice())),
                () -> assertTrue(response.getContentAsString().contains("\"image\":" + "\"" + book2.getImage() + "\""))
        );
    }

    /**
     * Test para comprobar que se obtiene un Book por su id
     *
     * @throws Exception excepción
     */
    @Test
    void testFindById() throws Exception {
        when(service.getBookById(book.getId())).thenReturn(book);
        MockHttpServletResponse response = mockMvc.perform(
                        get(endpoint + "/{id}", book.getId().toString())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("\"name\":" + "\"" + book.getName() + "\"")),
                () -> assertTrue(response.getContentAsString().contains("\"price\":" + book.getPrice())),
                () -> assertTrue(response.getContentAsString().contains("\"image\":" + "\"" + book.getImage() + "\""))
        );
    }

    /**
     * Test para comprobar que se obtiene un Book por su id
     *
     * @throws Exception excepción
     */
    @Test
    void testFindByIdNotValidID() throws Exception {
        when(service.getBookById(null)).thenThrow(new BookNotValidIDException(""));
        MockHttpServletResponse response = mockMvc.perform(
                        get(endpoint + "/{id}", "()")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertEquals(400, response.getStatus());
    }

    /**
     * Test para comprobar que se obtiene un Book por su id
     *
     * @throws Exception excepción
     */
    @Test
    void testDeleteBook() throws Exception {
        service.deleteBook(1L);
        mockMvc.perform(delete(endpoint + "/" + 1L))
                .andExpect(status().isNoContent());
    }

    /**
     * Test para comprobar ID no válida al intentar eliminar a un Book
     *
     * @throws Exception excepción
     */
    @Test
    void testDeleteNotValidIDPublisher() throws Exception {
        doThrow(new PublisherIDNotValid("")).when(service).deleteBook(null);
        MockHttpServletResponse response = mockMvc.perform(
                        delete(endpoint + "/{id}", "()")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertEquals(400, response.getStatus());
    }

    /**
     * Test para comprobar ID no válida al intentar hacer un Patch de un Book
     *
     * @throws Exception excepción
     */
    @Test
    void testPatchNotValidIDPublisher() throws Exception {
        doThrow(new PublisherIDNotValid("")).when(service).patchBook(null, PatchBookDTO.builder()
                .publisherId(-234L).build());
        MockHttpServletResponse response = mockMvc.perform(
                        delete(endpoint + "/{id}", "()")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertEquals(400, response.getStatus());
    }

    /**
     * Test para comprobar ID no válida al intentar hacer un Post de un Book
     *
     * @throws Exception excepción
     */
    @Test
    void testPostNotValidIDPublisher() throws Exception {
        doThrow(new PublisherIDNotValid("")).when(service).postBook(CreateBookDTO.builder().publisherId(-234L).build());
        MockHttpServletResponse response = mockMvc.perform(
                        delete(endpoint + "/{id}", "()")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertEquals(400, response.getStatus());
    }

    /**
     * Test para comprobar que se crea un Book
     *
     * @throws Exception excepción
     */
    @Test
    void testPostBook() throws Exception {
        var newBook = CreateBookDTO.builder()
                .name("nombre")
                .publisherId(1L)
                .price(2.2)
                .image("imagen")
                .description("descripción")
                .build();

        GetBookDTO createdBook = GetBookDTO.builder()
                .id(book.getId())
                .name("nombre")
                .publisher(PublisherData.builder().id(1L).build())
                .price(2.2)
                .image("imagen")
                .description("descripción")
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .active(book.getActive())
                .build();

        when(service.postBook(newBook)).thenReturn(createdBook);
        mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated());
    }

    /**
     * Test para comprobar que se actualiza un Book
     *
     * @throws Exception excepción
     */
    @Test
    void testPutBook() throws Exception {
        long id = 1L;

        UpdateBookDTO updatedBook = UpdateBookDTO.builder()
                .name("nombre")
                .publisherId(1L)
                .price(2.2)
                .image("imagen")
                .description("descripción")
                .build();

        GetBookDTO updatedBookResponse = GetBookDTO.builder()
                .id(book.getId())
                .name("nombre")
                .publisher(PublisherData.builder().id(1L).build())
                .price(2.2)
                .image("imagen")
                .description("descripción")
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .active(book.getActive())
                .build();

        when(service.putBook(any(), eq(updatedBook))).thenReturn(updatedBookResponse);

        mockMvc.perform(MockMvcRequestBuilders.put(endpoint + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk());
    }

    /**
     * Test para comprobar ID no válida al hacer un Put
     *
     * @throws Exception excepción
     */
    @Test
    void testPutNotValidID() throws Exception {
        doThrow(new BookNotValidIDException("")).when(service).putBook(null, UpdateBookDTO.builder()
                .name("nombre")
                .publisherId(1L)
                .price(2.2)
                .image("imagen")
                .description("descripción")
                .build());
        MockHttpServletResponse response = mockMvc.perform(
                        put(endpoint + "/{id}", "a()a")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();
        assertEquals(400, response.getStatus());
    }

    /**
     * Test para comprobar que se actualiza parcialmente un Book
     *
     * @throws Exception excepción
     */
    @Test
    void testPatchBook() throws Exception {
        long id = 1L;

        PatchBookDTO patchedBook = PatchBookDTO.builder()
                .price(14.99)
                .build();

        GetBookDTO patchedBookResponse = GetBookDTO.builder()
                .id(book.getId())
                .name("nombre")
                .publisher(PublisherData.builder().id(1L).build())
                .price(2.2)
                .image("imagen")
                .description("descripción")
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .active(book.getActive())
                .build();

        when(service.patchBook(any(), eq(patchedBook))).thenReturn(patchedBookResponse);

        mockMvc.perform(MockMvcRequestBuilders.patch(endpoint + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(patchedBook)))
                .andExpect(status().isOk());
    }

    /**
     * Test de ValidationException
     *
     * @throws Exception excepción
     */
    @Test
    void testValidationExceptionHandling() throws Exception {
        CreateBookDTO invalidBook = CreateBookDTO.builder()
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidBook)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test para comprobar que se obtienen todos los Books
     *
     * @throws Exception excepción
     */
    @Test
    void updateBookImage() throws Exception {
        var myLocalEndpoint = endpoint + "/image/" + book.getId().toString();

        when(service.updateImage(anyLong(), any(MultipartFile.class), anyBoolean())).thenReturn(book);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "filename.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "contenido del archivo".getBytes()
        );

        MockHttpServletResponse response = mockMvc.perform(
                multipart(myLocalEndpoint)
                        .file(file)
                        .with(req -> {
                            req.setMethod("PATCH");
                            return req;
                        })
        ).andReturn().getResponse();

        var res = mapper.readValue(response.getContentAsString(), GetBookDTO.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(book.getId(), res.getId())
        );

        verify(service, times(1)).updateImage(anyLong(), any(MultipartFile.class), anyBoolean());
    }

    /**
     * Test para comprobar cuando no se le asigna una imagen cuando se intenta actualizar la imagen de un Book
     *
     * @throws Exception excepción
     */
    @Test
    void testUpdateBookImageNoImageProvided() throws Exception {
        var myLocalEndpoint = endpoint + "/image/" + book.getId().toString();
        when(service.updateImage(anyLong(), any(MultipartFile.class), anyBoolean()))
                .thenReturn(book); // Esto no se va a invocar debido a la excepción

        MockHttpServletResponse response = mockMvc.perform(
                multipart(myLocalEndpoint)
                        .file(new MockMultipartFile("file", "", "text/plain", new byte[0]))
                        .with(requestPostProcessor -> {
                            requestPostProcessor.setMethod("PATCH");
                            return requestPostProcessor;
                        })
        ).andReturn().getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("No se ha enviado una imagen para el Book"));

        verify(service, never()).updateImage(anyLong(), any(MultipartFile.class), anyBoolean());
    }

}
