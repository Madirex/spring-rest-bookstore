package com.nullers.restbookstore.client.controllers;

import com.nullers.restbookstore.NOADD.models.Book;
import com.nullers.restbookstore.rest.client.controllers.ClientController;
import com.nullers.restbookstore.rest.client.dto.ClientCreateDto;
import com.nullers.restbookstore.rest.client.dto.ClientDto;
import com.nullers.restbookstore.rest.client.exceptions.ClientAlreadyExists;
import com.nullers.restbookstore.rest.client.exceptions.ClientNotFound;
import com.nullers.restbookstore.rest.client.models.Client;
import com.nullers.restbookstore.rest.client.services.ClientServiceImpl;
import com.nullers.restbookstore.rest.client.utils.PaginationLinksUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.connector.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(properties = "spring.config.name=application-test")
public class ClientControllerTestWithoutMockMvc {


    @Mock
    private ClientServiceImpl clientService;

    @Mock
    private PaginationLinksUtils paginationLinksUtils = new PaginationLinksUtils();

    @InjectMocks
    private ClientController clientController;

    private final Client clientTest = Client.builder()
            .id(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"))
            .name("Daniel")
            .surname("Garcia")
            .email("daniel@gmail.com")
            .phone("123456789")
            .address("Calle Falsa 123")
            .image("https://via.placeholder.com/150")
            .books(List.of(Book.builder().id(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5e2")).name("hobbit").description("prueba desc").build()))
            .build();



    private final Client clientTest2 = Client.builder()
            .id(UUID.fromString("6dbcbf5e-8e1c-47cc-8578-7b0a33ebc154"))
            .name("Pepe 2")
            .surname("ruiz")
            .email("pepe@gmail.com")
            .phone("123456789")
            .address("Calle Falsa 321")
            .image("https://via.placeholder.com/150")
            .build();

    private final ClientDto clientDtoTest = ClientDto.builder()
            .id(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"))
            .name("Daniel")
            .surname("Garcia")
            .email("daniel@gmail.com")
            .phone("123456789")
            .address("Calle Falsa 123")
            .image("https://via.placeholder.com/150")
            .build();

    private final ClientDto clientDtoTest2 = ClientDto.builder()
            .id(UUID.fromString("6dbcbf5e-8e1c-47cc-8578-7b0a33ebc154"))
            .name("Pepe 2")
            .surname("ruiz")
            .email("pepe@gmail.com")
            .phone("123456789")
            .address("Calle Falsa 321")
            .image("https://via.placeholder.com/150")
            .build();



    @BeforeEach
    void setUp() {
    }

    @Test
    void getAll(){
        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class))).thenReturn(new PageImpl(List.of(clientTest, clientTest2)));

        MockHttpServletRequest requestMock = new MockHttpServletRequest();
        requestMock.setRequestURI("/clients");
        requestMock.setServerPort(8080);



        var res = clientController.getAll(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                0 , 10, "id", "asc",
                requestMock
        );

        System.out.println(res.getBody());

        assertAll(
                () -> assertEquals(2, res.getBody().content().size()),
                () -> assertEquals(200, res.getStatusCodeValue())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAllWithFilters() {
        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class))).thenReturn(new PageImpl(List.of(clientDtoTest2, clientDtoTest)));

        MockHttpServletRequest requestMock = new MockHttpServletRequest();
        requestMock.setRequestURI("/clients");
        requestMock.setServerPort(8080);

        var res = clientController.getAll(
                Optional.of("Daniel"), Optional.of("Garcia"), Optional.of("daniel@gmail.com"), Optional.of("123456789"), Optional.of("Calle Falsa 123"),
                0, 10, "name", "desc",
                requestMock
        );

        assertAll(
                () -> assertEquals(2, res.getBody().content().size()),
                () -> assertEquals(200, res.getStatusCodeValue()),
                () -> assertEquals("Pepe 2", res.getBody().content().get(0).getName()),
                () -> assertEquals("ruiz", res.getBody().content().get(0).getSurname()),
                () -> assertEquals("Daniel", res.getBody().content().get(1).getName()),
                () -> assertEquals("Garcia", res.getBody().content().get(1).getSurname())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAllClients_ShouldReturnEmptyList(){
        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class))).thenReturn(new PageImpl(List.of()));

        MockHttpServletRequest requestMock = new MockHttpServletRequest();
        requestMock.setRequestURI("/clients");
        requestMock.setServerPort(8080);

        var res = clientController.getAll(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                0 , 10, "id", "asc",
                requestMock
        );

        assertAll(
                () -> assertEquals(0, res.getBody().content().size()),
                () -> assertEquals(200, res.getStatusCodeValue())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAllClients_ShouldReturnAllClients_withNameParam(){
        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class))).thenReturn(new PageImpl(List.of(clientDtoTest)));

