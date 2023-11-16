package com.nullers.restbookstore.rest.publisher.services;

import com.nullers.restbookstore.notifications.models.Notification;
import com.nullers.restbookstore.rest.book.dto.PatchBookDTO;
import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.book.exceptions.BookNotValidIDException;
import com.nullers.restbookstore.rest.book.models.Book;
import com.nullers.restbookstore.rest.book.repositories.BookRepository;
import com.nullers.restbookstore.rest.publisher.dto.CreatePublisherDto;
import com.nullers.restbookstore.rest.publisher.dto.PatchPublisherDto;
import com.nullers.restbookstore.rest.publisher.dto.PublisherDTO;
import com.nullers.restbookstore.rest.publisher.exceptions.PublisherIDNotValid;
import com.nullers.restbookstore.rest.publisher.exceptions.PublisherNotFound;
import com.nullers.restbookstore.rest.publisher.mappers.CreatePublisherMapper;
import com.nullers.restbookstore.rest.publisher.mappers.PublisherMapper;
import com.nullers.restbookstore.rest.publisher.models.Publisher;
import com.nullers.restbookstore.rest.publisher.repositories.PublisherRepository;
import com.nullers.restbookstore.storage.service.StorageService;
import com.nullers.restbookstore.utils.Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Clase PublisherServiceImpl
 *
 * @author jaimesalcedo1
 */
@Service
@CacheConfig(cacheNames = "publishers")
public class PublisherServiceImpl implements PublisherService {
    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;
    private final PublisherMapper publisherMapper;
    private final CreatePublisherMapper createPublisherMapper;
    private final StorageService storageService;

    @Autowired
    public PublisherServiceImpl(PublisherRepository publisherRepository, BookRepository bookRepository,
                                PublisherMapper publisherMapper, CreatePublisherMapper createPublisherMapper,
                                StorageService storageService) {
        this.publisherRepository = publisherRepository;
        this.bookRepository = bookRepository;
        this.publisherMapper = publisherMapper;
        this.createPublisherMapper = createPublisherMapper;
        this.storageService = storageService;
    }

