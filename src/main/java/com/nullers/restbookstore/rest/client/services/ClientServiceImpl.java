package com.nullers.restbookstore.rest.client.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nullers.restbookstore.config.websockets.WebSocketConfig;
import com.nullers.restbookstore.config.websockets.WebSocketHandler;
import com.nullers.restbookstore.notifications.models.Notification;
import com.nullers.restbookstore.rest.book.services.BookServiceImpl;
import com.nullers.restbookstore.rest.client.dto.ClientCreateDto;
import com.nullers.restbookstore.rest.client.dto.ClientDto;
import com.nullers.restbookstore.rest.client.dto.ClientUpdateDto;
import com.nullers.restbookstore.rest.client.exceptions.ClientAlreadyExists;
import com.nullers.restbookstore.rest.client.exceptions.ClientInOrderException;
import com.nullers.restbookstore.rest.client.exceptions.ClientNotFound;
import com.nullers.restbookstore.rest.client.mappers.ClientCreateMapper;
import com.nullers.restbookstore.rest.client.mappers.ClientMapper;
import com.nullers.restbookstore.rest.client.model.Client;
import com.nullers.restbookstore.rest.client.notifications.dto.ClientNotificationResponse;
import com.nullers.restbookstore.rest.client.notifications.mapper.ClientNotificationMapper;
import com.nullers.restbookstore.rest.client.repository.ClientRepository;
import com.nullers.restbookstore.rest.common.Address;
import com.nullers.restbookstore.rest.orders.repositories.OrderRepository;
import com.nullers.restbookstore.storage.services.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Daniel
 * @see ClientRepository
 * @see BookServiceImpl
 * @see StorageService
 * @see WebSocketConfig
 * @see WebSocketHandler
 * @see ClientNotificationMapper
 * Servicio que implementa la interfaz ClientService y que se encarga de gestionar los clientes
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "clients")
public class ClientServiceImpl implements ClientService {
    public static final String EMAIL = "email";
    private final ClientRepository clientRepository;
    private final OrderRepository orderRepository;

    private final StorageService storageService;

    private final WebSocketConfig webSocketConfig;
    private WebSocketHandler webSocketService;
    private final ClientNotificationMapper clientNotificationMapper;

    private final ObjectMapper mapper;


