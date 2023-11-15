package com.nullers.restbookstore.rest.book.services;

import com.nullers.restbookstore.rest.book.dto.CreateBookDTO;
import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.dto.PatchBookDTO;
import com.nullers.restbookstore.rest.book.dto.UpdateBookDTO;
import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.book.exceptions.BookNotValidIDException;
import com.nullers.restbookstore.rest.publisher.exceptions.PublisherNotFound;
import com.nullers.restbookstore.rest.publisher.exceptions.PublisherIDNotValid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * Interface BookService
 *
 * @Author Madirex
 */
public interface BookService {
    /**
     * Obtiene todos los libros
     *
     * @param category Categoría del libro
     * @param maxPrice Precio máximo del libro
     * @param pageable Paginación
     * @return Page<GetBookDTO>
     */
    Page<GetBookDTO> getAllBook(Optional<String> publisher, Optional<Double> maxPrice, Optional<String> category, PageRequest pageable);

    /**
     * Obtiene un libro por su id
     *
     * @param id Id del libro
     * @return GetBookDTO
     * @throws BookNotValidIDException Excepción de ID de Book no válida
     * @throws BookNotFoundException   Excepción de Book no encontrado
     */
    GetBookDTO getBookById(Long id) throws BookNotValidIDException, BookNotFoundException;

    /**
     * Crea un libro
     *
     * @param book Book
     * @return GetBookDTO
     * @throws PublisherNotFound   Excepción de Publisher no encontrado
     * @throws PublisherIDNotValid Excepción de ID de Publisher no válida
     */
    GetBookDTO postBook(CreateBookDTO book) throws PublisherNotFound, PublisherIDNotValid;

    /**
     * Actualiza un libro
     *
     * @param id   Id del libro
     * @param book Book
     * @return GetBookDTO
     * @throws BookNotValidIDException      Excepción de ID de Book no válida
     * @throws BookNotFoundException        Excepción de Book no encontrado
     * @throws PublisherNotFound   Excepción de Publisher no encontrado
     * @throws PublisherIDNotValid Excepción de ID de Publisher no válida
     */
    GetBookDTO putBook(Long id, UpdateBookDTO book) throws BookNotValidIDException, BookNotFoundException, PublisherNotFound, PublisherIDNotValid;

    /**
     * Actualiza parcialmente un libro
     *
     * @param id   Id del libro
     * @param book Book
     * @return GetBookDTO
     * @throws BookNotValidIDException      Excepción de ID de Book no válida
     * @throws BookNotFoundException        Excepción de Book no encontrado
     * @throws PublisherNotFound   Excepción de Publisher no encontrado
     * @throws PublisherIDNotValid Excepción de ID de Publisher no válida
     */
    GetBookDTO patchBook(Long id, PatchBookDTO book) throws BookNotValidIDException, BookNotFoundException, PublisherNotFound, PublisherIDNotValid;

    /**
     * Elimina un libro
     *
     * @param id Id del libro
     * @throws BookNotFoundException   Excepción de Book no encontrado
     * @throws BookNotValidIDException Excepción de ID de Book no válida
     */
    void deleteBook(Long id) throws BookNotFoundException, BookNotValidIDException;

    /**
     * Actualiza la imagen de un libro
     *
     * @param id      Id del libro
     * @param image   Imagen del libro
     * @param withUrl Si se quiere devolver la url de la imagen
     * @return GetBookDTO
     * @throws BookNotFoundException        Excepción de Book no encontrado
     * @throws BookNotValidIDException      Excepción de ID de Book no válida
     * @throws PublisherNotFound   Excepción de Publisher no encontrado
     * @throws PublisherIDNotValid Excepción de ID de Publisher no válida
     * @throws IOException                  Excepción Entrada/Salida
     */
    GetBookDTO updateImage(Long id, MultipartFile image, Boolean withUrl) throws BookNotFoundException, BookNotValidIDException, PublisherNotFound, PublisherIDNotValid, IOException;
}
