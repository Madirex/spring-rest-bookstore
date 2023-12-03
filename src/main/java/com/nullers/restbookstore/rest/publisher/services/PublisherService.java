package com.nullers.restbookstore.rest.publisher.services;

import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.publisher.dto.CreatePublisherDto;
import com.nullers.restbookstore.rest.publisher.dto.PatchPublisherDto;
import com.nullers.restbookstore.rest.publisher.dto.PublisherDTO;
import com.nullers.restbookstore.rest.publisher.exceptions.PublisherIDNotValid;
import com.nullers.restbookstore.rest.publisher.exceptions.PublisherNotFound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * Interface PublisherService
 *
 * @author jaimesalcedo1
 */
public interface PublisherService {
    /**
     * Método que devuelve todos los editores
     *
     * @param name     nombre del editor
     * @param pageable paginación
     * @return todos los editores
     */
    Page<PublisherDTO> findAll(Optional<String> name, PageRequest pageable);

    /**
     * Método que devuelve un editor por el ID
     *
     * @param id id del editor
     * @return editor por el ID
     */
    PublisherDTO findById(Long id);

    /**
     * Método que crea un editor
     *
     * @param publisher editor
     * @return editor creado
     */
    PublisherDTO save(CreatePublisherDto publisher);

    /**
     * Método que actualiza un editor
     *
     * @param id           id del editor
     * @param publisherDTO editor
     * @return editor actualizado
     */
    PublisherDTO update(Long id, CreatePublisherDto publisherDTO);

    /**
     * Método que actualiza un editor
     *
     * @param id           id del editor
     * @param publisherDTO editor
     * @throws PublisherNotFound   si no existe el editor
     * @throws PublisherIDNotValid si el ID del editor no es válido
     */
    void patchPublisher(Long id, PatchPublisherDto publisherDTO) throws PublisherNotFound, PublisherIDNotValid;

    /**
     * Método que añade un libro a un editor
     *
     * @param id     id del editor
     * @param bookId id del libro
     * @return editor actualizado
     */
    PublisherDTO addBookPublisher(Long id, Long bookId);

    /**
     * Método que elimina un libro de un editor
     *
     * @param id     id del editor
     * @param bookId id del libro
     * @return editor actualizado
     */
    PublisherDTO removeBookPublisher(Long id, Long bookId);

    /**
     * Método que elimina un editor por el ID
     *
     * @param id id del editor
     */
    void deleteById(Long id);

    /**
     * Método que elimina todos los editores
     */
    void deleteAll();

    /**
     * Método que actualiza la imagen de un editor
     *
     * @param id      id del editor
     * @param image   imagen
     * @param withUrl si se quiere devolver la url de la imagen
     * @return editor actualizado
     * @throws BookNotFoundException si no existe el libro
     * @throws PublisherNotFound     si no existe el editor
     * @throws PublisherIDNotValid   si el ID del editor no es válido
     * @throws IOException           si hay un error con la imagen
     */
    PublisherDTO updateImage(Long id, MultipartFile image, Boolean withUrl) throws BookNotFoundException,
            PublisherNotFound, PublisherIDNotValid, IOException;
}
