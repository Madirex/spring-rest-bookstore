package com.nullers.restbookstore.client.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nullers.restbookstore.NOADD.exceptions.BookNotFoundException;
import com.nullers.restbookstore.NOADD.models.Book;
import com.nullers.restbookstore.NOADD.services.BookService;
import com.nullers.restbookstore.client.dto.ClientCreateDto;
import com.nullers.restbookstore.client.dto.ClientDto;
import com.nullers.restbookstore.client.dto.ClientUpdateDto;
import com.nullers.restbookstore.client.exceptions.ClientAlreadyExists;
import com.nullers.restbookstore.client.exceptions.ClientNotFound;
import com.nullers.restbookstore.client.mappers.ClientCreateMapper;
import com.nullers.restbookstore.client.mappers.ClientMapper;
import com.nullers.restbookstore.client.models.Client;
import com.nullers.restbookstore.client.repositories.ClientRepository;
import com.nullers.restbookstore.config.websockets.WebSocketConfig;
import com.nullers.restbookstore.config.websockets.WebSocketHandler;
import com.nullers.restbookstore.notifications.dto.ClientNotificationResponse;
import com.nullers.restbookstore.notifications.mapper.ClientNotificationMapper;
import com.nullers.restbookstore.notifications.models.Notification;
import com.nullers.restbookstore.storage.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Daniel
 * @see ClientRepository
 * @see BookService
 * @see StorageService
 * @see WebSocketConfig
 * @see WebSocketHandler
 * @see ClientNotificationMapper
 * Servicio que implementa la interfaz ClientService y que se encarga de gestionar los clientes
 * */
@Service
@Slf4j
@CacheConfig(cacheNames = "clients")
public class ClientServiceImpl implements ClientService{

    private final ClientRepository clientRepository;

    private final BookService bookService;

    private final StorageService storageService;

    private final WebSocketConfig webSocketConfig;
    private WebSocketHandler webSocketService;
    private final ClientNotificationMapper funkoNotificationMapper;

