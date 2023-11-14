package com.nullers.restbookstore.rest.publisher.services;

import com.nullers.restbookstore.rest.book.models.Book;
import com.nullers.restbookstore.rest.book.repositories.BookRepository;
import com.nullers.restbookstore.rest.publisher.dto.CreatePublisherDto;
import com.nullers.restbookstore.rest.publisher.dto.PublisherDTO;
import com.nullers.restbookstore.rest.publisher.exceptions.PublisherNotFound;
import com.nullers.restbookstore.rest.publisher.mappers.CreatePublisherMapper;
import com.nullers.restbookstore.rest.publisher.mappers.PublisherMapper;
import com.nullers.restbookstore.rest.publisher.repositories.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Clase PublisherServiceImpl
 *
 * @author jaimesalcedo1
 */
@Service
public class PublisherServiceImpl implements PublisherService {
    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;
    private final PublisherMapper publisherMapper;

    @Autowired
    public PublisherServiceImpl(PublisherRepository publisherRepository, BookRepository bookRepository,
                                PublisherMapper publisherMapper) {
        this.publisherRepository = publisherRepository;
        this.bookRepository = bookRepository;
        this.publisherMapper = publisherMapper;
    }

    /**
     * Encuentra todos los Publisher
     *
     * @return List<PublisherDto> lista de publisher
     */
    @Override
    public List<PublisherDTO> findAll() {
        return publisherRepository
                .findAll()
                .stream()
                .map(publisherMapper::toDto).toList();
    }

    /**
     * encuentra un publisher dado un id
     *
     * @param id id por el que filtrar
     * @return PublisherDto
     */
    @Override
    public PublisherDTO findById(UUID id) {
        return publisherRepository.findById(id)
                .map(publisherMapper::toDto)
                .orElseThrow(() -> new PublisherNotFound("id: " + id));
    }

    /**
     * crea un publisher
     *
     * @param publisher publisher a crear
     * @return PublisherDto creado
     */
    @Override
    public PublisherDTO save(CreatePublisherDto publisher) {
        return publisherMapper.toDto(publisherRepository.save(CreatePublisherMapper.toPublisher(publisher)));
    }

    /**
     * Actualiza un Publisher dado su id
     *
     * @param id        id del publisher a actualizar
     * @param publisher publisher con datos actualizados
     * @return PublisherDto actualizado
     */
    @Override
    public PublisherDTO update(UUID id, CreatePublisherDto publisher) {
        PublisherDTO publisherUpdate = findById(id);
        publisherUpdate.setName(publisher.getName());
        publisherUpdate.setImage(publisher.getImage());
        return publisherMapper.toDto(publisherRepository.save(publisherMapper.toPublisher(publisherUpdate)));
    }

    /**
     * Añade un libro a un publisher
     *
     * @param id     id del publisher
     * @param bookId id del libro que se quiere añadir
     * @return PublisherDto con el libro añadido
     */
    @Override
    public PublisherDTO addBookPublisher(UUID id, Long bookId) {
        Book bookToAdd = bookRepository.getById(bookId);
        PublisherDTO publisherToUpdate = findById(id);
        publisherToUpdate.getBooks().add(bookToAdd);
        return publisherToUpdate;
    }

    /**
     * Añade un libro a un publisher
     *
     * @param id     id del publisher
     * @param bookId id del libro que se quiere eliminar
     * @return PublisherDto con el libro eliminado
     */
    @Override
    public PublisherDTO removeBookPublisher(UUID id, Long bookId) {
        Book bookToRemove = bookRepository.getById(bookId);
        PublisherDTO publisherUpdate = findById(id);
        publisherUpdate.getBooks().remove(bookToRemove);
        return publisherUpdate;
    }

    /**
     * Borra un publisher dado un id
     *
     * @param id id del publisher a eliminar
     */
    @Override
    public void deleteById(UUID id) {
        PublisherDTO publisherDelete = findById(id);
        publisherRepository.deleteById(id);
    }

    /**
     * Elimina todos los publisher
     */
    @Override
    public void deleteAll() {
        publisherRepository.deleteAll();
    }
}