    /**
     * Constructor de ClientServiceImpl
     *
     * @param clientRepository         repositorio de clientes
     * @param orderRepository          repositorio de pedidos
     * @param storageService           servicio de almacenamiento
     * @param webSocketConfig          configuración de websockets
     * @param clientNotificationMapper mapper de notificaciones de clientes
     */
    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, OrderRepository orderRepository, StorageService storageService, WebSocketConfig webSocketConfig, ClientNotificationMapper clientNotificationMapper) {
        this.clientRepository = clientRepository;
        this.orderRepository = orderRepository;
        this.storageService = storageService;
        this.webSocketConfig = webSocketConfig;
        this.clientNotificationMapper = clientNotificationMapper;
        webSocketService = webSocketConfig.webSocketClientsHandler();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    /**
     * @param name     nombre del cliente
     * @param surname  apellidos del cliente
     * @param email    email del cliente
     * @param phone    teléfono del cliente
     * @param address  dirección del cliente
     * @param pageable paginación
     * @return Page<ClientDto> página con los clientes encontrados
     * Busca todos los clientes que coincidan con los criterios de búsqueda
     */
    @Override
    @Cacheable
    public Page<ClientDto> findAll(
            Optional<String> name,
            Optional<String> surname,
            Optional<String> email,
            Optional<String> phone,
            Optional<String> address,
            Pageable pageable) {

        log.info("Buscando clientes con los criterios: name: " + name + ", surname: " + surname + ", email: " + email + ", phone: " + phone + ", address: " + address);
        Specification<Client> specName = ((root, query, criteriaBuilder) -> name.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + m.toLowerCase() + "%")).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))));

        Specification<Client> specSurName = ((root, query, criteriaBuilder) -> surname.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")), "%" + m.toLowerCase() + "%")).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))));

        Specification<Client> specEmail = ((root, query, criteriaBuilder) -> email.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get(EMAIL)), "%" + m.toLowerCase() + "%")).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))));

        Specification<Client> specPhone = ((root, query, criteriaBuilder) -> phone.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), "%" + m + "%")).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true))));

        Specification<Client> criterio = Specification.where(specName)
                .and(specSurName)
                .and(specEmail)
                .and(specPhone);

        return clientRepository.findAll(criterio, pageable).map(ClientMapper::toDto);
    }

    /**
     * Busca un cliente por su id
     *
     * @param id id del cliente
     * @return ClientDto cliente encontrado
     * @throws ClientNotFound si no existe el cliente
     */
    @Override
    @Cacheable(key = "#id")
    public ClientDto findById(UUID id) {
        log.info("Buscando cliente con id: " + id);
        return clientRepository.findById(id).map(ClientMapper::toDto).orElseThrow(() -> new ClientNotFound("id", id));
    }

    /**
     * Busca un cliente por su email
     *
     * @param email email del cliente
     * @return ClientDto cliente encontrado
     */
    @Override
    @Cacheable(key = "#email")
    public Optional<ClientDto> findByEmail(String email) {
        log.info("Buscando cliente con email: " + email);
        return clientRepository.getClientByEmailEqualsIgnoreCase(email).map(ClientMapper::toDto);
    }

    /**
     * Guarda un cliente
     *
     * @param client cliente a guardar
     * @return ClientDto cliente guardado
     * @throws ClientAlreadyExists si ya existe el cliente
     */
    @Override
    @CachePut(key = "#result.id")
    public ClientDto save(ClientCreateDto client) {
        Optional<Client> clientOpt = clientRepository.getClientByEmailEqualsIgnoreCase(client.getEmail());
        if (clientOpt.isPresent()) {
            log.error("El cliente con email: " + client.getEmail() + " ya existe");
            throw new ClientAlreadyExists(EMAIL, client.getEmail());
        }
        log.info("Guardando cliente " + client);
        ClientDto clientSave = ClientMapper.toDto(clientRepository.save(ClientCreateMapper.toEntity(client)));
        onChange(Notification.Type.CREATE, ClientMapper.toEntity(clientSave));
        return clientSave;
    }

    /**
     * Actualiza un cliente
     *
     * @param id     id del cliente
     * @param client cliente a actualizar
     * @return ClientDto cliente actualizado
     * @throws ClientNotFound si no existe el cliente
     */
    @Override
    @CachePut(key = "#id")
    public ClientDto update(UUID id, ClientUpdateDto client) {
        log.info("Actualizando cliente " + client);
        Optional<Client> clientDto = clientRepository.getClientByEmailEqualsIgnoreCase(client.getEmail());
        if (clientDto.isPresent() && !clientDto.get().getId().equals(id)) {
            log.error("El cliente con email: " + client.getEmail() + " ya existe");
            throw new ClientAlreadyExists(EMAIL, client.getEmail());
        }
        Optional<Client> clientToUpdateOpt = clientRepository.findById(id);
        var clientToUpdate = getClient(id, client, clientToUpdateOpt);
        ClientDto clientUpdate = ClientMapper.toDto(clientRepository.save(clientToUpdate));
        onChange(Notification.Type.UPDATE, ClientMapper.toEntity(clientUpdate));
        return clientUpdate;
    }

    /**
     * Obtiene el cliente a actualizar
     *
     * @param id                id del cliente
     * @param client            cliente a actualizar
     * @param clientToUpdateOpt cliente a actualizar
     * @return Client cliente a actualizar
     * @throws ClientNotFound si no existe el cliente
     */
    private static Client getClient(UUID id, ClientUpdateDto client, Optional<Client> clientToUpdateOpt) {
        var clientToUpdate = clientToUpdateOpt.orElseThrow(() -> new ClientNotFound("id", id));
        String name = client.getName() != null ? client.getName() : clientToUpdate.getName();
        String surname = client.getSurname() != null ? client.getSurname() : clientToUpdate.getSurname();
        String email = client.getEmail() != null ? client.getEmail() : clientToUpdate.getEmail();
        String phone = client.getPhone() != null ? client.getPhone() : clientToUpdate.getPhone();
        Address address = client.getAddress() != null ? client.getAddress() : clientToUpdate.getAddress();
        String image = clientToUpdate.getImage();
        clientToUpdate.setName(name);
        clientToUpdate.setSurname(surname);
        clientToUpdate.setEmail(email);
        clientToUpdate.setPhone(phone);
        clientToUpdate.setAddress(address);
        clientToUpdate.setImage(image);
        return clientToUpdate;
    }

    /**
     * Elimina un cliente
     *
     * @param id id del cliente
     * @throws ClientNotFound si no existe el cliente
     */
    @Override
    @CachePut(key = "#id")
    public void deleteById(UUID id) {
        var clientToDelete = clientRepository.findById(id);
        if (clientToDelete.isEmpty()) {
            throw new ClientNotFound("id", id);
        }
        int ordersClient = orderRepository.findByClientId(id, PageRequest.of(0, 10)).getContent().size();
        if (ordersClient > 0) {
            log.error("El cliente con id: " + id + " tiene pedidos asociados");
            throw new ClientInOrderException(id);
        }
        log.info("Eliminando cliente con id: " + id);
        onChange(Notification.Type.DELETE, clientToDelete.get());
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
     * Actualiza la imagen de un cliente
     *
     * @param id   id del cliente
     * @param file imagen del cliente
     * @return ClientDto cliente actualizado
     * @throws ClientNotFound si no existe el cliente
     */
    @CachePut(key = "#id")
    public ClientDto updateImage(UUID id, MultipartFile file) throws IOException {
        log.info("Actualizando imagen del cliente con id: " + id);
        var clientData = clientRepository.findById(id).orElseThrow(() -> new ClientNotFound("id", id));
        if (clientData.getImage() != null && !clientData.getImage().equals(Client.DEFAULT_IMAGE)) {
            storageService.delete(clientData.getImage());
        }
        clientData.setImage(storageService.getImageUrl(id.toString(), file, true));
        ClientDto clientUpdated = ClientMapper.toDto(clientRepository.save(clientData));
        onChange(Notification.Type.UPDATE, ClientMapper.toEntity(clientUpdated));
        return clientUpdated;
    }

    /**
     * Añadir websocket
     *
     * @param webSocketHandlerMock websocket
     */
    public void setWebSocketService(WebSocketHandler webSocketHandlerMock) {
        this.webSocketService = webSocketHandlerMock;
    }


    /**
     * Envía una notificación a los clientes conectados
     *
     * @param tipo tipo de notificación
     * @param data datos de la notificación
     */
    public void onChange(Notification.Type tipo, Client data) {
        if (webSocketService == null) {
            log.warn("No se ha podido enviar la notificación a los clientes ws, no se ha encontrado el servicio");
            webSocketService = this.webSocketConfig.webSocketClientsHandler();
        }

        try {
            Notification<ClientNotificationResponse> notification = new Notification<>(
                    "CLIENT",
                    tipo,
                    clientNotificationMapper.toClientNotificationResponse(data),
                    LocalDateTime.now().toString()
            );

            String json = mapper.writeValueAsString(notification);

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
