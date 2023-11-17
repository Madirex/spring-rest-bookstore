package com.nullers.restbookstore.rest.client.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nullers.restbookstore.pagination.models.ErrorResponse;
import com.nullers.restbookstore.pagination.models.PageResponse;
import com.nullers.restbookstore.rest.book.dto.GetBookDTO;
import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.book.model.Book;
import com.nullers.restbookstore.rest.client.dto.ClientCreateDto;
import com.nullers.restbookstore.rest.client.dto.ClientDto;
import com.nullers.restbookstore.rest.client.dto.ClientUpdateDto;
import com.nullers.restbookstore.rest.client.exceptions.ClientAlreadyExists;
import com.nullers.restbookstore.rest.client.exceptions.ClientBookAlreadyExists;
import com.nullers.restbookstore.rest.client.exceptions.ClientNotFound;
import com.nullers.restbookstore.rest.client.model.Client;
import com.nullers.restbookstore.rest.client.services.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest(properties = "spring.config.name=application-test")
class ClientControllerTest {

    private ObjectMapper mapper;

    @MockBean
    ClientServiceImpl clientService;

    @Autowired
    MockMvc mockMvc;

    private final Client clientTest = Client.builder()
            .id(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"))
            .name("Daniel")
            .surname("Garcia")
            .email("daniel@gmail.com")
            .phone("123456789")
            .address("Calle Falsa 123")
            .image("https://via.placeholder.com/150")
            .books(List.of(Book.builder().id(1L).name("hobbit").description("prueba desc").active(true).build()))
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

    private final String endpoint = "/api/clients";

    @Autowired
    public ClientControllerTest(ClientServiceImpl clientService) {
        this.clientService = clientService;
    }

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    void getAllClients() throws Exception {
        List<ClientDto> clients = List.of(clientDtoTest, clientDtoTest2);

        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(clients));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        PageResponse<ClientDto> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructParametricType(PageResponse.class, ClientDto.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(2, res.totalElements()),
                () -> assertEquals(1, res.totalPages()),
                () -> assertEquals(2, res.content().size()),
                () -> assertEquals(clientDtoTest.getName(), res.content().get(0).getName()),
                () -> assertEquals(clientDtoTest2.getName(), res.content().get(1).getName())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));

    }

    @Test
    void getAllClients_ShouldReturnAllClients_withAllParams() throws Exception {
        List<ClientDto> clients = List.of(clientDtoTest);

        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(clients));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint)
                .param("name", "Daniel")
                .param("surname", "García")
                .param("email", "daniel@gmail.com")
                .param("phone", "123456789")
                .param("address", "Calle Falsa 123")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        PageResponse<ClientDto> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructParametricType(PageResponse.class, ClientDto.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(1, res.totalElements()),
                () -> assertEquals(1, res.totalPages()),
                () -> assertEquals(1, res.content().size()),
                () -> assertEquals(clientDtoTest.getName(), res.content().get(0).getName())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAllClients_ShouldReturnEmptyList() throws Exception {
        List<ClientDto> clients = List.of();

        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(clients));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint)
                .param("name", "Daniel22")
                .param("surname", "García")
                .param("email", "daniel@gmail.com")
                .param("phone", "123456789")
                .param("address", "Calle Falsa 123")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        System.out.println(response.getContentAsString());

        PageResponse<ClientDto> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructParametricType(PageResponse.class, ClientDto.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(0, res.totalElements()),
                () -> assertEquals(1, res.totalPages()),
                () -> assertEquals(0, res.content().size())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    /*@Test
    void getAllClients_ShouldReturnPropertyReferenceException() throws Exception {
        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class) ,any(PageRequest.class)))
                .thenThrow(new PropertyReferenceException("pepe", TypeInformation.OBJECT));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint)
                .param("sortBy", "pepe")
                .param("order", "asc")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        ErrorResponse res = mapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), res.status()),
                () -> assertEquals("No property 'pepe' found for type 'Client'", res.msg())
        );
    }*/

    @Test
    void getAllClients_ShouldReturnAllClients_withNameParam() throws Exception {
        List<ClientDto> clients = List.of(clientDtoTest);

        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(clients));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint)
                .param("name", "Daniel")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        PageResponse<ClientDto> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructParametricType(PageResponse.class, ClientDto.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(1, res.totalElements()),
                () -> assertEquals(1, res.totalPages()),
                () -> assertEquals(1, res.content().size()),
                () -> assertEquals(clientDtoTest.getName(), res.content().get(0).getName())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAllClients_ShouldReturnAllClients_withSurnameParam() throws Exception {
        List<ClientDto> clients = List.of(clientDtoTest);

        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(clients));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint)
                .param("surname", "García")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        PageResponse<ClientDto> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructParametricType(PageResponse.class, ClientDto.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(1, res.totalElements()),
                () -> assertEquals(1, res.totalPages()),
                () -> assertEquals(1, res.content().size()),
                () -> assertEquals(clientDtoTest.getName(), res.content().get(0).getName())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));
    }


    @Test
    void getAllClients_ShouldReturnAllClients_withEmailParam() throws Exception {
        List<ClientDto> clients = List.of(clientDtoTest);

        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(clients));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint)
                .param("email", "daniel@gmail.com")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        PageResponse<ClientDto> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructParametricType(PageResponse.class, ClientDto.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(1, res.totalElements()),
                () -> assertEquals(1, res.totalPages()),
                () -> assertEquals(1, res.content().size()),
                () -> assertEquals(clientDtoTest.getName(), res.content().get(0).getName())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAllClients_ShouldReturnAllClients_withPhoneParam() throws Exception {
        List<ClientDto> clients = List.of(clientDtoTest);

        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(clients));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint)
                .param("phone", "123456789")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        PageResponse<ClientDto> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructParametricType(PageResponse.class, ClientDto.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(1, res.totalElements()),
                () -> assertEquals(1, res.totalPages()),
                () -> assertEquals(1, res.content().size()),
                () -> assertEquals(clientDtoTest.getName(), res.content().get(0).getName())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAllClients_ShouldReturnAllClients_withAddressParam() throws Exception {
        List<ClientDto> clients = List.of(clientDtoTest);

        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(clients));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint)
                .param("address", "Calle Falsa 123")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        PageResponse<ClientDto> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructParametricType(PageResponse.class, ClientDto.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(1, res.totalElements()),
                () -> assertEquals(1, res.totalPages()),
                () -> assertEquals(1, res.content().size()),
                () -> assertEquals(clientDtoTest.getName(), res.content().get(0).getName())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAllClients_ShouldReturnAllClients_withPageParam() throws Exception {
        List<ClientDto> clients = List.of(clientDtoTest);

        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(clients));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint)
                .param("page", "0")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        PageResponse<ClientDto> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructParametricType(PageResponse.class, ClientDto.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(1, res.totalElements()),
                () -> assertEquals(1, res.totalPages()),
                () -> assertEquals(1, res.content().size()),
                () -> assertEquals(clientDtoTest.getName(), res.content().get(0).getName())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAllClients_ShouldReturnAllClients_withSizeParam() throws Exception {
        List<ClientDto> clients = List.of(clientDtoTest);

        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(clients));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint)
                .param("size", "1")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        PageResponse<ClientDto> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructParametricType(PageResponse.class, ClientDto.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(1, res.totalElements()),
                () -> assertEquals(1, res.totalPages()),
                () -> assertEquals(1, res.content().size()),
                () -> assertEquals(clientDtoTest.getName(), res.content().get(0).getName())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAllClients_ShouldReturnAllClients_withSortParam() throws Exception {
        List<ClientDto> clients = List.of(clientDtoTest2, clientDtoTest);

        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(clients));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint)
                .param("sort", "name")
                .param("order", "desc")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        PageResponse<ClientDto> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructParametricType(PageResponse.class, ClientDto.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(2, res.totalElements()),
                () -> assertEquals(1, res.totalPages()),
                () -> assertEquals(2, res.content().size()),
                () -> assertEquals(clientDtoTest2.getName(), res.content().get(0).getName()),
                () -> assertEquals(clientDtoTest.getName(), res.content().get(1).getName())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAllClients_ShouldReturnAllClients_withAllParamsAndPageable() throws Exception {
        List<ClientDto> clients = List.of(clientDtoTest2, clientDtoTest);

        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class)))
                .thenReturn(new PageImpl(clients));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint)
                .param("name", "Daniel")
                .param("surname", "García")
                .param("email", "@gmail.com")
                .param("phone", "123456789")
                .param("address", "Calle Falsa 123")
                .param("page", "0")
                .param("size", "1")
                .param("sort", "name")
                .param("order", "desc")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        PageResponse<ClientDto> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructParametricType(PageResponse.class, ClientDto.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(2, res.totalElements()),
                () -> assertEquals(1, res.totalPages()),
                () -> assertEquals(2, res.content().size()),
                () -> assertEquals(clientDtoTest2.getName(), res.content().get(0).getName()),
                () -> assertEquals(clientDtoTest.getName(), res.content().get(1).getName())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));

    }

    @Test
    void getAll_ShouldReturnErrorResponse_withInvalidPageParam() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(endpoint)
                .param("page", "-1")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        ErrorResponse res = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), res.status()),
                () -> assertEquals("El numero de pagina no debe ser menor a 0 y el tamano de la pagina debe ser mayor que 0", res.msg())
        );

        verify(clientService, times(0)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAll_ShouldReturnErrorResponse_withInvalidSizeParam() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(endpoint)
                .param("size", "0")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        ErrorResponse res = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), res.status()),
                () -> assertEquals("El numero de pagina no debe ser menor a 0 y el tamano de la pagina debe ser mayor que 0", res.msg())
        );

        verify(clientService, times(0)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAll_ShouldReturnErrorResponse_withInvalidSortByParam() throws Exception {
        when(clientService.findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class)))
                .thenThrow(new IllegalArgumentException("No property 'pepe' found for type 'Client'"));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint)
                .param("sortBy", "pepe")
                .param("order", "asc")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        ErrorResponse res = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), res.status()),
                () -> assertEquals("No property 'pepe' found for type 'Client'", res.msg())
        );

        verify(clientService, times(1)).findAll(any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getById_ShouldReturnClient() throws Exception {
        when(clientService.findById(any(UUID.class))).thenReturn(clientDtoTest);

        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b0")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        ClientDto res = mapper.readValue(response.getContentAsString(), ClientDto.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(clientDtoTest.getId(), res.getId()),
                () -> assertEquals(clientDtoTest.getName(), res.getName()),
                () -> assertEquals(clientDtoTest.getSurname(), res.getSurname()),
                () -> assertEquals(clientDtoTest.getEmail(), res.getEmail()),
                () -> assertEquals(clientDtoTest.getPhone(), res.getPhone()),
                () -> assertEquals(clientDtoTest.getAddress(), res.getAddress()),
                () -> assertEquals(clientDtoTest.getImage(), res.getImage())
        );

        verify(clientService, times(1)).findById(any(UUID.class));
    }

    @Test
    void getById_ShouldReturnClientNotFound() throws Exception {
        when(clientService.findById(any(UUID.class))).thenThrow(new ClientNotFound("id", UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b9")));


        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b9")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        ErrorResponse res = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Client con id: 9def16db-362b-44c4-9fc9-77117758b5b9 no existe", res.msg())
        );

        verify(clientService, times(1)).findById(any(UUID.class));
    }


    @Test
    void getByEmail_ShouldReturnClient() throws Exception {
        when(clientService.findByEmail(any(String.class))).thenReturn(Optional.of(clientDtoTest));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/email/daniel@gmail.com")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        ClientDto res = mapper.readValue(response.getContentAsString(), ClientDto.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(clientDtoTest.getId(), res.getId()),
                () -> assertEquals(clientDtoTest.getName(), res.getName()),
                () -> assertEquals(clientDtoTest.getSurname(), res.getSurname()),
                () -> assertEquals(clientDtoTest.getEmail(), res.getEmail()),
                () -> assertEquals(clientDtoTest.getPhone(), res.getPhone()),
                () -> assertEquals(clientDtoTest.getAddress(), res.getAddress()),
                () -> assertEquals(clientDtoTest.getImage(), res.getImage())
        );

        verify(clientService, times(1)).findByEmail(any(String.class));
    }

    @Test
    void findByEmail_ShouldReturnNotFound() throws Exception {
        when(clientService.findByEmail(any(String.class))).thenReturn(Optional.empty());

        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/email/daniel13@gmail.com")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        ErrorResponse res = mapper.readValue(response.getContentAsString(), ErrorResponse.class);


        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Client con email: daniel13@gmail.com no existe", res.msg())
        );

        verify(clientService, times(1)).findByEmail(any(String.class));
    }

    @Test
    void save_ShouldReturnCreateAndClient() throws Exception {
        when(clientService.save(any(ClientCreateDto.class))).thenReturn(clientDtoTest);

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientDtoTest)))
                .andReturn().getResponse();

        ClientDto res = mapper.readValue(response.getContentAsString(), ClientDto.class);

        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), response.getStatus()),
                () -> assertEquals(clientDtoTest.getName(), res.getName()),
                () -> assertEquals(clientDtoTest.getSurname(), res.getSurname()),
                () -> assertEquals(clientDtoTest.getEmail(), res.getEmail()),
                () -> assertEquals(clientDtoTest.getPhone(), res.getPhone()),
                () -> assertEquals(clientDtoTest.getAddress(), res.getAddress()),
                () -> assertEquals(clientDtoTest.getImage(), res.getImage())
        );

        verify(clientService, times(1)).save(any(ClientCreateDto.class));
    }

    @Test
    void createClient_ShouldReturnClientNotFound() throws Exception {
        when(clientService.save(any(ClientCreateDto.class))).thenThrow(new ClientNotFound("id", UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b9")));
        when(clientService.findById(any(UUID.class))).thenThrow(new ClientNotFound("id", UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b9")));

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientDtoTest)))
                .andReturn().getResponse();

        ErrorResponse res = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Client con id: 9def16db-362b-44c4-9fc9-77117758b5b9 no existe", res.msg())
        );

        verify(clientService, times(1)).save(any(ClientCreateDto.class));
    }

    @Test
    void createClient_ShouldReturnErrorResponse_withDuplicateEmail() throws Exception {
        when(clientService.save(any(ClientCreateDto.class))).thenThrow(new ClientAlreadyExists("email", "daniel@gmail.com"));

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientDtoTest)))
                .andReturn().getResponse();
        ErrorResponse res = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.CONFLICT.value(), response.getStatus()),
                () -> assertEquals("Client con email: daniel@gmail.com ya existe", res.msg())
        );

        verify(clientService, times(1)).save(any(ClientCreateDto.class));
    }


    @Test
    void createClient_ShouldReturnErrorReponse_WithEmptyName() throws Exception {
        ClientCreateDto clientCreateDto = ClientCreateDto.builder()
                .surname("Garcia")
                .email("daniel@gmail.com")
                .phone("123456789")
                .address("Calle Falsa 123")
                .build();

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientCreateDto)))
                .andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El nombre no puede estar vacio", errors.get("name"))
        );
    }

    @Test
    void createClient_ShouldReturnErrorReponse_WithEmptySurname() throws Exception {
        ClientCreateDto clientCreateDto = ClientCreateDto.builder()
                .name("Daniel")
                .email("daniel@gmail.com")
                .phone("123456789")
                .address("Calle Falsa 123")
                .build();

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(clientCreateDto))).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El apellido no puede estar vacio", errors.get("surname"))
        );
    }

    @Test
    void createClient_ShouldReturnErrorReponse_WithEmptyEmail() throws Exception {
        ClientCreateDto clientCreateDto = ClientCreateDto.builder()
                .name("Daniel")
                .surname("Garcia")
                .phone("123456789")
                .address("Calle Falsa 123")
                .build();

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(clientCreateDto))).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El email no puede estar vacio", errors.get("email"))
        );
    }


    @Test
    void createClient_ShouldReturnErrorReponse_WithEmptyPhone() throws Exception {
        ClientCreateDto clientCreateDto = ClientCreateDto.builder()
                .name("Daniel")
                .surname("Garcia")
                .email("daniel@gmail.com")
                .address("Calle Falsa 123")
                .build();

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(clientCreateDto))).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El telefono no puede estar vacio", errors.get("phone"))
        );
    }

    @Test
    void createClient_ShouldReturnErrorReponse_WithEmptyAddress() throws Exception {
        ClientCreateDto clientCreateDto = ClientCreateDto.builder()
                .name("Daniel")
                .surname("Garcia")
                .email("daniel@gmail.com")
                .phone("123456789")
                .build();

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(clientCreateDto))).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("La direccion no puede estar vacia", errors.get("address"))
        );

    }

    @Test
    void createClient_ShouldReturnErrorReponse_WithInvalidEmail() throws Exception {
        ClientCreateDto clientCreateDto = ClientCreateDto.builder()
                .name("Daniel")
                .surname("Garcia")
                .email("danielgmail.com")
                .phone("123456789")
                .address("Calle Falsa 123")
                .build();

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(clientCreateDto))).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El email debe tener un formato valido", errors.get("email"))
        );
    }

    @Test
    void createClient_ShouldReturnErrorReponse_WithInvalidPhone() throws Exception {
        ClientCreateDto clientCreateDto = ClientCreateDto.builder()
                .name("Daniel")
                .surname("Garcia")
                .email("daniel@gmail.com")
                .phone("123456784937598437587349857439857893475897")
                .address("Calle Falsa 123")
                .build();

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(clientCreateDto))).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El telefono debe tener como maximo 11 caracteres y como minimo 3", errors.get("phone"))
        );
    }

    @Test
    void createClient_ShouldReturnErrorReponse_WithInvalidPhoneString() throws Exception {
        ClientCreateDto clientCreateDto = ClientCreateDto.builder()
                .name("Daniel")
                .surname("Garcia")
                .email("daniel@gmail.com")
                .phone("sdf324df")
                .address("Calle Falsa 123")
                .build();

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(clientCreateDto))).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El telefono debe contener solo digitos", errors.get("phone"))
        );
    }

    @Test
    void createClient_ShouldReturnErrorResponse_WithAllErrors() throws Exception {
        ClientCreateDto clientCreateDto = ClientCreateDto.builder()
                .name("")
                .surname("")
                .email("danielgmail.com")
                .phone("123456784937598437587349857439857893475897")
                .address("")
                .build();

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientCreateDto)))
                .andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El nombre no puede estar vacio", errors.get("name")),
                () -> assertEquals("El apellido no puede estar vacio", errors.get("surname")),
                () -> assertEquals("El email debe tener un formato valido", errors.get("email")),
                () -> assertEquals("El telefono debe tener como maximo 11 caracteres y como minimo 3", errors.get("phone")),
                () -> assertEquals("La direccion no puede estar vacia", errors.get("address"))
        );
    }

    @Test
    void createClient_ShouldReturnErrorReponse_withPhoneMinINvalid() throws Exception {
        ClientCreateDto clientCreateDto = ClientCreateDto.builder()
                .name("Daniel")
                .surname("Garcia")
                .email("daniel@gmail.com")
                .phone("12")
                .address("Calle Falsa 123")
                .build();

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientCreateDto)))
                .andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El telefono debe tener como maximo 11 caracteres y como minimo 3", errors.get("phone"))
        );
    }

    @Test
    void update_ShouldReturnClient() throws Exception {
        when(clientService.update(any(UUID.class), any(ClientUpdateDto.class))).thenReturn(clientDtoTest);

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientDtoTest)))
                .andReturn().getResponse();

        ClientDto res = mapper.readValue(response.getContentAsString(), ClientDto.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(clientDtoTest.getId(), res.getId()),
                () -> assertEquals(clientDtoTest.getName(), res.getName()),
                () -> assertEquals(clientDtoTest.getSurname(), res.getSurname()),
                () -> assertEquals(clientDtoTest.getEmail(), res.getEmail()),
                () -> assertEquals(clientDtoTest.getPhone(), res.getPhone()),
                () -> assertEquals(clientDtoTest.getAddress(), res.getAddress()),
                () -> assertEquals(clientDtoTest.getImage(), res.getImage())
        );

        verify(clientService, times(1)).update(any(UUID.class), any(ClientUpdateDto.class));
    }

    @Test
    void update_ShouldReturnClientNotFound() throws Exception {
        when(clientService.update(any(UUID.class), any(ClientUpdateDto.class))).thenThrow(new ClientNotFound("id", UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b9")));

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b9")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientDtoTest)))
                .andReturn().getResponse();

        ErrorResponse res = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Client con id: 9def16db-362b-44c4-9fc9-77117758b5b9 no existe", res.msg())
        );

        verify(clientService, times(1)).update(any(UUID.class), any(ClientUpdateDto.class));
    }

    @Test
    void update_ShouldReturnErrorResponse_withDuplicateEmail() throws Exception {
        when(clientService.update(any(UUID.class), any(ClientUpdateDto.class))).thenThrow(new ClientAlreadyExists("email", "daniel@gmail.com"));

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientDtoTest)))
                .andReturn().getResponse();

        ErrorResponse res = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.CONFLICT.value(), response.getStatus()),
                () -> assertEquals("Client con email: daniel@gmail.com ya existe", res.msg())
        );

        verify(clientService, times(1)).update(any(UUID.class), any(ClientUpdateDto.class));
    }

    @Test
    void update_ShouldReturnErrorResponse_withAllErrors() throws Exception {
        ClientUpdateDto clientUpdateDto = ClientUpdateDto.builder()
                .name("")
                .surname("")
                .email("danielgmail.com")
                .phone("123456784937598437587349857439857893475897")
                .address("")
                .build();

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientUpdateDto)))
                .andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El nombre debe tener entre 3 y 50 caracteres", errors.get("name")),
                () -> assertEquals("El apellido debe tener entre 3 y 80 caracteres", errors.get("surname")),
                () -> assertEquals("El email debe tener un formato valido", errors.get("email")),
                () -> assertEquals("El telefono debe tener como maximo 11 caracteres y como minimo 3", errors.get("phone")),
                () -> assertEquals("La direccion debe tener entre 3 y 150 caracteres", errors.get("address"))
        );
    }

    @Test
    void update_ShouldReturnErrorReponse_withPhoneMinINvalid() throws Exception {
        ClientUpdateDto clientUpdateDto = ClientUpdateDto.builder()
                .name("Daniel")
                .surname("Garcia")
                .email("daniel@gmail.com")
                .phone("12")
                .address("Calle Falsa 123")
                .build();

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientUpdateDto)))
                .andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El telefono debe tener como maximo 11 caracteres y como minimo 3", errors.get("phone"))
        );
    }

    @Test
    void update_ShouldReturnErrorReponse_withInvalidPhoneString() throws Exception {
        ClientUpdateDto clientUpdateDto = ClientUpdateDto.builder()
                .name("Daniel")
                .surname("Garcia")
                .email("daniel@gmail.com")
                .phone("sdf324df")
                .address("Calle Falsa 123")
                .build();

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientUpdateDto)))
                .andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El telefono debe contener solo digitos", errors.get("phone"))
        );

    }

    @Test
    void update_ShouldReturnErrorReponse_withInvalidEmail() throws Exception {
        ClientUpdateDto clientUpdateDto = ClientUpdateDto.builder()
                .name("Daniel")
                .surname("Garcia")
                .email("danielgmail.com")
                .phone("123456784937598437587349857439857893475897")
                .address("Calle Falsa 123")
                .build();

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientUpdateDto)))
                .andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El email debe tener un formato valido", errors.get("email"))
        );
    }


    @Test
    void update_ShouldReturnErrorReponse_withEmptyAddress() throws Exception {
        ClientUpdateDto clientUpdateDto = ClientUpdateDto.builder()
                .name("Daniel")
                .surname("Garcia")
                .email("daniel@gmail.com")
                .phone("1234567")
                .address("")
                .build();

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientUpdateDto)))
                .andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("La direccion debe tener entre 3 y 150 caracteres", errors.get("address"))
        );
    }

    @Test
    void update_ShouldReturnErrorReponse_withEmptyPhone() throws Exception {
        ClientUpdateDto clientUpdateDto = ClientUpdateDto.builder()
                .name("Daniel")
                .surname("Garcia")
                .email("daniel@gmail.com")
                .phone("")
                .address("Calle Falsa 123")
                .build();

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(clientUpdateDto)))
                .andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El telefono debe tener como maximo 11 caracteres y como minimo 3", errors.get("phone"))
        );

    }

    @Test
    void update_ShouldReturnErrorReponse_withEmptySurname() throws Exception {
        ClientUpdateDto clientUpdateDto = ClientUpdateDto.builder()
                .name("Daniel")
                .surname("")
                .email("daniel@gmail.com")
                .phone("1234567")
                .address("Calle Falsa 123")
                .build();

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b0").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(clientUpdateDto))).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El apellido debe tener entre 3 y 80 caracteres", errors.get("surname"))
        );
    }

    @Test
    void update_ShouldReturnErrorReponse_withEmptyName() throws Exception {
        ClientUpdateDto clientUpdateDto = ClientUpdateDto.builder()
                .name("")
                .surname("Garcia")
                .email("daniel@gmail.com")
                .phone("1234567")
                .address("Calle Falsa 123")
                .build();

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b0").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(clientUpdateDto))).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El nombre debe tener entre 3 y 50 caracteres", errors.get("name"))
        );
    }


    @Test
    void deleteClient() throws Exception {
        doNothing().when(clientService).deleteById(any(UUID.class));

        MockHttpServletResponse response = mockMvc.perform(
                        delete(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b0"))
                .andReturn().getResponse();


        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), response.getStatus())
        );

        verify(clientService, times(1)).deleteById(any(UUID.class));

    }

    @Test
    void deleteClient_ShouldThrowClientNotFound() throws Exception {
        doThrow(new ClientNotFound("id", UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b9"))).when(clientService).deleteById(any(UUID.class));

        MockHttpServletResponse response = mockMvc.perform(
                        delete(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b9"))
                .andReturn().getResponse();

        ErrorResponse res = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Client con id: 9def16db-362b-44c4-9fc9-77117758b5b9 no existe", res.msg())
        );

        verify(clientService, times(1)).deleteById(any(UUID.class));
    }

    @Test
    void getAllBooks() throws Exception {
        List<GetBookDTO> books = List.of(
                GetBookDTO.builder()
                        .id(1L)
                        .name("El senor de los anillos")
                        .description("Libro de fantasia")
                        .image("https://images-na.ssl-images-amazon.com/images/I/51ZkLkaZ3OL._SX331_BO1,204,203,200_.jpg")
                        .active(true)
                        .build()
        );

        when(clientService.getAllBooksOfClient(any(UUID.class), any(PageRequest.class))).thenReturn(new PageImpl<>(books));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/9def16db-362b-44c4-9fc9-77117758b6a2/books")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        System.out.println(response.getContentAsString());
        PageResponse<Book> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructParametricType(PageResponse.class, Book.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(1, res.totalElements()),
                () -> assertEquals(1, res.totalPages()),
                () -> assertEquals(1, res.content().size()),
                () -> assertEquals("El senor de los anillos", res.content().get(0).getName()),
                () -> assertEquals("Libro de fantasia", res.content().get(0).getDescription()),
                () -> assertEquals("https://images-na.ssl-images-amazon.com/images/I/51ZkLkaZ3OL._SX331_BO1,204,203,200_.jpg", res.content().get(0).getImage())
        );

        verify(clientService, times(1)).getAllBooksOfClient(any(UUID.class), any(PageRequest.class));
    }

    @Test
    void getAllBooks_ShouldThrowClientNotFound() throws Exception {
        when(clientService.getAllBooksOfClient(any(UUID.class), any(PageRequest.class))).thenThrow(new ClientNotFound("id", UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b9")));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b9/books")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ErrorResponse res = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Client con id: 9def16db-362b-44c4-9fc9-77117758b5b9 no existe", res.msg())
        );

        verify(clientService, times(1)).getAllBooksOfClient(any(UUID.class), any(PageRequest.class));
    }


    @Test
    void getAllBooks_ShouldReturnEmptyList() throws Exception {
        when(clientService.getAllBooksOfClient(any(UUID.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of()));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/9def16db-362b-44c4-9fc9-77117758b6a2/books")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        PageResponse res = mapper.readValue(response.getContentAsString(), PageResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(0, res.totalElements()),
                () -> assertEquals(1, res.totalPages()),
                () -> assertEquals(0, res.content().size())
        );

        verify(clientService, times(1)).getAllBooksOfClient(any(UUID.class), any(PageRequest.class));
    }

    @Test
    void getAllBooks_ShouldReturnErrorReponse_withInvalidPage() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/9def16db-362b-44c4-9fc9-77117758b6a2/books?page=-1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name()))
                .andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El numero de pagina no debe ser menor a 0 y el tamano de la pagina debe ser mayor que 0", res.get("msg"))
        );
    }

    @Test
    void getAllBooks_ShouldReturnErrorReponse_withInvalidSize() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/9def16db-362b-44c4-9fc9-77117758b6a2/books?size=-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El numero de pagina no debe ser menor a 0 y el tamano de la pagina debe ser mayor que 0", res.get("msg"))
        );
    }


    @Test
    void updatePatchBook_ShouldReturnClientDto() throws Exception {
        when(clientService.addBookToClient(any(UUID.class), any(Long.class))).thenReturn(clientDtoTest);

        MockHttpServletResponse response = mockMvc.perform(patch(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b0/books/add?idBook=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ClientDto res = mapper.readValue(response.getContentAsString(), ClientDto.class);

        assertAll(
                () -> assertEquals(clientDtoTest.getId(), res.getId()),
                () -> assertEquals(clientDtoTest.getName(), res.getName()),
                () -> assertEquals(clientDtoTest.getSurname(), res.getSurname()),
                () -> assertEquals(clientDtoTest.getEmail(), res.getEmail()),
                () -> assertEquals(clientDtoTest.getPhone(), res.getPhone()),
                () -> assertEquals(clientDtoTest.getAddress(), res.getAddress()),
                () -> assertEquals(clientDtoTest.getImage(), res.getImage())
        );

        verify(clientService, times(1)).addBookToClient(any(UUID.class), any(Long.class));
    }

    @Test
    void updatePatchBook_ShouldReturnClientNotFound() throws Exception {
        when(clientService.addBookToClient(any(UUID.class), any(Long.class))).thenThrow(new ClientNotFound("id", UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b9")));

        MockHttpServletResponse response = mockMvc.perform(patch(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b9/books/add?idBook=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ErrorResponse res = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Client con id: 9def16db-362b-44c4-9fc9-77117758b5b9 no existe", res.msg())
        );

        verify(clientService, times(1)).addBookToClient(any(UUID.class), any(Long.class));
    }

    @Test
    void updatePatchBook_ShouldReturnBookNotFound() throws Exception {
        when(clientService.addBookToClient(any(UUID.class), any(Long.class))).thenThrow(new BookNotFoundException("No se ha encontrado el Book con el ID indicado"));

        MockHttpServletResponse response = mockMvc.perform(patch(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b0/books/add?idBook=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ErrorResponse res = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Libro no encontrado - No se ha encontrado el Book con el ID indicado", res.msg())
        );

        verify(clientService, times(1)).addBookToClient(any(UUID.class), any(Long.class));
    }

    @Test
    void updatePatchBookDelete() throws Exception {
        when(clientService.removeBookOfClient(any(UUID.class), any(Long.class))).thenReturn(clientDtoTest);

        MockHttpServletResponse response = mockMvc.perform(patch(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b0/books/remove?idBook=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ClientDto res = mapper.readValue(response.getContentAsString(), ClientDto.class);

        assertAll(
                () -> assertEquals(clientDtoTest.getId(), res.getId()),
                () -> assertEquals(clientDtoTest.getName(), res.getName()),
                () -> assertEquals(clientDtoTest.getSurname(), res.getSurname()),
                () -> assertEquals(clientDtoTest.getEmail(), res.getEmail()),
                () -> assertEquals(clientDtoTest.getPhone(), res.getPhone()),
                () -> assertEquals(clientDtoTest.getAddress(), res.getAddress()),
                () -> assertEquals(clientDtoTest.getImage(), res.getImage())
        );

        verify(clientService, times(1)).removeBookOfClient(any(UUID.class), any(Long.class));
    }

    @Test
    void updatePatchBookDelete_ShouldReturnClientNotFound() throws Exception {
        when(clientService.removeBookOfClient(any(UUID.class), any(Long.class))).thenThrow(new ClientNotFound("id", UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b9")));

        MockHttpServletResponse response = mockMvc.perform(patch(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b9/books/remove?idBook=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ErrorResponse res = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Client con id: 9def16db-362b-44c4-9fc9-77117758b5b9 no existe", res.msg())
        );

        verify(clientService, times(1)).removeBookOfClient(any(UUID.class), any(Long.class));
    }

    @Test
    void updatePatchBookDelete_ShouldReturnBookNotFound() throws Exception {
        when(clientService.removeBookOfClient(any(UUID.class), any(Long.class))).thenThrow(new BookNotFoundException("No se ha encontrado el Book con el ID indicado"));

        MockHttpServletResponse response = mockMvc.perform(patch(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b0/books/remove?idBook=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ErrorResponse res = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Libro no encontrado - No se ha encontrado el Book con el ID indicado", res.msg())
        );

        verify(clientService, times(1)).removeBookOfClient(any(UUID.class), any(Long.class));
    }


    @Test
    void updatePatchImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "file.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "dasd".getBytes()
        );

        when(clientService.updateImage(any(UUID.class), any(MultipartFile.class))).thenReturn(clientDtoTest);

        MockHttpServletResponse response = mockMvc.perform(multipart(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b0/image")
                        .file(file)
                        .with(req -> {
                            req.setMethod("PATCH");
                            return req;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andReturn().getResponse();

        ClientDto res = mapper.readValue(response.getContentAsString(), ClientDto.class);

        assertAll(
                () -> assertEquals(clientDtoTest.getId(), res.getId()),
                () -> assertEquals(clientDtoTest.getName(), res.getName()),
                () -> assertEquals(clientDtoTest.getSurname(), res.getSurname()),
                () -> assertEquals(clientDtoTest.getEmail(), res.getEmail()),
                () -> assertEquals(clientDtoTest.getPhone(), res.getPhone()),
                () -> assertEquals(clientDtoTest.getAddress(), res.getAddress()),
                () -> assertEquals(clientDtoTest.getImage(), res.getImage())
        );

        verify(clientService, times(1)).updateImage(any(UUID.class), any(MultipartFile.class));
    }

    @Test
    void updatePatchImage_ShouldReturnClientNotFound() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "file.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "dasd".getBytes()
        );

        when(clientService.updateImage(any(UUID.class), any(MultipartFile.class))).thenThrow(new ClientNotFound("id", UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b9")));

        MockHttpServletResponse response = mockMvc.perform(multipart(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b9/image")
                        .file(file)
                        .with(req -> {
                            req.setMethod("PATCH");
                            return req;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andReturn().getResponse();

        ErrorResponse res = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Client con id: 9def16db-362b-44c4-9fc9-77117758b5b9 no existe", res.msg())
        );

        verify(clientService, times(1)).updateImage(any(UUID.class), any(MultipartFile.class));
    }

    @Test
    void updatePatchImage_ShouldReturnErrorReponse_withInvalidImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "dasd".getBytes()
        );

        MockHttpServletResponse response = mockMvc.perform(multipart(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b0/image")
                        .file(file)
                        .with(req -> {
                            req.setMethod("PATCH");
                            return req;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El archivo no es una imagen o esta vacio", res.get("msg"))
        );
    }

    @Test
    void updatePatchImage_ShouldReturnErrorReponse_withEmptyImage() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "file.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "".getBytes()
        );

        MockHttpServletResponse response = mockMvc.perform(multipart(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b0/image")
                        .file(file)
                        .with(req -> {
                            req.setMethod("PATCH");
                            return req;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El archivo no es una imagen o esta vacio", res.get("msg"))
        );
    }

    @Test
    void updatePatchImage_ShouldReturnErrorReponse_withInvalidImageSize() throws Exception {
        long bytes = 11 * 1024L * 1024L;
        byte[] byteArray = new byte[(int) bytes];

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "file.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                byteArray
        );

        when(clientService.updateImage(any(UUID.class), any(MultipartFile.class))).thenThrow(new MaxUploadSizeExceededException(10));

        MockHttpServletResponse response = mockMvc.perform(multipart(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b0/image")
                        .file(file)
                        .with(req -> {
                            req.setMethod("PATCH");
                            return req;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andReturn().getResponse();


        ErrorResponse res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);
        System.out.println(res.msg());

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El tamaño del archivo supera el límite permitido. (10MB)", res.msg())
        );
    }

    @Test
    void addBookToClientAlreadyExistBook() throws Exception {
        when(clientService.addBookToClient(any(UUID.class), any(Long.class))).thenThrow(new ClientBookAlreadyExists("El libro con id: 1 ya existe en el cliente con id: 9def16db-362b-44c4-9fc9-77117758b5b9"));

        MockHttpServletResponse response = mockMvc.perform(patch(endpoint + "/9def16db-362b-44c4-9fc9-77117758b5b9/books/add?idBook=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ErrorResponse res = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.CONFLICT.value(), response.getStatus()),
                () -> assertEquals("El libro con id: 1 ya existe en el cliente con id: 9def16db-362b-44c4-9fc9-77117758b5b9", res.msg())
        );

        verify(clientService, times(1)).addBookToClient(any(UUID.class), any(Long.class));
    }


}