    /**
     * Encuentra todos los Publisher
     *
     * @return List<PublisherDto> lista de publisher
     */
    @Cacheable
    @Override
    public Page<PublisherDTO> findAll(Optional<String> name, PageRequest pageable) {

        Specification<Publisher> specNombrePublisher = (root, query, criteriaBuilder) ->
                name.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + m + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));


        Page<Publisher> publisherPage = publisherRepository.findAll(specNombrePublisher, pageable);
        List<PublisherDTO> dtoList = publisherPage.getContent().stream()
                .map(publisherMapper::toDto)
                .toList();

        return new PageImpl<>(dtoList, publisherPage.getPageable(), publisherPage.getTotalElements());
    }

    /**
     * Encuentra un publisher dado un ID
     *
     * @param id id por el que filtrar
     * @return PublisherDto
     */
    @Cacheable(key = "#result.id")
    @Override
    public PublisherDTO findById(Long id) {
        if (id == null) {
            throw new PublisherIDNotValid("El ID del Publisher no es válido");
        }
        return publisherRepository.findById(id)
                .map(publisherMapper::toDto)
                .orElseThrow(() -> new PublisherNotFound("id :" + id));
    }

    /**
     * crea un publisher
     *
     * @param publisher publisher a crear
     * @return PublisherDto creado
     */
    @CachePut(key = "#result.id")
    @Override
    public PublisherDTO save(CreatePublisherDto publisher) {
        return publisherMapper.toDto(publisherRepository.save(createPublisherMapper.toPublisher(publisher)));
    }

    /**
     * Actualiza un Publisher dado su id
     *
     * @param id        id del publisher a actualizar
     * @param publisher publisher con datos actualizados
     * @return PublisherDto actualizado
     */
    @CachePut(key = "#result.id")
    @Override
    public PublisherDTO update(Long id, CreatePublisherDto publisher) {
        PublisherDTO publisherUpdate = findById(id);
        Publisher updatedPublisher = publisherMapper.toPublisherModification(publisher, publisherUpdate);
        return publisherMapper.toDto(publisherRepository.save(updatedPublisher));
    }

    /**
     * Añade un libro a un publisher
     *
     * @param id     id del publisher
     * @param bookId id del libro que se quiere añadir
     * @return PublisherDto con el libro añadido
     */
    @Override
    @CachePut(key = "#id")
    public PublisherDTO addBookPublisher(Long id, Long bookId) {
        Book bookToAdd = bookRepository.getById(bookId);
        PublisherDTO publisherToUpdate = findById(id);
        publisherToUpdate.getBooks().add(bookToAdd);
        return publisherToUpdate;
    }

    /**
     * Elimina un libro de un publisher
     *
     * @param id     id del publisher
     * @param bookId id del libro que se quiere eliminar
     * @return PublisherDto con el libro eliminado
     */
    @CachePut(key = "#id")
    @Override
    public PublisherDTO removeBookPublisher(Long id, Long bookId) {
        Book bookToRemove = bookRepository.getReferenceById(bookId);
        PublisherDTO publisherUpdate = findById(id);
        publisherUpdate.getBooks().remove(bookToRemove);
        return publisherUpdate;
    }

    /**
     * Borra un publisher dado un id
     *
     * @param id id del publisher a eliminar
     */
    @CacheEvict("#id")
    @Override
    public void deleteById(Long id) {
        try {
            var opt = publisherRepository.findById(id);
            if (opt.isEmpty()) {
                throw new PublisherNotFound("No se ha encontrado el Publisher con dicho id");
            }
            patchPublisher(id, PatchPublisherDto.builder().active(false).build());
        } catch (IllegalArgumentException e) {
            throw new PublisherIDNotValid("El ID no es válido");
        }
    }

    @Override
    @CachePut(key = "#result.id")
    public PublisherDTO patchPublisher(Long id, PatchPublisherDto publisher) throws PublisherNotFound, PublisherIDNotValid{
        try {
            var opt = publisherRepository.findById(id);
            if (opt.isEmpty()) {
                throw new PublisherNotFound("No se ha encontrado el Publisher con dicho id");
            }
            BeanUtils.copyProperties(publisher, opt.get(), Util.getNullPropertyNames(publisher));
            opt.get().setId(id);
            opt.get().setUpdatedAt(LocalDateTime.now());
            Publisher modified = publisherRepository.save(opt.get());
            return publisherMapper.toDto(modified);
        } catch (IllegalArgumentException e) {
            throw new PublisherIDNotValid("El ID no es válido");
        }
    }

    /**
     * Elimina todos los publisher
     */
    @Override
    public void deleteAll() {
        publisherRepository.deleteAll();
    }

    /**
     * Actualiza la imagen de un Publisher
     *
     * @param id      Id del Publisher a actualizar
     * @param image   Imagen a actualizar
     * @param withUrl Si se quiere devolver la URL de la imagen
     * @return Publisher actualizado
     * @throws PublisherNotFound   Si no se ha encontrado la publisher con el ID indicado
     * @throws PublisherIDNotValid Si el ID no tiene un formato válido
     * @throws IOException         Si se produce un error al guardar la imagen
     */
    @Override
    @CachePut(key = "#result.id")
    @Transactional
    public PublisherDTO updateImage(Long id, MultipartFile image, Boolean withUrl) throws BookNotFoundException,
            PublisherNotFound, PublisherIDNotValid, IOException {
        try {
            var actualPublisher = publisherRepository.findById(id).orElseThrow(() -> new PublisherNotFound(String.valueOf(id)));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSSSSS");
            String imageStored = storageService.store(image, List.of("jpg", "jpeg", "png"), id
                    + "-" + LocalDateTime.now().format(formatter));
            String imageUrl = Boolean.FALSE.equals(withUrl) ? imageStored : storageService.getUrl(imageStored);
            if (actualPublisher.getImage() != null && !actualPublisher.getImage().equals(Book.IMAGE_DEFAULT)) {
                storageService.delete(actualPublisher.getImage());
            }
            return update(id, createPublisherMapper.toDtoOnlyImage(actualPublisher, imageUrl));
        } catch (IllegalArgumentException e) {
            throw new PublisherIDNotValid("El ID del Publisher no es válido");
        }
    }
}
