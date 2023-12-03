package com.nullers.restbookstore.rest.shop.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nullers.restbookstore.pagination.models.ErrorResponse;
import com.nullers.restbookstore.pagination.models.PageResponse;
import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.book.model.Book;
import com.nullers.restbookstore.rest.category.model.Category;
import com.nullers.restbookstore.rest.client.exceptions.ClientNotFound;
import com.nullers.restbookstore.rest.client.model.Client;
import com.nullers.restbookstore.rest.common.Address;
import com.nullers.restbookstore.rest.publisher.model.Publisher;
import com.nullers.restbookstore.rest.shop.dto.CreateShopDto;
import com.nullers.restbookstore.rest.shop.dto.GetShopDto;
import com.nullers.restbookstore.rest.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.rest.shop.exceptions.ShopHasOrders;
import com.nullers.restbookstore.rest.shop.exceptions.ShopNotFoundException;
import com.nullers.restbookstore.rest.shop.mappers.ShopMapperImpl;
import com.nullers.restbookstore.rest.shop.model.Shop;
import com.nullers.restbookstore.rest.shop.services.ShopServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Clase ShopControllerImplTest
 */
@SpringBootTest(properties = "spring.config.name=application-test")
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
class ShopControllerImplTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ShopServiceImpl service;

    @MockBean
    ShopMapperImpl shopMapper;


    Publisher publisher = Publisher.builder()
            .id(1L)
            .name("name")
            .build();

    Category category = Category.builder()
            .id(UUID.fromString("a712c5f2-eb95-449a-9ec4-1aa55cdac9bc"))
            .name("Cat")
            .isActive(true)
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
            .category(category)
            .build();

    Address address = Address.builder()
            .street("Calle Falsa 123")
            .city("Springfield")
            .country("USA")
            .province("Springfield")
            .number("123")
            .postalCode("12345")
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


    Shop shop = Shop.builder()
            .id(UUID.fromString("b5f29063-77d8-4d5d-98ea-def0cc9ebc5f"))
            .name("name")
            .books(Set.of(book))
            .clients(Set.of(clientTest))
            .location(address)
            .build();

    UpdateShopDto updateShopDto = UpdateShopDto.builder()
            .name("name")
            .books(Set.of(book.getId()))
            .clients(Set.of(clientTest.getId()))
            .location(address)
            .build();

    GetShopDto getShopDto = GetShopDto.builder()
            .id(shop.getId())
            .name(shop.getName())
            .books_id(shop.getBooks().stream().map(Book::getId).collect(Collectors.toSet()))
            .clients_id(shop.getClients().stream().map(Client::getId).collect(Collectors.toSet()))
            .location(shop.getLocation())
            .build();
    String endpoint = "/api/shops";

    ShopControllerImplTest() {
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    @WithAnonymousUser
    void getAllShops_WithAnonymousUser_ShouldReturnForbidden() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                get(endpoint)
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(403, response.getStatus()),
                () -> assertEquals("Access Denied", response.getErrorMessage())
        );

        verify(service, times(0)).getAllShops(any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    @WithAnonymousUser
    void getShopById_WithAnonymousUser_ShouldReturnForbidden() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                get(endpoint + "/{id}", shop.getId())
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(403, response.getStatus()),
                () -> assertEquals("Access Denied", response.getErrorMessage())
        );

        verify(service, times(0)).getShopById(any(UUID.class));
    }

    @Test
    @WithAnonymousUser
    void createShop_WithAnonymousUser_ShouldReturnForbidden() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                post(endpoint)
                        .contentType("application/json")
                        .accept("application/json")
                        .content(mapper.writeValueAsString(shop))
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(403, response.getStatus()),
                () -> assertEquals("Access Denied", response.getErrorMessage())
        );

        verify(service, times(0)).createShop(any(CreateShopDto.class));
    }

    @Test
    @WithAnonymousUser
    void updateShop_WithAnonymousUser_ShouldReturnForbidden() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                put(endpoint + "/{id}", shop.getId())
                        .contentType("application/json")
                        .accept("application/json")
                        .content(mapper.writeValueAsString(updateShopDto))
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(403, response.getStatus()),
                () -> assertEquals("Access Denied", response.getErrorMessage())
        );

        verify(service, times(0)).updateShop(any(UUID.class), any(UpdateShopDto.class));
    }

    @Test
    @WithAnonymousUser
    void deleteShop_WithAnonymousUser_ShouldReturnForbidden() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                delete(endpoint + "/{id}", shop.getId())
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(403, response.getStatus()),
                () -> assertEquals("Access Denied", response.getErrorMessage())
        );

        verify(service, times(0)).deleteShop(any(UUID.class));
    }

    @Test
    @WithAnonymousUser
    void addBookToShop_WithAnonymousUser_ShouldReturnForbidden() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                patch(endpoint + "/{id}/books/{bookId}", shop.getId(), book.getId())
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(403, response.getStatus()),
                () -> assertEquals("Access Denied", response.getErrorMessage())
        );

        verify(service, times(0)).addBookToShop(any(UUID.class), any(Long.class));
    }

    @Test
    @WithAnonymousUser
    void removeBookFromShop_WithAnonymousUser_ShouldReturnForbidden() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                delete(endpoint + "/{id}/books/{bookId}", shop.getId(), book.getId())
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(403, response.getStatus()),
                () -> assertEquals("Access Denied", response.getErrorMessage())
        );

        verify(service, times(0)).removeBookFromShop(any(UUID.class), any(Long.class));
    }

    @Test
    @WithAnonymousUser
    void addClientToShop_WithAnonymousUser_ShouldReturnForbidden() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                patch(endpoint + "/{id}/clients/{clientId}", shop.getId(), clientTest.getId())
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(403, response.getStatus()),
                () -> assertEquals("Access Denied", response.getErrorMessage())
        );

        verify(service, times(0)).addClientToShop(any(UUID.class), any(UUID.class));
    }

    @Test
    @WithAnonymousUser
    void removeClientFromShop_WithAnonymousUser_ShouldReturnForbidden() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                delete(endpoint + "/{id}/clients/{clientId}", shop.getId(), clientTest.getId())
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(403, response.getStatus()),
                () -> assertEquals("Access Denied", response.getErrorMessage())
        );
        verify(service, times(0)).removeClientFromShop(any(UUID.class), any(UUID.class));
    }

    @Test
    void getAllShops_ShouldReturnShops() throws Exception {
        when(service.getAllShops(any(Optional.class), any(Optional.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(
                GetShopDto.builder()
                        .id(shop.getId())
                        .name(shop.getName())
                        .books_id(shop.getBooks().stream().map(Book::getId).collect(Collectors.toSet()))
                        .clients_id(shop.getClients().stream().map(Client::getId).collect(Collectors.toSet()))
                        .location(shop.getLocation())
                        .build()
        )));

        MockHttpServletResponse response = mockMvc.perform(
                get(endpoint)
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        PageResponse<GetShopDto> pageResponse = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructParametricType(PageResponse.class, GetShopDto.class));

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, pageResponse.totalElements()),
                () -> assertEquals(1, pageResponse.totalPages()),
                () -> assertEquals(1, pageResponse.content().size()),
                () -> assertEquals(shop.getId(), pageResponse.content().get(0).getId()),
                () -> assertEquals(shop.getName(), pageResponse.content().get(0).getName()),
                () -> assertTrue(pageResponse.content().get(0).getBooks_id().contains(book.getId())),
                () -> assertTrue(pageResponse.content().get(0).getClients_id().contains(clientTest.getId())),
                () -> assertEquals(shop.getLocation(), pageResponse.content().get(0).getLocation())
        );

        verify(service, times(1)).getAllShops(any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAllShops_ShouldReturnEmptyList() throws Exception {
        when(service.getAllShops(any(Optional.class), any(Optional.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of()));

        MockHttpServletResponse response = mockMvc.perform(
                get(endpoint)
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        PageResponse<GetShopDto> pageResponse = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructParametricType(PageResponse.class, GetShopDto.class));

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(0, pageResponse.totalElements()),
                () -> assertEquals(1, pageResponse.totalPages()),
                () -> assertEquals(0, pageResponse.content().size())
        );

        verify(service, times(1)).getAllShops(any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAllShops_ShouldReturnShopWithFiltererName() throws Exception {
        when(service.getAllShops(any(Optional.class), any(Optional.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(
                GetShopDto.builder()
                        .id(shop.getId())
                        .name(shop.getName())
                        .books_id(shop.getBooks().stream().map(Book::getId).collect(Collectors.toSet()))
                        .clients_id(shop.getClients().stream().map(Client::getId).collect(Collectors.toSet()))
                        .location(shop.getLocation())
                        .build()
        )));

        MockHttpServletResponse response = mockMvc.perform(
                get(endpoint)
                        .contentType("application/json")
                        .accept("application/json")
                        .param("name", "name")
        ).andReturn().getResponse();

        PageResponse<GetShopDto> pageResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructParametricType(PageResponse.class, GetShopDto.class));

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(1, pageResponse.totalElements()),
                () -> assertEquals(1, pageResponse.totalPages()),
                () -> assertEquals(1, pageResponse.content().size()),
                () -> assertEquals(shop.getId(), pageResponse.content().get(0).getId()),
                () -> assertEquals(shop.getName(), pageResponse.content().get(0).getName()),
                () -> assertTrue(pageResponse.content().get(0).getBooks_id().contains(book.getId())),
                () -> assertTrue(pageResponse.content().get(0).getClients_id().contains(clientTest.getId())),
                () -> assertEquals(shop.getLocation(), pageResponse.content().get(0).getLocation())
        );

        verify(service, times(1)).getAllShops(any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAllShops_ShouldReturnErrorWithInvalidPage() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                get(endpoint)
                        .contentType("application/json")
                        .accept("application/json")
                        .param("page", "-1")
        ).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");


        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertEquals(1, errors.size()),
                () -> assertEquals("La página no puede ser inferior a 0", errors.get("page"))
        );

        verify(service, times(0)).getAllShops(any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAllShops_ShouldReturnErrorWithInvalidSize() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                get(endpoint)
                        .contentType("application/json")
                        .accept("application/json")
                        .param("size", "-1")
        ).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertEquals(1, errors.size()),
                () -> assertEquals("El tamaño de la pagina no puede ser inferior a 1", errors.get("size"))
        );

        verify(service, times(0)).getAllShops(any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getShopById_ShouldReturnShop() throws Exception {
        when(service.getShopById(any(UUID.class))).thenReturn(getShopDto);

        MockHttpServletResponse response = mockMvc.perform(
                get(endpoint + "/{id}", shop.getId())
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        GetShopDto shopResponse = mapper.readValue(response.getContentAsString(), GetShopDto.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(shop.getId(), shopResponse.getId()),
                () -> assertEquals(shop.getName(), shopResponse.getName()),
                () -> assertEquals(getShopDto.getBooks_id(), shopResponse.getBooks_id()),
                () -> assertEquals(getShopDto.getClients_id(), shopResponse.getClients_id()),
                () -> assertEquals(shop.getLocation(), shopResponse.getLocation())
        );

        verify(service, times(1)).getShopById(any(UUID.class));
    }

    @Test
    void getShopById_ShouldReturnShopNotFound() throws Exception {
        when(service.getShopById(any(UUID.class))).thenThrow(new ShopNotFoundException("Tienda no encontrada con ID: " + shop.getId()));

        MockHttpServletResponse response = mockMvc.perform(
                get(endpoint + "/{id}", shop.getId())
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(404, response.getStatus()),
                () -> assertEquals("Tienda no encontrada - " + "Tienda no encontrada con ID: " + shop.getId(), errorResponse.msg())
        );

        verify(service, times(1)).getShopById(any(UUID.class));
    }

    @Test
    void createShop_ShouldReturnShop() throws Exception {
        when(service.createShop(any(CreateShopDto.class))).thenReturn(getShopDto);

        MockHttpServletResponse response = mockMvc.perform(
                post(endpoint)
                        .contentType("application/json")
                        .accept("application/json")
                        .content(mapper.writeValueAsString(shop))
        ).andReturn().getResponse();

        GetShopDto shopResponse = mapper.readValue(response.getContentAsString(), GetShopDto.class);

        assertAll(
                () -> assertEquals(201, response.getStatus()),
                () -> assertEquals(shop.getId(), shopResponse.getId()),
                () -> assertEquals(shop.getName(), shopResponse.getName()),
                () -> assertEquals(getShopDto.getBooks_id(), shopResponse.getBooks_id()),
                () -> assertEquals(getShopDto.getClients_id(), shopResponse.getClients_id()),
                () -> assertEquals(shop.getLocation(), shopResponse.getLocation())
        );

        verify(service, times(1)).createShop(any(CreateShopDto.class));
    }

    @Test
    void createShop_ShouldReturnErrorWithInvalidName() throws Exception {
        CreateShopDto createShopDto = CreateShopDto.builder()
                .name("")
                .location(address)
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                post(endpoint)
                        .contentType("application/json")
                        .accept("application/json")
                        .content(mapper.writeValueAsString(createShopDto))
        ).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertEquals(1, errors.size()),
                () -> assertEquals("El nombre no puede estar vacío", errors.get("name"))
        );

        verify(service, times(0)).createShop(any(CreateShopDto.class));
    }

    @Test
    void createShop_ShouldReturnErrorWithInvalidLocation() throws Exception {
        CreateShopDto createShopDto = CreateShopDto.builder()
                .name("name")
                .location(null)
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                post(endpoint)
                        .contentType("application/json")
                        .accept("application/json")
                        .content(mapper.writeValueAsString(createShopDto))
        ).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertEquals(1, errors.size()),
                () -> assertEquals("La ubicación no puede estar vacía", errors.get("location"))
        );

        verify(service, times(0)).createShop(any(CreateShopDto.class));
    }

    @Test
    void createShop_ShouldReturnErrorWithInvalidLocationErrorAll() throws Exception {
        CreateShopDto createShopDto = CreateShopDto.builder()
                .name("tienda 1")
                .location(Address.builder().build())
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                post(endpoint)
                        .contentType("application/json")
                        .accept("application/json")
                        .content(mapper.writeValueAsString(createShopDto))
        ).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");


        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertEquals(6, errors.size()),
                () -> assertEquals("La calle no puede estar vacía", errors.get("location.street")),
                () -> assertEquals("El número no puede estar vacío", errors.get("location.number")),
                () -> assertEquals("La ciudad no puede estar vacía", errors.get("location.city")),
                () -> assertEquals("La provincia no puede estar vacía", errors.get("location.province")),
                () -> assertEquals("El país no puede estar vacío", errors.get("location.country")),
                () -> assertEquals("El código postal no puede estar vacío", errors.get("location.postalCode"))
        );

        verify(service, times(0)).createShop(any(CreateShopDto.class));
    }

    @Test
    void createShop_ShouldReturnErrorWithInvalidLocationErrorAllOther() throws Exception {
        CreateShopDto createShopDto = CreateShopDto.builder()
                .name("tienda 1")
                .location(Address.builder()
                        .street("Ca")
                        .city("Sp")
                        .country("USA")
                        .province("Sp")
                        .number("23")
                        .postalCode("145")
                        .build())
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                post(endpoint)
                        .contentType("application/json")
                        .accept("application/json")
                        .content(mapper.writeValueAsString(createShopDto))
        ).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertEquals(4, errors.size()),
                () -> assertEquals("La calle debe tener al menos 3 caracteres y máximo 200", errors.get("location.street")),
                () -> assertEquals("La ciudad debe tener al menos 3 caracteres", errors.get("location.city")),
                () -> assertEquals("El código postal debe tener 5 dígitos", errors.get("location.postalCode")),
                () -> assertEquals("La provincia debe tener al menos 3 caracteres", errors.get("location.province"))
        );

        verify(service, times(0)).createShop(any(CreateShopDto.class));
    }

    @Test
    void updateShop_ShouldReturnShop() throws Exception {
        when(service.updateShop(any(UUID.class), any(UpdateShopDto.class))).thenReturn(getShopDto);

        MockHttpServletResponse response = mockMvc.perform(
                put(endpoint + "/{id}", shop.getId())
                        .contentType("application/json")
                        .accept("application/json")
                        .content(mapper.writeValueAsString(updateShopDto))
        ).andReturn().getResponse();

        GetShopDto shopResponse = mapper.readValue(response.getContentAsString(), GetShopDto.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(shop.getId(), shopResponse.getId()),
                () -> assertEquals(shop.getName(), shopResponse.getName()),
                () -> assertEquals(getShopDto.getBooks_id(), shopResponse.getBooks_id()),
                () -> assertEquals(getShopDto.getClients_id(), shopResponse.getClients_id()),
                () -> assertEquals(shop.getLocation(), shopResponse.getLocation())
        );

        verify(service, times(1)).updateShop(any(UUID.class), any(UpdateShopDto.class));
    }

    @Test
    void updateShop_ShouldReturnErrorWithInvalidName() throws Exception {
        UpdateShopDto updateShopDto = UpdateShopDto.builder()
                .name("")
                .location(address)
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                put(endpoint + "/{id}", shop.getId())
                        .contentType("application/json")
                        .accept("application/json")
                        .content(mapper.writeValueAsString(updateShopDto))
        ).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertEquals(1, errors.size()),
                () -> assertEquals("El nombre no puede estar vacío", errors.get("name"))
        );

        verify(service, times(0)).updateShop(any(UUID.class), any(UpdateShopDto.class));
    }

    @Test
    void updateShop_ShouldReturnErrorWithInvalidLocation() throws Exception {
        UpdateShopDto updateShopDto = UpdateShopDto.builder()
                .name("name")
                .location(null)
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                put(endpoint + "/{id}", shop.getId())
                        .contentType("application/json")
                        .accept("application/json")
                        .content(mapper.writeValueAsString(updateShopDto))
        ).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertEquals(1, errors.size()),
                () -> assertEquals("La ubicación no puede estar vacía", errors.get("location"))
        );

        verify(service, times(0)).updateShop(any(UUID.class), any(UpdateShopDto.class));
    }

    @Test
    void updateShop_ShouldReturnErrorWithInvalidLocationErrorAll() throws Exception {
        UpdateShopDto updateShopDto = UpdateShopDto.builder()
                .name("tienda 1")
                .location(Address.builder().build())
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                put(endpoint + "/{id}", shop.getId())
                        .contentType("application/json")
                        .accept("application/json")
                        .content(mapper.writeValueAsString(updateShopDto))
        ).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertEquals(6, errors.size()),
                () -> assertEquals("La calle no puede estar vacía", errors.get("location.street")),
                () -> assertEquals("El número no puede estar vacío", errors.get("location.number")),
                () -> assertEquals("La ciudad no puede estar vacía", errors.get("location.city")),
                () -> assertEquals("La provincia no puede estar vacía", errors.get("location.province")),
                () -> assertEquals("El país no puede estar vacío", errors.get("location.country")),
                () -> assertEquals("El código postal no puede estar vacío", errors.get("location.postalCode"))
        );

        verify(service, times(0)).updateShop(any(UUID.class), any(UpdateShopDto.class));
    }

    @Test
    void updateShop_ShouldReturnErrorWithInvalidLocationErrorAllOther() throws Exception {
        UpdateShopDto updateShopDto = UpdateShopDto.builder()
                .name("tienda 1")
                .location(Address.builder()
                        .street("Ca")
                        .city("Sp")
                        .country("USA")
                        .province("Sp")
                        .number("23")
                        .postalCode("145")
                        .build())
                .build();

        MockHttpServletResponse response = mockMvc.perform(
                put(endpoint + "/{id}", shop.getId())
                        .contentType("application/json")
                        .accept("application/json")
                        .content(mapper.writeValueAsString(updateShopDto))
        ).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertEquals(4, errors.size()),
                () -> assertEquals("La calle debe tener al menos 3 caracteres y máximo 200", errors.get("location.street")),
                () -> assertEquals("La ciudad debe tener al menos 3 caracteres", errors.get("location.city")),
                () -> assertEquals("El código postal debe tener 5 dígitos", errors.get("location.postalCode")),
                () -> assertEquals("La provincia debe tener al menos 3 caracteres", errors.get("location.province"))
        );

        verify(service, times(0)).updateShop(any(UUID.class), any(UpdateShopDto.class));
    }

    @Test
    void updateShop_ShouldReturnShopNotFound() throws Exception {
        when(service.updateShop(any(UUID.class), any(UpdateShopDto.class))).thenThrow(new ShopNotFoundException("Tienda no encontrada con ID: " + shop.getId()));

        MockHttpServletResponse response = mockMvc.perform(
                put(endpoint + "/{id}", shop.getId())
                        .contentType("application/json")
                        .accept("application/json")
                        .content(mapper.writeValueAsString(updateShopDto))
        ).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(404, response.getStatus()),
                () -> assertEquals("Tienda no encontrada - " + "Tienda no encontrada con ID: " + shop.getId(), errorResponse.msg())
        );

        verify(service, times(1)).updateShop(any(UUID.class), any(UpdateShopDto.class));
    }

    @Test
    void deleteShop_ShouldReturnCreated() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                delete(endpoint + "/{id}", shop.getId())
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        assertAll(
                () -> assertEquals(204, response.getStatus())
        );

        verify(service, times(1)).deleteShop(any(UUID.class));
    }

    @Test
    void deleteShop_ShouldReturnShopNotFound() throws Exception {
        doThrow(new BookNotFoundException("Tienda no encontrada con ID: " + shop.getId())).when(service).deleteShop(any(UUID.class));

        MockHttpServletResponse response = mockMvc.perform(
                delete(endpoint + "/{id}", shop.getId())
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(404, response.getStatus()),
                () -> assertEquals("Libro no encontrado - Tienda no encontrada con ID: b5f29063-77d8-4d5d-98ea-def0cc9ebc5f", errorResponse.msg())
        );

        verify(service, times(1)).deleteShop(any(UUID.class));
    }

    @Test
    void deleteShop_ShouldReturnShopHasOrders() throws Exception {
        doThrow(new ShopHasOrders("La tienda no se puede eliminar porque tiene pedidos asociados")).when(service).deleteShop(any(UUID.class));

        MockHttpServletResponse response = mockMvc.perform(
                delete(endpoint + "/{id}", shop.getId())
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(400, response.getStatus()),
                () -> assertEquals("La tienda no se puede eliminar porque tiene pedidos asociados", errorResponse.msg())
        );

        verify(service, times(1)).deleteShop(any(UUID.class));
    }

    @Test
    void addBookToShop_ShouldReturnGetShopDto() throws Exception {
        when(service.addBookToShop(any(UUID.class), any(Long.class))).thenReturn(getShopDto);

        MockHttpServletResponse response = mockMvc.perform(
                patch(endpoint + "/{id}/books/{bookId}", shop.getId(), book.getId())
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        GetShopDto shopResponse = mapper.readValue(response.getContentAsString(), GetShopDto.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(shop.getId(), shopResponse.getId()),
                () -> assertEquals(shop.getName(), shopResponse.getName()),
                () -> assertEquals(getShopDto.getBooks_id(), shopResponse.getBooks_id()),
                () -> assertEquals(getShopDto.getClients_id(), shopResponse.getClients_id()),
                () -> assertEquals(shop.getLocation(), shopResponse.getLocation())
        );

        verify(service, times(1)).addBookToShop(any(UUID.class), any(Long.class));
    }

    @Test
    void addBookToShop_ShouldReturnShopNotFound() throws Exception {
        when(service.addBookToShop(any(UUID.class), any(Long.class))).thenThrow(new ShopNotFoundException("Tienda no encontrada con ID: " + shop.getId()));

        MockHttpServletResponse response = mockMvc.perform(
                patch(endpoint + "/{id}/books/{bookId}", shop.getId(), book.getId())
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(404, response.getStatus()),
                () -> assertEquals("Tienda no encontrada - " + "Tienda no encontrada con ID: " + shop.getId(), errorResponse.msg())
        );

        verify(service, times(1)).addBookToShop(any(UUID.class), any(Long.class));
    }

    @Test
    void addBookToShop_ShouldReturnBookNotFound() throws Exception {
        when(service.addBookToShop(any(UUID.class), any(Long.class))).thenThrow(new BookNotFoundException("Libro no encontrado con ID: " + book.getId()));

        MockHttpServletResponse response = mockMvc.perform(
                patch(endpoint + "/{id}/books/{bookId}", shop.getId(), book.getId())
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(404, response.getStatus()),
                () -> assertEquals("Libro no encontrado - " + "Libro no encontrado con ID: " + book.getId(), errorResponse.msg())
        );

        verify(service, times(1)).addBookToShop(any(UUID.class), any(Long.class));
    }

    @Test
    void removeBookFromShop_ShouldReturnGetShopDto() throws Exception {
        when(service.removeBookFromShop(any(UUID.class), any(Long.class))).thenReturn(getShopDto);

        MockHttpServletResponse response = mockMvc.perform(
                delete(endpoint + "/{id}/books/{bookId}", shop.getId(), book.getId())
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        GetShopDto shopResponse = mapper.readValue(response.getContentAsString(), GetShopDto.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(shop.getId(), shopResponse.getId()),
                () -> assertEquals(shop.getName(), shopResponse.getName()),
                () -> assertEquals(getShopDto.getBooks_id(), shopResponse.getBooks_id()),
                () -> assertEquals(getShopDto.getClients_id(), shopResponse.getClients_id()),
                () -> assertEquals(shop.getLocation(), shopResponse.getLocation())
        );

        verify(service, times(1)).removeBookFromShop(any(UUID.class), any(Long.class));
    }

    @Test
    void removeBookFromShop_ShouldReturnShopNotFound() throws Exception {
        when(service.removeBookFromShop(any(UUID.class), any(Long.class))).thenThrow(new ShopNotFoundException("Tienda no encontrada con ID: " + shop.getId()));

        MockHttpServletResponse response = mockMvc.perform(
                delete(endpoint + "/{id}/books/{bookId}", shop.getId(), book.getId())
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(404, response.getStatus()),
                () -> assertEquals("Tienda no encontrada - " + "Tienda no encontrada con ID: " + shop.getId(), errorResponse.msg())
        );

        verify(service, times(1)).removeBookFromShop(any(UUID.class), any(Long.class));
    }

    @Test
    void removeBookFromShop_ShouldReturnBookNotFound() throws Exception {
        when(service.removeBookFromShop(any(UUID.class), any(Long.class))).thenThrow(new BookNotFoundException("Libro no encontrado con ID: " + book.getId()));

        MockHttpServletResponse response = mockMvc.perform(
                delete(endpoint + "/{id}/books/{bookId}", shop.getId(), book.getId())
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(404, response.getStatus()),
                () -> assertEquals("Libro no encontrado - " + "Libro no encontrado con ID: " + book.getId(), errorResponse.msg())
        );

        verify(service, times(1)).removeBookFromShop(any(UUID.class), any(Long.class));
    }

    @Test
    void addClientToShop_ShouldReturnGetShopDto() throws Exception {
        when(service.addClientToShop(any(UUID.class), any(UUID.class))).thenReturn(getShopDto);

        MockHttpServletResponse response = mockMvc.perform(
                patch(endpoint + "/{id}/clients/{clientId}", shop.getId(), clientTest.getId())
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        GetShopDto shopResponse = mapper.readValue(response.getContentAsString(), GetShopDto.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(shop.getId(), shopResponse.getId()),
                () -> assertEquals(shop.getName(), shopResponse.getName()),
                () -> assertEquals(getShopDto.getBooks_id(), shopResponse.getBooks_id()),
                () -> assertEquals(getShopDto.getClients_id(), shopResponse.getClients_id()),
                () -> assertEquals(shop.getLocation(), shopResponse.getLocation())
        );

        verify(service, times(1)).addClientToShop(any(UUID.class), any(UUID.class));
    }

    @Test
    void addClientToShop_ShouldReturnShopNotFound() throws Exception {
        when(service.addClientToShop(any(UUID.class), any(UUID.class))).thenThrow(new ShopNotFoundException("Tienda no encontrada con ID: " + shop.getId()));

        MockHttpServletResponse response = mockMvc.perform(
                patch(endpoint + "/{id}/clients/{clientId}", shop.getId(), clientTest.getId())
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(404, response.getStatus()),
                () -> assertEquals("Tienda no encontrada - " + "Tienda no encontrada con ID: " + shop.getId(), errorResponse.msg())
        );

        verify(service, times(1)).addClientToShop(any(UUID.class), any(UUID.class));
    }

    @Test
    void addClientToShop_ShouldReturnClientNotFound() throws Exception {
        when(service.addClientToShop(any(UUID.class), any(UUID.class))).thenThrow(new ClientNotFound("id", clientTest.getId()));

        MockHttpServletResponse response = mockMvc.perform(
                patch(endpoint + "/{id}/clients/{clientId}", shop.getId(), clientTest.getId())
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(404, response.getStatus()),
                () -> assertEquals("Client con id: " + clientTest.getId() + " no existe", errorResponse.msg())
        );

        verify(service, times(1)).addClientToShop(any(UUID.class), any(UUID.class));
    }

    @Test
    void removeClientFromShop_ShouldReturnGetShopDto() throws Exception {
        when(service.removeClientFromShop(any(UUID.class), any(UUID.class))).thenReturn(getShopDto);

        MockHttpServletResponse response = mockMvc.perform(
                delete(endpoint + "/{id}/clients/{clientId}", shop.getId(), clientTest.getId())
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        GetShopDto shopResponse = mapper.readValue(response.getContentAsString(), GetShopDto.class);

        assertAll(
                () -> assertEquals(200, response.getStatus()),
                () -> assertEquals(shop.getId(), shopResponse.getId()),
                () -> assertEquals(shop.getName(), shopResponse.getName()),
                () -> assertEquals(getShopDto.getBooks_id(), shopResponse.getBooks_id()),
                () -> assertEquals(getShopDto.getClients_id(), shopResponse.getClients_id()),
                () -> assertEquals(shop.getLocation(), shopResponse.getLocation())
        );

        verify(service, times(1)).removeClientFromShop(any(UUID.class), any(UUID.class));
    }

    @Test
    void removeClientFromShop_ShouldReturnShopNotFound() throws Exception {
        when(service.removeClientFromShop(any(UUID.class), any(UUID.class))).thenThrow(new ShopNotFoundException("Tienda no encontrada con ID: " + shop.getId()));

        MockHttpServletResponse response = mockMvc.perform(
                delete(endpoint + "/{id}/clients/{clientId}", shop.getId(), clientTest.getId())
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(404, response.getStatus()),
                () -> assertEquals("Tienda no encontrada - " + "Tienda no encontrada con ID: " + shop.getId(), errorResponse.msg())
        );

        verify(service, times(1)).removeClientFromShop(any(UUID.class), any(UUID.class));
    }

    @Test
    void removeClientFromShop_ShouldReturnClientNotFound() throws Exception {
        when(service.removeClientFromShop(any(UUID.class), any(UUID.class))).thenThrow(new ClientNotFound("id", clientTest.getId()));

        MockHttpServletResponse response = mockMvc.perform(
                delete(endpoint + "/{id}/clients/{clientId}", shop.getId(), clientTest.getId())
                        .contentType("application/json")
                        .accept("application/json")
        ).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(404, response.getStatus()),
                () -> assertEquals("Client con id: " + clientTest.getId() + " no existe", errorResponse.msg())
        );

        verify(service, times(1)).removeClientFromShop(any(UUID.class), any(UUID.class));
    }


}
