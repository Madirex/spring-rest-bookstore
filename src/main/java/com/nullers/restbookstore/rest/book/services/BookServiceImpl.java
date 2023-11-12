package com.nullers.restbookstore.rest.book.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nullers.restbookstore.NOADD.Publisher;
import com.nullers.restbookstore.NOADD.PublisherNotFoundException;
import com.nullers.restbookstore.NOADD.PublisherNotValidIDException;
import com.nullers.restbookstore.NOADD.PublisherService;
import com.nullers.restbookstore.config.websockets.WebSocketConfig;
import com.nullers.restbookstore.config.websockets.WebSocketHandler;
import com.nullers.restbookstore.notifications.models.Notification;
import com.nullers.restbookstore.rest.book.dto.CreateBookDTO;
import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.dto.PatchBookDTO;
import com.nullers.restbookstore.rest.book.dto.UpdateBookDTO;
import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.book.exceptions.BookNotValidUUIDException;
import com.nullers.restbookstore.rest.book.mappers.BookMapperImpl;
import com.nullers.restbookstore.rest.book.mappers.BookNotificationMapper;
import com.nullers.restbookstore.rest.book.models.Book;
import com.nullers.restbookstore.rest.book.notifications.BookNotificationResponse;
import com.nullers.restbookstore.rest.book.repositories.BookRepository;
import com.nullers.restbookstore.storage.service.StorageService;
import com.nullers.restbookstore.utils.Util;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Clase BookServiceImpl
 *
 * @Author Madirex
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "books")
public class BookServiceImpl implements BookService {

    public static final String BOOK_NOT_FOUND_MSG = "No se ha encontrado el Book con el UUID indicado";
    public static final String NOT_VALID_FORMAT_UUID_MSG = "El UUID no tiene un formato válido";

    private final BookRepository bookRepository;
    private final BookMapperImpl bookMapperImpl;
    private final WebSocketConfig webSocketConfig;

    @Setter
    private WebSocketHandler webSocketService;
    private final StorageService storageService;
    private final PublisherService publisherService;
    private final ObjectMapper mapper;
    private final BookNotificationMapper bookNotificationMapper;


