package com.nullers.restbookstore.rest.client.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nullers.restbookstore.rest.book.mappers.BookMapperImpl;
import com.nullers.restbookstore.rest.book.model.Book;
import com.nullers.restbookstore.rest.book.services.BookServiceImpl;
import com.nullers.restbookstore.rest.category.model.Categoria;
import com.nullers.restbookstore.rest.client.dto.ClientCreateDto;
import com.nullers.restbookstore.rest.client.dto.ClientDto;
import com.nullers.restbookstore.rest.client.dto.ClientUpdateDto;
import com.nullers.restbookstore.rest.client.exceptions.ClientAlreadyExists;
import com.nullers.restbookstore.rest.client.exceptions.ClientInOrderException;
import com.nullers.restbookstore.rest.client.exceptions.ClientNotFound;
import com.nullers.restbookstore.rest.common.Address;
import com.nullers.restbookstore.rest.client.model.Client;
import com.nullers.restbookstore.rest.client.repository.ClientRepository;
import com.nullers.restbookstore.config.websockets.WebSocketConfig;
import com.nullers.restbookstore.config.websockets.WebSocketHandler;
import com.nullers.restbookstore.rest.client.notifications.mapper.ClientNotificationMapper;
import com.nullers.restbookstore.notifications.models.Notification;
import com.nullers.restbookstore.rest.orders.models.Order;
import com.nullers.restbookstore.rest.orders.models.OrderLine;
import com.nullers.restbookstore.rest.orders.repositories.OrderRepository;
import com.nullers.restbookstore.rest.publisher.model.Publisher;
import com.nullers.restbookstore.rest.user.models.Role;
import com.nullers.restbookstore.rest.user.models.User;
import com.nullers.restbookstore.storage.services.StorageService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    WebSocketHandler webSocketHandler = mock(WebSocketHandler.class);

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private BookServiceImpl bookService;

    @Mock
    private BookMapperImpl bookMapper;

    @Mock
    private StorageService storageService;

    @Mock
    private WebSocketConfig webSocketConfig;

    @Mock
    private ClientNotificationMapper clientNotificationMapper;

    @InjectMocks
    private ClientServiceImpl clientService;

    OrderLine orderLine = OrderLine.builder()
            .bookId(1L)
            .quantity(1)
            .price(1.0)
            .build();

    OrderLine orderLine2 = OrderLine.builder()
            .bookId(2L)
            .quantity(1)
            .price(1.0)
            .build();



    Publisher publisher = Publisher.builder()
            .id(1L)
            .name("name")
            .build();

    Categoria categoria = Categoria.builder()
            .id(UUID.fromString("a712c5f2-eb95-449a-9ec4-1aa55cdac9bc"))
            .nombre("Cat")
            .activa(true)
            .build();

    Book book = Book.builder()
            .id(1L)
            .name("name")
            .publisher(publisher)
            .image("image.jpg")
            .stock(10)
            .price(1.0)
            .description("description")
            .active(true)
            .category(categoria)
            .build();

    Address address = Address.builder()
            .street("Calle Falsa 123")
            .city("Springfield")
            .country("USA")
            .province("Springfield")
            .number("123")
            .PostalCode("12345")
            .build();


    private final Client clientTest = Client.builder()
            .id(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"))
            .name("Daniel")
            .surname("García")
            .email("daniel@gmail.com")
            .phone("123456789")
            .address(address)
            .image("https://via.placeholder.com/150")
            .createdAt(LocalDateTime.now())
            .build();

    private final User userTest = User.builder()
            .id(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b9"))
            .name("Daniel")
            .email("daniel@gmail.com")
            .password("123456789")
            .surnames("García")
            .isDeleted(false)
            .roles(Set.of(Role.USER))
            .build();

    Order order = Order.builder()
            .id(new ObjectId())
            .userId(userTest.getId())
            .clientId(clientTest.getId())
            .isDeleted(false)
            .orderLines(List.of(orderLine, orderLine2))
            .updatedAt(LocalDateTime.now())
            .createdAt(LocalDateTime.now())
            .total(2.0)
            .totalBooks(2)
            .build();




    private final Client clientTest2 = Client.builder()
            .id(UUID.fromString("6dbcbf5e-8e1c-47cc-8578-7b0a33ebc154"))
            .name("Pepe 2")
            .surname("ruiz")
            .email("pepe@gmail.com")
            .phone("123456789")
            .address(address)
            .image("https://via.placeholder.com/150")
            .build();

    private final ClientDto clientDtoTest = ClientDto.builder()
            .id(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"))
            .name("Daniel")
            .surname("García")
            .email("daniel@gmail.com")
            .phone("123456789")
            .address(address)
            .image("https://via.placeholder.com/150")
            .build();

    private final ClientDto clientDtoTest2 = ClientDto.builder()
            .id(UUID.fromString("6dbcbf5e-8e1c-47cc-8578-7b0a33ebc154"))
            .name("Pepe 2")
            .surname("ruiz")
            .email("pepe@gmail.com")
            .phone("123456789")
            .address(address)
            .image("https://via.placeholder.com/150")
            .build();

    @BeforeEach
    void setUp() {
        clientService.setWebSocketService(webSocketHandler);
    }

    @Test
    void findAll_shouldReturnAllClientsWithoutParams() {
        List<Client> clientExpected = Arrays.asList(clientTest, clientTest2);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());

        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(2, result.getTotalElements())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));

    }

    @Test
    void findAll_shouldReturnAllClientsWithParamName() {
        List<Client> clientExpected = List.of(clientTest);
        System.out.println(clientTest.toString() + " " + " devolvemos esto al mock");
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.of("Dani"), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithParamSurName() {
        List<Client> clientExpected = List.of(clientTest2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.empty(), Optional.of("ruiz"), Optional.empty(), Optional.empty(), Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest2.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest2.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest2.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithParamEmail() {
        List<Client> clientExpected = List.of(clientTest, clientTest2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.empty(), Optional.empty(), Optional.of("@gmail.com"), Optional.empty(), Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(2, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail()),
                () -> assertEquals(clientTest2.getName(), result.getContent().get(1).getName()),
                () -> assertEquals(clientTest2.getSurname(), result.getContent().get(1).getSurname()),
                () -> assertEquals(clientTest2.getEmail(), result.getContent().get(1).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithParamPhone() {
        List<Client> clientExpected = List.of(clientTest, clientTest2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of("123456789"), Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(2,result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail()),
                () -> assertEquals(clientTest2.getName(), result.getContent().get(1).getName()),
                () -> assertEquals(clientTest2.getSurname(), result.getContent().get(1).getSurname()),
                () -> assertEquals(clientTest2.getEmail(), result.getContent().get(1).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }


    @Test
    void findAll_shouldReturnAllClientsWithParamAddress() {
        List<Client> clientExpected = List.of(clientTest, clientTest2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of("Calle Falsa 123"), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(2, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithAllParams(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.of("Dani"), Optional.of("García"), Optional.of("@gmail.com"), Optional.of("123456789"), Optional.of("Calle Falsa 123"), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithNameAndSurName(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.of("Dani"), Optional.of("García"), Optional.empty(), Optional.empty(),Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithNameAndEmail(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.of("Dani"), Optional.empty(), Optional.of("daniel@gmail.com"), Optional.empty(),Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithNameAndPhone(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.of("Dani"), Optional.empty(), Optional.empty(), Optional.of("12345"),Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithNameAndAddress(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.of("Dani"), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of("calle"), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithSurNameAndEmail(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.empty(), Optional.of("García"), Optional.of("daniel@gmail.com"), Optional.empty(), Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithSurNameAndPhone(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.empty(), Optional.of("García"), Optional.of("daniel@gmail.com"), Optional.empty(), Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithSurNameAndAddress(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.empty(), Optional.of("García"), Optional.empty(), Optional.empty(), Optional.of("calle"), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithEmailAndName(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.of("dani"), Optional.empty(), Optional.of("@gmail"), Optional.empty(), Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithEmailAndSurName(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.empty(), Optional.of("garc"), Optional.of("@gmail"), Optional.empty(), Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithEmailAndPhone(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.empty(), Optional.empty(), Optional.of("@gmail"), Optional.of("123"), Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithEmailAndAddress(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.empty(), Optional.empty(), Optional.of("@gmail"), Optional.empty(), Optional.of("calle"), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithPhoneAndName(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.of("dani"), Optional.empty(), Optional.empty(), Optional.of("123"), Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithPhoneAndSurName(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.empty(), Optional.of("gar"), Optional.empty(), Optional.of("123"), Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithPhoneAndEmail(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.empty(), Optional.empty(), Optional.of("daniel"), Optional.of("123"), Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithPhoneAndAddress(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of("123"), Optional.of("calle"), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithAddressAndName(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.of("da"), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of("calle"), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithAddressAndSurName(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.empty(), Optional.of("ga"), Optional.empty(), Optional.empty(), Optional.of("calle"), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithAddressAndEmail(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.empty(), Optional.empty(), Optional.of(".com"), Optional.empty(), Optional.of("calle"), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithAddressAndPhone(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.empty(), Optional.empty(), Optional.empty(), Optional.of("12"), Optional.of("calle"), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithNameAndSurNameAndEmail(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.of("dani"), Optional.of("gar"), Optional.of(".com"), Optional.empty(), Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithNameAndSurNameAndPhone(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.of("dani"), Optional.of("gar"), Optional.empty(), Optional.of("56"), Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithNameAndSurNameAndAddress(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.of("dani"), Optional.of("gar"), Optional.empty(), Optional.empty(), Optional.of("calle"), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithNameAndEmailAndPhone(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.of("dani"), Optional.empty(), Optional.of("@gmail"), Optional.of("123"), Optional.empty(), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldReturnAllClientsWithNameAndEmailAndAddress(){
        List<Client> clientExpected = List.of(clientTest);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(clientExpected);

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.of("dani"), Optional.empty(), Optional.of("@gmail"), Optional.empty(), Optional.of("calle"), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertFalse(result.isEmpty()),
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(clientTest.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(clientTest.getSurname(), result.getContent().get(0).getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getContent().get(0).getEmail())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_shouldNotReturnAllClientsWithAllParams() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Client> expectedPage = new PageImpl<>(List.of());

        when(clientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<ClientDto> result = clientService.findAll(Optional.of("roberto"), Optional.of("García"), Optional.of("@gmail.com"), Optional.of("123456789"), Optional.of("Calle Falsa 123"), pageable);

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isEmpty()),
                () -> assertEquals(0,result.getTotalElements())
        );

        verify(clientRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }


    @Test
    void findById(){
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));

        ClientDto result = clientService.findById(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"));

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(clientTest.getName(), result.getName()),
                () -> assertEquals(clientTest.getSurname(), result.getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getEmail())
        );

        verify(clientRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void findById_shouldThrowExceptionClientNotFound(){
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var res = assertThrows(ClientNotFound.class, () -> clientService.findById(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0")));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("Client con id: 9def16db-362b-44c4-9fc9-77117758b5b0 no existe", res.getMessage())
        );

        verify(clientRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void findByEmail(){
        when(clientRepository.getClientByEmailEqualsIgnoreCase(any(String.class))).thenReturn(Optional.of(clientTest));

        Optional<ClientDto> result = clientService.findByEmail("dani@gmail.com");

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(clientTest.getName(), result.get().getName()),
                () -> assertEquals(clientTest.getSurname(), result.get().getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.get().getEmail())
        );

        verify(clientRepository, times(1)).getClientByEmailEqualsIgnoreCase(any(String.class));
    }

    @Test
    void findByEmail_shouldReturnEmpty() {
        when(clientRepository.getClientByEmailEqualsIgnoreCase(any(String.class))).thenReturn(Optional.empty());

        Optional<ClientDto> result = clientService.findByEmail("rubi@gmail.com");

        assertAll(
                () -> assertNotNull(result),
                () -> assertTrue(result.isEmpty())
        );

        verify(clientRepository, times(1)).getClientByEmailEqualsIgnoreCase(any(String.class));

    }

    @Test
    void saved() throws IOException, InterruptedException {
        when(clientRepository.save(any(Client.class))).thenReturn(clientTest);
        doNothing().when(webSocketHandler).sendMessage(any(String.class));


        ClientCreateDto clientCreateDto = ClientCreateDto.builder()
                .name("Daniel")
                .surname("García")
                .email("dani@gmail.com")
                .address(address)
                .build();

        ClientDto result = clientService.save(clientCreateDto);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(clientTest.getName(), result.getName()),
                () -> assertEquals(clientTest.getSurname(), result.getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getEmail())
        );

        verify(clientRepository, times(1)).save(any(Client.class));
        Thread.sleep(1000);
        verify(webSocketHandler, times(1)).sendMessage(any(String.class));

    }

    @Test
    void save_shouldThrowExceptionClientAlreadyExist() {
        when(clientRepository.getClientByEmailEqualsIgnoreCase(any(String.class))).thenReturn(Optional.of(clientTest));

        ClientCreateDto clientCreateDto = ClientCreateDto.builder()
                .name("Daniel")
                .surname("García")
                .email("dani@gmail.com")
                .address(address)
                .build();

        var res = assertThrows(ClientAlreadyExists.class, () -> clientService.save(clientCreateDto));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("Client con email: dani@gmail.com ya existe", res.getMessage())
        );

        verify(clientRepository, times(1)).getClientByEmailEqualsIgnoreCase(any(String.class));
    }


    @Test
    void update() throws IOException, InterruptedException {
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));
        when(clientRepository.save(any(Client.class))).thenReturn(clientTest);
        doNothing().when(webSocketHandler).sendMessage(any(String.class));


        ClientUpdateDto client = ClientUpdateDto.builder()
                .name("Daniel")
                .surname("García")
                .email("daniupdate@gmail.com")
                .address(address)
                .phone("644331287")
                .build();

        var result = clientService.update(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0") ,client);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(clientTest.getName(), result.getName()),
                () -> assertEquals(clientTest.getSurname(), result.getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getEmail())
        );

        verify(clientRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(1)).save(any(Client.class));
        Thread.sleep(1000);
        verify(webSocketHandler, times(1)).sendMessage(any(String.class));

    }

    @Test
    void update_shouldThrowExceptionClientNotFound() {
        when(clientRepository.findById(any(UUID.class))).thenThrow(new ClientNotFound("id", UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0")));

        ClientUpdateDto client = ClientUpdateDto.builder()
                .name("Daniel")
                .surname("García")
                .email("daniel@gmail.com")
                .address(address)
                .phone("123456789")
                .build();

        var res = assertThrows(ClientNotFound.class, () -> clientService.update(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"), client));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("Client con id: 9def16db-362b-44c4-9fc9-77117758b5b0 no existe", res.getMessage())
        );

        verify(clientRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void delete() throws IOException, InterruptedException {
        when(orderRepository.findByClientId(any(UUID.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(
                Client.builder()
                        .id(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"))
                        .name("Daniel")
                        .surname("García")
                        .email("daniel@gmail.com")
                        .address(address)
                        .phone("123456789")
                        .image("http://imag.jpg")
                        .build()
        ));
        doNothing().when(webSocketHandler).sendMessage(any(String.class));

        clientService.deleteById(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"));

        verify(orderRepository, times(1)).findByClientId(any(UUID.class), any(Pageable.class));
        verify(clientRepository, times(1)).deleteById(any(UUID.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
        Thread.sleep(1000);

        verify(webSocketHandler, times(1)).sendMessage(any(String.class));

    }

    @Test
    void delete_ShouldThrowExceptionClientInOrder() {
        when(orderRepository.findByClientId(any(UUID.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(Order.builder().build())));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));
        var res = assertThrows(ClientInOrderException.class, () -> clientService.deleteById(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0")));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("El cliente con id "+clientTest.getId()+" tiene pedidos asociados", res.getMessage())
        );

        verify(orderRepository, times(1)).findByClientId(any(UUID.class), any(Pageable.class));
        verify(clientRepository, times(0)).deleteById(any(UUID.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
    }


    @Test
    void delete_shouldThrowExceptionClientNotFound() {
        when(clientRepository.findById(any(UUID.class))).thenThrow(new ClientNotFound("id", UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0")));

        var res = assertThrows(ClientNotFound.class, () -> clientService.deleteById(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0")));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("Client con id: 9def16db-362b-44c4-9fc9-77117758b5b0 no existe", res.getMessage())
        );

        verify(clientRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void deleteAll(){
        clientService.deleteAll();
        verify(clientRepository, times(1)).deleteAll();
    }

    @Test
    void updateImage() throws IOException, InterruptedException {
        String imgrl = "http://placeimg.com/640/480/people";
        MultipartFile multipartFile = mock(MultipartFile.class);

        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));
        when(storageService.store(any(MultipartFile.class), any(List.class), any(String.class))).thenReturn(imgrl);
        when(clientRepository.save(any(Client.class))).thenReturn(Client.builder()
                .id(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"))
                .name("Daniel")
                .surname("García")
                .email("daniel@gmail.com")
                .phone("123456789")
                .address(address)
                .image(imgrl)
                .build());
        when(storageService.getUrl(any(String.class))).thenReturn(imgrl);
        doNothing().when(webSocketHandler).sendMessage(any(String.class));


        ClientDto result = clientService.updateImage(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"), multipartFile);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(clientTest.getName(), result.getName()),
                () -> assertEquals(clientTest.getSurname(), result.getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getEmail()),
                () -> assertEquals(imgrl, result.getImage())
        );

        verify(clientRepository, times(1)).findById(any(UUID.class));
        verify(storageService, times(1)).store(any(MultipartFile.class), any(List.class), any(String.class));
        verify(clientRepository, times(1)).save(any(Client.class));
        verify(storageService, times(1)).getUrl(any(String.class));
        Thread.sleep(1000);
        verify(webSocketHandler, times(1)).sendMessage(any(String.class));

    }

    @Test
    void updateImage_shouldThrowExceptionClientNotFound() throws IOException, InterruptedException {
        MultipartFile multipartFile = mock(MultipartFile.class);

        when(clientRepository.findById(any(UUID.class))).thenThrow(new ClientNotFound("id", UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0")));


        var res = assertThrows(ClientNotFound.class, () -> clientService.updateImage(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"), multipartFile));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("Client con id: 9def16db-362b-44c4-9fc9-77117758b5b0 no existe", res.getMessage())
        );

        verify(clientRepository, times(1)).findById(any(UUID.class));

    }


    @Test
    void updateImage_WithImgToDelete() throws IOException, InterruptedException {
        String imgrl = "http://placeimg.com/640/480/people";
        MultipartFile multipartFile = mock(MultipartFile.class);

        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(
                Client.builder()
                        .id(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"))
                        .name("Daniel")
                        .surname("García")
                        .email("daniel@gmail.com")
                        .phone("123456789")
                        .address(address)
                        .image("https://via.placeholder.com/250")
                        .build()
        ));
        when(storageService.store(any(MultipartFile.class), any(List.class), any(String.class))).thenReturn(imgrl);
        when(clientRepository.save(any(Client.class))).thenReturn(Client.builder()
                .id(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"))
                .name("Daniel")
                .surname("García")
                .email("daniel@gmail.com")
                .phone("123456789")
                .address(address)
                .image(imgrl)
                .build());
        when(storageService.getUrl(any(String.class))).thenReturn(imgrl);
        doNothing().when(webSocketHandler).sendMessage(any(String.class));


        ClientDto result = clientService.updateImage(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"), multipartFile);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(clientTest.getName(), result.getName()),
                () -> assertEquals(clientTest.getSurname(), result.getSurname()),
                () -> assertEquals(clientTest.getEmail(), result.getEmail()),
                () -> assertEquals(imgrl, result.getImage())
        );

        verify(clientRepository, times(1)).findById(any(UUID.class));
        verify(storageService, times(1)).store(any(MultipartFile.class), any(List.class), any(String.class));
        verify(clientRepository, times(1)).save(any(Client.class));
        verify(storageService, times(1)).getUrl(any(String.class));
        verify(storageService, times(1)).delete(any(String.class));
        Thread.sleep(1000);
        verify(webSocketHandler, times(1)).sendMessage(any(String.class));

    }


    @Test
    void onChange() throws IOException, InterruptedException {
        clientService.setWebSocketService(webSocketHandler);
        doNothing().when(webSocketHandler).sendMessage(any(String.class));

        clientService.onChange(Notification.Type.CREATE, clientTest);

        Thread.sleep(1000);

        verify(webSocketHandler, times(1)).sendMessage(any(String.class));
    }

    @Test
    void onChange_ShouldReturnJsonProcessingException() throws IOException, InterruptedException {
        clientService.setWebSocketService(webSocketHandler);

        clientService.onChange(Notification.Type.CREATE, clientTest);

        Thread.sleep(1000);

        verify(webSocketHandler, times(1)).sendMessage(any(String.class));
    }

    @Test
    void update_ShouldReturnClientAlreadyExists(){
        when(clientRepository.getClientByEmailEqualsIgnoreCase(any(String.class))).thenReturn(Optional.of(clientTest));

        ClientUpdateDto client = ClientUpdateDto.builder()
                .name(clientTest.getName())
                .surname(clientTest.getSurname())
                .email(clientTest.getEmail())
                .address(clientTest.getAddress())
                .phone(clientTest.getPhone())
                .build();

        var res = assertThrows(ClientAlreadyExists.class, () -> clientService.update(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b3"), client));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("Client con email: daniel@gmail.com ya existe", res.getMessage())
        );

        verify(clientRepository, times(1)).getClientByEmailEqualsIgnoreCase(any(String.class));
    }

    @Test
    void onChangeWebSocketServiceNull() throws IOException {
        clientService.setWebSocketService(null);
        clientService.onChange(Notification.Type.CREATE, clientTest);
        verify(webSocketHandler, times(0)).sendMessage(any(String.class));
    }


}