    private final ObjectMapper mapper;


    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, BookService bookService, StorageService storageService, WebSocketConfig webSocketConfig, ClientNotificationMapper funkoNotificationMapper) {
        this.clientRepository = clientRepository;
        this.bookService = bookService;
        this.storageService = storageService;
        this.webSocketConfig = webSocketConfig;
        this.funkoNotificationMapper = funkoNotificationMapper;
        webSocketService = webSocketConfig.webSocketClientsHandler();
        mapper = new ObjectMapper();

    }

    /**
     * @param name nombre del cliente
     * @param surname apellidos del cliente
     * @param email email del cliente
     * @param phone telefono del cliente
     * @param address direccion del cliente
     * @param pageable paginacion
     * @return Page<ClientDto> pagina con los clientes encontrados
     * Busca todos los clientes que coincidan con los criterios de busqueda
     */
    @Override
    public Page<ClientDto> findAll(
            Optional<String> name,
            Optional<String> surname,
            Optional<String> email,
            Optional<String> phone,
            Optional<String> address,
            Pageable pageable) {

        log.info("Buscando clientes con los criterios: name: " + name + ", surname: " + surname + ", email: " + email + ", phone: " + phone + ", address: " + address);
        Specification<Client> specName = ((root, query, criteriaBuilder) ->  name.map(m -> criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")), "%"+ m + "%"))
                .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)))
        );
        Specification<Client> specSurName = ((root, query, criteriaBuilder) -> surname.map(m -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("surname")), "%"+ m + "%"))
                .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)))
        );
        Specification<Client> specEmail = ((root, query, criteriaBuilder) -> email.map(m -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("email")), "%"+ m + "%"))
                .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)))
        );
        Specification<Client> specPhone = ((root, query, criteriaBuilder) -> phone.map(m -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("phone")), "%"+ m + "%"))
                .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)))
        );
        Specification<Client> specAddress = ((root, query, criteriaBuilder) -> address.map(m -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get("address")), "%"+ m + "%"))
                .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)))
        );
        Specification<Client> criterio = Specification.where(specName)
                .and(specSurName)
                .and(specEmail)
                .and(specPhone)
                .and(specAddress);

        return clientRepository.findAll(criterio,pageable).map(ClientMapper::toDto);
    }

    /**
     * @param id id del cliente
     * @return ClientDto cliente encontrado
     * @throws ClientNotFound si no existe el cliente
     * Busca un cliente por su id
     */
    @Override
    @Cacheable(key = "#id")
    public ClientDto findById(UUID id) {
        log.info("Buscando cliente con id: " + id);
        return clientRepository.findById(id).map(ClientMapper::toDto).orElseThrow(()-> new ClientNotFound("id",id));
    }

    /**
     * @param email email del cliente
     * @return ClientDto cliente encontrado
     * Busca un cliente por su email
     */
    @Override
    @Cacheable(key = "#email")
    public Optional<ClientDto> findByEmail(String email){
        log.info("Buscando cliente con email: " + email);
        return clientRepository.getClientByEmail(email).map(ClientMapper::toDto);
    }

    /**
     * @param client cliente a guardar
     * @return ClientDto cliente guardado
     * @throws ClientAlreadyExists si ya existe el cliente
     * Guarda un cliente
     */
    @Override
    @CachePut(key = "#result.id")
    public ClientDto save(ClientCreateDto client) {
        Optional<ClientDto> clientDto = findByEmail(client.getEmail());
        if(clientDto.isPresent()){
            log.error("El cliente con email: " + client.getEmail() + " ya existe");
            throw new ClientAlreadyExists("email",client.getEmail());
        }
        log.info("Guardando cliente "+ client);
        ClientDto clientSave = ClientMapper.toDto(clientRepository.save(ClientCreateMapper.toEntity(client)));
        onChange(Notification.Type.CREATE, ClientMapper.toEntity(clientSave));
        return clientSave;
    }

    /**
     * @param id id del cliente
     * @param client cliente a actualizar
     * @return ClientDto cliente actualizado
     * @throws ClientNotFound si no existe el cliente
     * Actualiza un cliente
     */
    @Override
    @CachePut(key = "#id")
    public ClientDto update(UUID id, ClientUpdateDto client) {
        log.info("Actualizando cliente "+ client);
        ClientDto clientToUpdate = findById(id);
        String name = client.getName() != null ? client.getName() : clientToUpdate.getName();
        String surname = client.getSurname() != null ? client.getSurname() : clientToUpdate.getSurname();
        String email = client.getEmail() != null ? client.getEmail() : clientToUpdate.getEmail();
        String phone = client.getPhone() != null ? client.getPhone() : clientToUpdate.getPhone();
        String address = client.getAddress() != null ? client.getAddress() : clientToUpdate.getAddress();
        clientToUpdate.setName(name);
        clientToUpdate.setSurname(surname);
        clientToUpdate.setEmail(email);
        clientToUpdate.setPhone(phone);
        clientToUpdate.setAddress(address);
        ClientDto clientUpdate = ClientMapper.toDto(clientRepository.save(ClientMapper.toEntity(clientToUpdate)));
        onChange(Notification.Type.UPDATE, ClientMapper.toEntity(clientUpdate));
        return clientUpdate;
    }

    /**
     * @param id id del cliente
     * Elimina un cliente
     * @throws ClientNotFound si no existe el cliente
     */
    @Override
    @CachePut(key = "#id")
    public void deleteById(UUID id) {
        ClientDto clientToDelete = findById(id);
        log.info("Eliminando cliente con id: " + id);
        onChange(Notification.Type.DELETE, ClientMapper.toEntity(clientToDelete));
        clientRepository.deleteById(id);

    }

    /**
     * Elimina todos los clientes
     */
    @Override
    public void deleteAll() {
        log.info("Eliminando todos los clientes");
        clientRepository.deleteAll();
    }

    /**
     * @param id id del cliente
     * @param pageable paginacion
     * @return Page<Book> pagina con los libros del cliente
     * @throws ClientNotFound si no existe el cliente
     * Busca todos los libros de un cliente
     */
    public Page<Book> getAllBooksOfClient(UUID id, Pageable pageable){
        log.info("Buscando libros del cliente con id: " + id);
        ClientDto client = findById(id);
        List<Book> books = client.getBooks();

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = pageSize * currentPage;

        List<Book> pagedBooks;

        if (books.size() < startItem) {
            pagedBooks = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, books.size());
            pagedBooks = books.subList(startItem, toIndex);
        }

        return new PageImpl<>(pagedBooks, PageRequest.of(currentPage, pageSize), books.size());
    }


    /**
     * @param id id del cliente
     * @param bookId id del libro
     * @return ClientDto cliente actualizado
     * @throws ClientNotFound si no existe el cliente
     * @throws BookNotFoundException si no existe el libro
     * Añade un libro a un cliente
     */
    @Override
    @CachePut(key = "#id")
    public ClientDto addBookOfClient(UUID id, UUID bookId) {
        log.info("Añadiendo libro con id: " + bookId + " al cliente con id: " + id);
        Book bookToAdd = bookService.getById(bookId);
        ClientDto clientToUpdate = findById(id);
        clientToUpdate.getBooks().add(bookToAdd);
        ClientDto clientUpdate = ClientMapper.toDto(clientRepository.save(ClientMapper.toEntity(clientToUpdate)));
        onChange(Notification.Type.UPDATE, ClientMapper.toEntity(clientUpdate));
        return clientUpdate;
    }

    /**
     * @param id id del cliente
     * @param bookId id del libro
     * @return ClientDto cliente actualizado
     * @throws ClientNotFound si no existe el cliente
     * @throws BookNotFoundException si no existe el libro
     * Elimina un libro de un cliente
     */
    @Override
    @CachePut(key = "#id")
    public ClientDto removeBookOfClient(UUID id, UUID bookId) {
        log.info("Eliminando libro con id: " + bookId + " del cliente con id: " + id);
        Book bookToRemove = bookService.getById(bookId);
        ClientDto clientToUpdate = findById(id);
        clientToUpdate.getBooks().remove(bookToRemove);
        ClientDto clientUpdate = ClientMapper.toDto(clientRepository.save(ClientMapper.toEntity(clientToUpdate)));
        onChange(Notification.Type.UPDATE, ClientMapper.toEntity(clientUpdate));
        return  clientUpdate;
    }

    /**
     * @param id id del cliente
     * @param file imagen del cliente
     * @return ClientDto cliente actualizado
     * @throws ClientNotFound si no existe el cliente
     * Actualiza la imagen de un cliente
     */
    @CachePut(key = "#id")
    public ClientDto updateImage(UUID id, MultipartFile file) {
        log.info("Actualizando imagen del cliente con id: " + id);
        ClientDto clientDto = findById(id);

        if(clientDto.getImage() != null && clientDto.getImage().equals(Client.DEFAULT_IMAGE)){
            storageService.delete(clientDto.getImage());
        }

        String fileName = storageService.store(file);
        String urlImg = storageService.getUrl(fileName);

        Client clientToUpdate = ClientMapper.toEntity(clientDto);
        clientToUpdate.setImage(urlImg);
        ClientDto clientUpdated = ClientMapper.toDto(clientRepository.save(clientToUpdate));
        onChange(Notification.Type.UPDATE, ClientMapper.toEntity(clientUpdated));
        return clientUpdated;
    }


    /**
     * @param tipo tipo de notificacion
     * @param data datos de la notificacion
     * Envia una notificacion a los clientes conectados
     */
    void onChange(Notification.Type tipo, Client data) {

        if (webSocketService == null) {
            log.warn("No se ha podido enviar la notificación a los clientes ws, no se ha encontrado el servicio");
            webSocketService = this.webSocketConfig.webSocketClientsHandler();
        }

        try {
            Notification<ClientNotificationResponse> notificacion = new Notification<>(
                    "CLIENT",
                    tipo,
                    funkoNotificationMapper.toClientNotificationResponse(data),
                    LocalDateTime.now().toString()
            );

            String json = mapper.writeValueAsString(notificacion);

            Thread senderThread = new Thread(() -> {
                try {
                    webSocketService.sendMessage(json);
                } catch (Exception e) {
                    log.error("Error al enviar la notificación", e);
                }
            });
            senderThread.start();
        } catch (JsonProcessingException e) {
            log.error("Error al convertir la notificación a JSON", e);
        }
    }


}