        MockHttpServletRequest requestMock = new MockHttpServletRequest();
        requestMock.setRequestURI("/clients");
        requestMock.setServerPort(8080);

        var res = clientController.getAll(
                Optional.of("Daniel"), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                0 , 10, "id", "asc",
                requestMock
        );

        assertAll(
                () -> assertEquals(1, res.getBody().content().size()),
                () -> assertEquals(200, res.getStatusCodeValue()),
                () -> assertEquals("Daniel", res.getBody().content().get(0).getName()),
                () -> assertEquals("Garcia", res.getBody().content().get(0).getSurname())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAllClients_ShouldReturnAllClients_withSurnameParam(){
        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class))).thenReturn(new PageImpl(List.of(clientDtoTest)));

        MockHttpServletRequest requestMock = new MockHttpServletRequest();
        requestMock.setRequestURI("/clients");
        requestMock.setServerPort(8080);

        var res = clientController.getAll(
                Optional.empty(), Optional.of("Garcia"), Optional.empty(), Optional.empty(), Optional.empty(),
                0 , 10, "id", "asc",
                requestMock
        );

        assertAll(
                () -> assertEquals(1, res.getBody().content().size()),
                () -> assertEquals(200, res.getStatusCodeValue()),
                () -> assertEquals("Daniel", res.getBody().content().get(0).getName()),
                () -> assertEquals("Garcia", res.getBody().content().get(0).getSurname())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAllClients_ShouldReturnAllClients_withEmailParam() {
        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class))).thenReturn(new PageImpl(List.of(clientDtoTest)));

        MockHttpServletRequest requestMock = new MockHttpServletRequest();
        requestMock.setRequestURI("/clients");
        requestMock.setServerPort(8080);

        var res = clientController.getAll(
                Optional.empty(), Optional.empty(), Optional.of("@gmail.com"), Optional.empty(), Optional.empty(),
                0, 10, "id", "asc",
                requestMock
        );

        assertAll(
                () -> assertEquals(1, res.getBody().content().size()),
                () -> assertEquals(200, res.getStatusCodeValue()),
                () -> assertEquals("Daniel", res.getBody().content().get(0).getName()),
                () -> assertEquals("Garcia", res.getBody().content().get(0).getSurname())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAllClients_ShouldReturnAllClients_withPhoneParam() {
        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class))).thenReturn(new PageImpl(List.of(clientDtoTest)));

        MockHttpServletRequest requestMock = new MockHttpServletRequest();
        requestMock.setRequestURI("/clients");
        requestMock.setServerPort(8080);

        var res = clientController.getAll(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of("123456789"), Optional.empty(),
                0, 10, "id", "asc",
                requestMock
        );

        assertAll(
                () -> assertEquals(1, res.getBody().content().size()),
                () -> assertEquals(200, res.getStatusCodeValue()),
                () -> assertEquals("Daniel", res.getBody().content().get(0).getName()),
                () -> assertEquals("Garcia", res.getBody().content().get(0).getSurname())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));

    }

    @Test
    void getAllClients_ShouldReturnAllClients_withAddressParam() {
        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class))).thenReturn(new PageImpl(List.of(clientDtoTest)));

        MockHttpServletRequest requestMock = new MockHttpServletRequest();
        requestMock.setRequestURI("/clients");
        requestMock.setServerPort(8080);

        var res = clientController.getAll(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.of("Calle Falsa 123"),
                0, 10, "id", "asc",
                requestMock
        );

        assertAll(
                () -> assertEquals(1, res.getBody().content().size()),
                () -> assertEquals(200, res.getStatusCodeValue()),
                () -> assertEquals("Daniel", res.getBody().content().get(0).getName()),
                () -> assertEquals("Garcia", res.getBody().content().get(0).getSurname())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));

    }

    @Test
    void getAllClients_ShouldReturnAllClients_withPageParam(){
        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class))).thenReturn(new PageImpl(List.of(clientDtoTest2)));

        MockHttpServletRequest requestMock = new MockHttpServletRequest();
        requestMock.setRequestURI("/clients");
        requestMock.setServerPort(8080);

        var res = clientController.getAll(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                1 , 1, "id", "asc",
                requestMock
        );

        System.out.println(res.getBody());
        assertAll(
                () -> assertEquals(1, res.getBody().content().size()),
                () -> assertEquals(200, res.getStatusCodeValue()),
                () -> assertEquals("Pepe 2", res.getBody().content().get(0).getName()),
                () -> assertEquals("ruiz", res.getBody().content().get(0).getSurname())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAllClients_ShouldReturnAllClients_withSortParam(){
        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class))).thenReturn(new PageImpl(List.of(clientDtoTest2, clientDtoTest)));

        MockHttpServletRequest requestMock = new MockHttpServletRequest();
        requestMock.setRequestURI("/clients");
        requestMock.setServerPort(8080);

        var resDesc = clientController.getAll(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                0 , 10, "id", "desc",
                requestMock
        );

        assertAll(
                () -> assertEquals(2, resDesc.getBody().content().size()),
                () -> assertEquals(200, resDesc.getStatusCodeValue()),
                () -> assertEquals("Pepe 2", resDesc.getBody().content().get(0).getName()),
                () -> assertEquals("ruiz", resDesc.getBody().content().get(0).getSurname()),
                () -> assertEquals("Daniel", resDesc.getBody().content().get(1).getName()),
                () -> assertEquals("Garcia", resDesc.getBody().content().get(1).getSurname())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAllClients_ShouldReturnAllClients_withAllParamsAndPageable(){
        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class))).thenReturn(
                new PageImpl(List.of(clientDtoTest2))
        );

        MockHttpServletRequest requestMock = new MockHttpServletRequest();
        requestMock.setRequestURI("/clients");
        requestMock.setServerPort(8080);

        var resDesc = clientController.getAll(
                Optional.of("Pepe 2"), Optional.of("ruiz"), Optional.of("@gmail.com"), Optional.of("123456789"), Optional.of("Calle Falsa 321"),
                0 , 1, "id", "desc",
                requestMock
        );

        assertAll(
                () -> assertEquals(1, resDesc.getBody().content().size()),
                () -> assertEquals(200, resDesc.getStatusCodeValue()),
                () -> assertEquals("Pepe 2", resDesc.getBody().content().get(0).getName()),
                () -> assertEquals("ruiz", resDesc.getBody().content().get(0).getSurname())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));
    }


    @Test
    void getAll_ShouldReturnErrorResponse_withInvalidPageParam(){
        MockHttpServletRequest requestMock = new MockHttpServletRequest();
        requestMock.setRequestURI("/clients");
        requestMock.setServerPort(8080);

        var res = assertThrows(IllegalArgumentException.class,() -> clientController.getAll(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                -1 , 10, "id", "asc",
                requestMock
        ));

        assertAll(
                () -> assertEquals("El numero de pagina no debe ser menor a 0 y el tamano de la pagina debe ser mayor que 0", res.getMessage())
        );
    }

    @Test
    void getAll_ShouldReturnErrorResponse_withInvalidSizeParam(){
        MockHttpServletRequest requestMock = new MockHttpServletRequest();
        requestMock.setRequestURI("/clients");
        requestMock.setServerPort(8080);

        var res = assertThrows(IllegalArgumentException.class,() -> clientController.getAll(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                0 , 0, "id", "asc",
                requestMock
        ));

        assertAll(
                () -> assertEquals("El numero de pagina no debe ser menor a 0 y el tamano de la pagina debe ser mayor que 0", res.getMessage())
        );
    }

    @Test
    void getAll_ShouldReturnErrorResponse_withInvalidSortByParam(){
        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class))).thenThrow(new IllegalArgumentException("No property 'id2' found for type 'Client'"));

        MockHttpServletRequest requestMock = new MockHttpServletRequest();
        requestMock.setRequestURI("/clients");
        requestMock.setServerPort(8080);

        var res = assertThrows(IllegalArgumentException.class,() -> clientController.getAll(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                0 , 10, "id2", "asc",
                requestMock
        ));

        assertAll(
                () -> assertEquals("No property 'id2' found for type 'Client'", res.getMessage())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getById_ShouldReturnClient(){
        when(clientService.findById(any(UUID.class))).thenReturn(clientDtoTest);

        var res = clientController.getById(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"));

        assertAll(
                () -> assertEquals(clientDtoTest, res.getBody()),
                () -> assertEquals(200, res.getStatusCodeValue()),
                () -> assertEquals(clientDtoTest.getName(), res.getBody().getName()),
                () -> assertEquals(clientDtoTest.getSurname(), res.getBody().getSurname()),
                () -> assertEquals(clientDtoTest.getEmail(), res.getBody().getEmail()),
                () -> assertEquals(clientDtoTest.getPhone(), res.getBody().getPhone()),
                () -> assertEquals(clientDtoTest.getAddress(), res.getBody().getAddress()),
                () -> assertEquals(clientDtoTest.getImage(), res.getBody().getImage())
        );

        verify(clientService, times(1)).findById(any(UUID.class));
    }

    @Test
    void getById_ShouldReturnClientNotFound(){
        when(clientService.findById(any(UUID.class))).thenThrow(new ClientNotFound("id", UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b9")));

        var res = assertThrows(ClientNotFound.class, () -> clientController.getById(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b9")));

        assertAll(
                () -> assertEquals("Client con id: 9def16db-362b-44c4-9fc9-77117758b5b9 no existe", res.getMessage())
        );

        verify(clientService, times(1)).findById(any(UUID.class));
    }


    @Test
    void getByEmail_ShouldReturnClient(){
        when(clientService.findByEmail(any(String.class))).thenReturn(Optional.of(clientDtoTest));

        var res = clientController.getByEmail("daniel@gmail.com");

        assertAll(
                () -> assertEquals(clientDtoTest, res.getBody()),
                () -> assertEquals(200, res.getStatusCodeValue()),
                () -> assertEquals(clientDtoTest.getName(), res.getBody().getName()),
                () -> assertEquals(clientDtoTest.getSurname(), res.getBody().getSurname()),
                () -> assertEquals(clientDtoTest.getEmail(), res.getBody().getEmail()),
                () -> assertEquals(clientDtoTest.getPhone(), res.getBody().getPhone()),
                () -> assertEquals(clientDtoTest.getAddress(), res.getBody().getAddress()),
                () -> assertEquals(clientDtoTest.getImage(), res.getBody().getImage())
        );

        verify(clientService, times(1)).findByEmail(any(String.class));
    }

    @Test
    void findByEmail_ShouldReturnNotFound(){
        when(clientService.findByEmail(any(String.class))).thenReturn(Optional.empty());

        var res = assertThrows(ClientNotFound.class, () -> clientController.getByEmail("daniel@sdfnj.com"));

        assertAll(
                () -> assertEquals("Client con email: daniel@sdfnj.com no existe", res.getMessage())
        );

        verify(clientService, times(1)).findByEmail(any(String.class));
    }

    @Test
    void save_ShouldReturnCreateAndClient(){
        when(clientService.save(any(ClientCreateDto.class))).thenReturn(clientDtoTest);

        var res = clientController.create(
                ClientCreateDto.builder()
                        .name("Daniel")
                        .surname("Garcia")
                        .email("daniel@gmail.com")
                        .phone("123456789")
                        .address("Calle Falsa 123")
                        .build()
        );

        assertAll(
                () -> assertEquals(clientDtoTest, res.getBody()),
                () -> assertEquals(201, res.getStatusCodeValue()),
                () -> assertEquals(clientDtoTest.getName(), res.getBody().getName()),
                () -> assertEquals(clientDtoTest.getSurname(), res.getBody().getSurname()),
                () -> assertEquals(clientDtoTest.getEmail(), res.getBody().getEmail()),
                () -> assertEquals(clientDtoTest.getPhone(), res.getBody().getPhone()),
                () -> assertEquals(clientDtoTest.getAddress(), res.getBody().getAddress()),
                () -> assertEquals(clientDtoTest.getImage(), res.getBody().getImage())
        );

        verify(clientService, times(1)).save(any(ClientCreateDto.class));
    }

    @Test
    void createClient_ShouldReturnClientNotFound(){
        when(clientService.findById(any(UUID.class))).thenThrow(new ClientNotFound("id", UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b9")));

        var res = assertThrows(ClientNotFound.class, () -> clientController.getById(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b9")));

        assertAll(
                () -> assertEquals("Client con id: 9def16db-362b-44c4-9fc9-77117758b5b9 no existe", res.getMessage())
        );

        verify(clientService, times(1)).findById(any(UUID.class));
        verify(clientService, times(0)).save(any(ClientCreateDto.class));

    }

    @Test
    void createClient_ShouldReturnErrorResponse_withDuplicateEmail(){
        when(clientService.save(any(ClientCreateDto.class))).thenThrow(new ClientAlreadyExists("email", "daniel@gmail.com"));

        var res = assertThrows(ClientAlreadyExists.class, () -> clientController.create(
                ClientCreateDto.builder()
                        .name("Daniel")
                        .surname("Garcia")
                        .email("daniel@gmail.com")
                        .phone("123456789")
                        .address("Calle Falsa 123")
                        .build()
        ));

        assertAll(
                () -> assertEquals("Client con email: daniel@gmail.com ya existe", res.getMessage())
        );

        verify(clientService, times(1)).save(any(ClientCreateDto.class));
    }

    @Test
    void createClient_ShouldReturnErrorResponse_WithEmptyName(){
        ClientCreateDto clientCreateDto = ClientCreateDto.builder()
                .surname("Garcia")
                .email("daniel@gmail.com")
                .phone("123456789")
                .address("Calle Falsa 123")
                .build();

        var res = clientController.create(clientCreateDto);

        System.out.println(res);

        assertAll(
                () -> assertEquals(200, res.getStatusCodeValue())
        );
    }





}