    /**
     * Constructor BookServiceImpl
     *
     * @param bookRepository         BookRepositoryImpl
     * @param bookMapperImpl         BookMapper
     * @param webSocketConfig        WebSocketConfig
     * @param storageService         StorageService
     * @param publisherService       PublisherService
     * @param bookNotificationMapper BookNotificationMapper
     */
    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookMapperImpl bookMapperImpl,
                           WebSocketConfig webSocketConfig, StorageService storageService,
                           PublisherService publisherService, BookNotificationMapper bookNotificationMapper) {
        this.bookRepository = bookRepository;
        this.bookMapperImpl = bookMapperImpl;
        this.webSocketConfig = webSocketConfig;
        this.webSocketService = webSocketConfig.webSocketHandler();
        this.storageService = storageService;
        this.publisherService = publisherService;
        this.bookNotificationMapper = bookNotificationMapper;
        this.mapper = new ObjectMapper();
    }

    /**
     * Obtiene todos los Books
     *
     * @param publisher Publisher por la que filtrar
     * @param maxPrice  Precio máximo por el que filtrar
     * @param pageable  Paginación
     * @return Lista de Books
     */
    @Cacheable
    @Override
    public Page<GetBookDTO> getAllBook(Optional<String> publisher, Optional<Double> maxPrice, PageRequest pageable) {
        Specification<Book> specType = (root, query, criteriaBuilder) ->
                publisher.map(m -> {
                    try {
                        return criteriaBuilder.equal(criteriaBuilder.upper(root.get("publisher").get("name")),
                                m.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        return criteriaBuilder.isTrue(criteriaBuilder.literal(false));
                    }
                }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Book> specMaxPrice = (root, query, criteriaBuilder) ->
                maxPrice.map(p -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));


        Specification<Book> criterion = Specification.where(specType)
                .and(specMaxPrice);

        Page<Book> funkoPage = bookRepository.findAll(criterion, pageable);
        List<GetBookDTO> dtoList = funkoPage.getContent().stream()
                .map(bookMapperImpl::toGetBookDTO)
                .toList();

        return new PageImpl<>(dtoList, funkoPage.getPageable(), funkoPage.getTotalElements());
    }

    /**
     * Obtiene un Book por su UUID
     *
     * @param id UUID del Book a obtener
     * @return Book con el UUID indicado
     * @throws BookNotValidUUIDException Si el UUID no tiene un formato válido
     * @throws BookNotFoundException     Si no se ha encontrado el Book con el UUID indicado
     */
    @Cacheable(key = "#result.id")
    @Override
    public GetBookDTO getBookById(String id) throws BookNotValidUUIDException, BookNotFoundException {
        try {
            UUID uuid = UUID.fromString(id);
            var f = bookRepository.findById(uuid).orElseThrow(() ->
                    new BookNotFoundException(BOOK_NOT_FOUND_MSG));
            return bookMapperImpl.toGetBookDTO(f);
        } catch (IllegalArgumentException e) {
            throw new BookNotValidUUIDException(NOT_VALID_FORMAT_UUID_MSG);
        }
    }

    /**
     * Crea un Book
     *
     * @param book CreateBookDTO con los datos del Book a crear
     * @return Book creado
     * @throws PublisherNotFoundException   Si no se ha encontrado la publisher con el ID indicado
     * @throws PublisherNotValidIDException Si el ID no tiene un formato válido
     */
    @CachePut(key = "#result.id")
    @Override
    public GetBookDTO postBook(CreateBookDTO book) throws PublisherNotFoundException, PublisherNotValidIDException {
        var publisher = publisherService.getPublisherById(book.getPublisherId());
        var f = bookRepository.save(bookMapperImpl.toBook(book, publisher));
        var bookDTO = bookMapperImpl.toGetBookDTO(f);
        onChange(Notification.Type.CREATE, bookDTO);
        return bookDTO;
    }

    /**
     * Actualiza un Book
     *
     * @param id   UUID del Book a actualizar
     * @param book UpdateBookDTO con los datos a actualizar
     * @return Book actualizado
     * @throws BookNotValidUUIDException    Si el UUID no tiene un formato válido
     * @throws PublisherNotFoundException   Si no se ha encontrado el publisher con el ID indicado
     * @throws PublisherNotValidIDException Si el ID no tiene un formato válido
     * @throws BookNotFoundException        Si no se ha encontrado el Book con el UUID indicado
     */
    @CachePut(key = "#result.id")
    @Override
    public GetBookDTO putBook(String id, UpdateBookDTO book) throws BookNotValidUUIDException,
            PublisherNotFoundException, PublisherNotValidIDException, BookNotFoundException {
        try {
            UUID uuid = UUID.fromString(id);
            Book existingBook = bookRepository.findById(UUID.fromString(id))
                    .orElseThrow(() -> new BookNotFoundException("Book no encontrado"));
            Publisher publisher = publisherService.getPublisherById(book.getPublisherId());
            Book f = bookMapperImpl.toBook(existingBook, book, publisher);
            f.setId(uuid);
            var modified = bookRepository.save(f);
            var bookDTO = bookMapperImpl.toGetBookDTO(modified);
            onChange(Notification.Type.UPDATE, bookDTO);
            return bookDTO;
        } catch (IllegalArgumentException e) {
            throw new BookNotValidUUIDException(NOT_VALID_FORMAT_UUID_MSG);
        }
    }

    /**
     * Actualiza un Book
     *
     * @param id   UUID del Book a actualizar
     * @param book Book con los datos a actualizar
     * @return Book actualizado
     * @throws BookNotValidUUIDException    Si el UUID no tiene un formato válido
     * @throws BookNotFoundException        Si no se ha encontrado el Book con el UUID indicado
     * @throws PublisherNotFoundException   Si no se ha encontrado la publisher con el ID indicado
     * @throws PublisherNotValidIDException Si el ID no tiene un formato válido
     */
    @CachePut(key = "#result.id")
    @Override
    public GetBookDTO patchBook(String id, PatchBookDTO book) throws BookNotValidUUIDException, BookNotFoundException,
            PublisherNotFoundException, PublisherNotValidIDException {
        try {
            UUID uuid = UUID.fromString(id);
            var opt = bookRepository.findById(uuid);
            if (opt.isEmpty()) {
                throw new BookNotFoundException(BOOK_NOT_FOUND_MSG);
            }
            BeanUtils.copyProperties(book, opt.get(), Util.getNullPropertyNames(book));
            opt.get().setId(uuid);
            opt.get().setUpdatedAt(LocalDateTime.now());
            Book modified = bookRepository.save(opt.get());
            var bookDTO = bookMapperImpl.toGetBookDTO(modified);
            onChange(Notification.Type.UPDATE, bookDTO);
            return bookDTO;
        } catch (IllegalArgumentException e) {
            throw new BookNotValidUUIDException(NOT_VALID_FORMAT_UUID_MSG);
        }
    }

    /**
     * Elimina un Book
     *
     * @param id UUID del Book a eliminar
     * @throws BookNotFoundException     Si no se ha encontrado el Book con el UUID indicado
     * @throws BookNotValidUUIDException Si el UUID no tiene un formato válido
     */
    @CacheEvict(key = "#id")
    @Override
    public void deleteBook(String id) throws BookNotFoundException, BookNotValidUUIDException {
        try {
            UUID uuid = UUID.fromString(id);
            var opt = bookRepository.findById(uuid);
            if (opt.isEmpty()) {
                throw new BookNotFoundException(BOOK_NOT_FOUND_MSG);
            }
            bookRepository.delete(opt.get());
            onChange(Notification.Type.DELETE, bookMapperImpl.toGetBookDTO(opt.get()));
        } catch (IllegalArgumentException e) {
            throw new BookNotValidUUIDException(NOT_VALID_FORMAT_UUID_MSG);
        }
    }

    /**
     * Actualiza la imagen de un Book
     *
     * @param id      UUID del Book a actualizar
     * @param image   Imagen a actualizar
     * @param withUrl Si se quiere devolver la URL de la imagen
     * @return Book actualizado
     * @throws BookNotFoundException        Si no se ha encontrado el Book con el UUID indicado
     * @throws BookNotValidUUIDException    Si el UUID no tiene un formato válido
     * @throws PublisherNotFoundException   Si no se ha encontrado la publisher con el ID indicado
     * @throws PublisherNotValidIDException Si el ID no tiene un formato válido
     */
    @Override
    @CachePut(key = "#result.id")
    @Transactional
    public GetBookDTO updateImage(String id, MultipartFile image, Boolean withUrl) throws BookNotFoundException,
            BookNotValidUUIDException, PublisherNotFoundException, PublisherNotValidIDException, IOException {
        try {
            UUID uuid = UUID.fromString(id);
            var actualBook = bookRepository.findById(uuid).orElseThrow(() -> new BookNotFoundException(id));
            String imageStored = storageService.store(image, List.of("jpg", "jpeg", "png"), id);
            String imageUrl = Boolean.FALSE.equals(withUrl) ? imageStored : storageService.getUrl(imageStored);
            if (actualBook.getImage() != null && !actualBook.getImage().equals(Book.IMAGE_DEFAULT)) {
                storageService.delete(actualBook.getImage());
            }
            return patchBook(id, PatchBookDTO.builder()
                    .image(imageUrl)
                    .build());
        } catch (IllegalArgumentException e) {
            throw new BookNotValidUUIDException(NOT_VALID_FORMAT_UUID_MSG);
        }
    }

    /**
     * Método para enviar una notificación a los clientes ws
     *
     * @param type Tipo de notificación
     * @param data Datos de la notificación
     */
    void onChange(Notification.Type type, GetBookDTO data) {
        log.debug("Servicio de productos onChange con tipo: " + type + " y datos: " + data);
        if (webSocketService == null) {
            log.warn("No se ha podido enviar la notificación a los clientes ws, no se ha encontrado el servicio");
            webSocketService = this.webSocketConfig.webSocketHandler();
        }

        try {
            Notification<BookNotificationResponse> notification = new Notification<>(
                    "BOOKS",
                    type,
                    bookNotificationMapper.toBookNotificationDto(data),
                    LocalDateTime.now().toString()
            );

            String json = mapper.writeValueAsString(notification);

            log.info("Enviando mensaje a los clientes ws");
            Thread senderThread = new Thread(() -> {
                try {
                    webSocketService.sendMessage(json);
                } catch (Exception e) {
                    log.error("Error al enviar el mensaje a través del servicio WebSocket", e);
                }
            });
            senderThread.start();
        } catch (JsonProcessingException e) {
            log.error("Error al convertir la notificación a JSON", e);
        }
    }
}