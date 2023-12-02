package com.nullers.restbookstore.rest.shop.controllers;

import com.nullers.restbookstore.pagination.util.PaginationLinksUtils;
import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.book.model.Book;
import com.nullers.restbookstore.rest.category.model.Categoria;
import com.nullers.restbookstore.rest.client.exceptions.ClientNotFound;
import com.nullers.restbookstore.rest.client.model.Client;
import com.nullers.restbookstore.rest.common.Address;
import com.nullers.restbookstore.rest.common.PageableRequest;
import com.nullers.restbookstore.rest.publisher.model.Publisher;
import com.nullers.restbookstore.rest.shop.dto.CreateShopDto;
import com.nullers.restbookstore.rest.shop.dto.GetShopDto;
import com.nullers.restbookstore.rest.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.rest.shop.exceptions.ShopNotFoundException;
import com.nullers.restbookstore.rest.shop.model.Shop;
import com.nullers.restbookstore.rest.shop.services.ShopServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShopControllerWithoutMockMvcTest {

    @Mock
    private PaginationLinksUtils paginationLinksUtils = new PaginationLinksUtils();

    @Mock
    private ShopServiceImpl shopService;

    @InjectMocks
    private ShopRestControllerImpl shopRestController;


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

    MockHttpServletRequest requestMock;

    ShopControllerWithoutMockMvcTest() {
        requestMock = new MockHttpServletRequest();
        requestMock.setRequestURI("/api/shops");
        requestMock.setServerPort(8080);
    }


    @Test
    void getAllShops_ShouldReturnShops(){
        when(shopService.getAllShops(any(Optional.class), any(Optional.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(getShopDto)));

        var res = shopRestController.getAllShops(Optional.empty(), Optional.empty(), new PageableRequest(0, 10, "id", "ASC"), requestMock);

        assertAll(
                () -> assertEquals(1, res.getBody().content().size()),
                () -> assertEquals(200, res.getStatusCodeValue()),
                () -> assertEquals(shop.getId(), res.getBody().content().get(0).getId()),
                () -> assertEquals(shop.getName(), res.getBody().content().get(0).getName()),
                () -> assertEquals(shop.getBooks().stream().map(Book::getId).collect(Collectors.toSet()), res.getBody().content().get(0).getBooks_id()),
                () -> assertEquals(shop.getClients().stream().map(Client::getId).collect(Collectors.toSet()), res.getBody().content().get(0).getClients_id()),
                () -> assertEquals(shop.getLocation(), res.getBody().content().get(0).getLocation())
        );


        verify(shopService, times(1)).getAllShops(any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAllShops_ShouldReturnEmptyList(){
        when(shopService.getAllShops(any(Optional.class), any(Optional.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of()));

        var res = shopRestController.getAllShops(Optional.empty(), Optional.empty(), new PageableRequest(0, 10, "id", "ASC"), requestMock);

        assertAll(
                () -> assertEquals(0, res.getBody().content().size()),
                () -> assertEquals(200, res.getStatusCodeValue())
        );

        verify(shopService, times(1)).getAllShops(any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getAllShops_ShouldReturnShopsFilteredByName(){
        when(shopService.getAllShops(any(Optional.class), any(Optional.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(getShopDto)));

        var res = shopRestController.getAllShops(Optional.of("name"), Optional.empty(), new PageableRequest(0, 10, "id", "ASC"), requestMock);

        assertAll(
                () -> assertEquals(1, res.getBody().content().size()),
                () -> assertEquals(200, res.getStatusCodeValue()),
                () -> assertEquals(shop.getId(), res.getBody().content().get(0).getId()),
                () -> assertEquals(shop.getName(), res.getBody().content().get(0).getName()),
                () -> assertEquals(shop.getBooks().stream().map(Book::getId).collect(Collectors.toSet()), res.getBody().content().get(0).getBooks_id()),
                () -> assertEquals(shop.getClients().stream().map(Client::getId).collect(Collectors.toSet()), res.getBody().content().get(0).getClients_id()),
                () -> assertEquals(shop.getLocation(), res.getBody().content().get(0).getLocation())
        );

        verify(shopService, times(1)).getAllShops(any(Optional.class), any(Optional.class), any(PageRequest.class));
    }

    @Test
    void getShopById_ShouldReturnShop() throws ShopNotFoundException {
        when(shopService.getShopById(any(UUID.class))).thenReturn(getShopDto);

        var res = shopRestController.getShopById(UUID.randomUUID());

        assertAll(
                () -> assertEquals(shop.getId(), res.getBody().getId()),
                () -> assertEquals(shop.getName(), res.getBody().getName()),
                () -> assertEquals(shop.getBooks().stream().map(Book::getId).collect(Collectors.toSet()), res.getBody().getBooks_id()),
                () -> assertEquals(shop.getClients().stream().map(Client::getId).collect(Collectors.toSet()), res.getBody().getClients_id()),
                () -> assertEquals(shop.getLocation(), res.getBody().getLocation()),
                () -> assertEquals(200, res.getStatusCodeValue())
        );

        verify(shopService, times(1)).getShopById(any(UUID.class));
    }

    @Test
    void getShopById_ShouldThrowShopNotFoundException()  {
        when(shopService.getShopById(any(UUID.class))).thenThrow(new ShopNotFoundException("Tienda no encontrada con ID: " + shop.getId()));

        var res = assertThrows(ShopNotFoundException.class, () -> shopRestController.getShopById(UUID.randomUUID()));


        assertAll(
                () -> assertEquals("Tienda no encontrada - Tienda no encontrada con ID: " + shop.getId(), res.getMessage())
        );

        verify(shopService, times(1)).getShopById(any(UUID.class));
    }


    @Test
    void createShop_ShouldReturnShop(){
        when(shopService.createShop(any(CreateShopDto.class))).thenReturn(
                GetShopDto.builder()
                        .id(shop.getId())
                        .name(shop.getName())
                        .books_id(Set.of())
                        .clients_id(Set.of())
                        .location(shop.getLocation())
                        .build()
        );

        var res = shopRestController.createShop(CreateShopDto.builder()
                .name("name")
                .location(address)
                .build());


        assertAll(
                () -> assertEquals(shop.getId(), res.getBody().getId()),
                () -> assertEquals(shop.getName(), res.getBody().getName()),
                () -> assertTrue(res.getBody().getClients_id().isEmpty()),
                () -> assertTrue(res.getBody().getBooks_id().isEmpty()),
                () -> assertEquals(shop.getLocation(), res.getBody().getLocation()),
                () -> assertEquals(201, res.getStatusCodeValue())
        );

        verify(shopService, times(1)).createShop(any(CreateShopDto.class));
    }

    @Test
    void updateShop_ShouldReturnShop() throws ShopNotFoundException {
        when(shopService.updateShop(any(UUID.class), any(UpdateShopDto.class))).thenReturn(getShopDto);

        var res = shopRestController.updateShop(UUID.randomUUID(), updateShopDto);

        assertAll(
                () -> assertEquals(shop.getId(), res.getBody().getId()),
                () -> assertEquals(shop.getName(), res.getBody().getName()),
                () -> assertEquals(shop.getBooks().stream().map(Book::getId).collect(Collectors.toSet()), res.getBody().getBooks_id()),
                () -> assertEquals(shop.getClients().stream().map(Client::getId).collect(Collectors.toSet()), res.getBody().getClients_id()),
                () -> assertEquals(shop.getLocation(), res.getBody().getLocation()),
                () -> assertEquals(200, res.getStatusCodeValue())
        );

        verify(shopService, times(1)).updateShop(any(UUID.class), any(UpdateShopDto.class));
    }

    @Test
    void updateShop_ShouldThrowShopNotFoundException() {
        when(shopService.updateShop(any(UUID.class), any(UpdateShopDto.class))).thenThrow(new ShopNotFoundException("Tienda no encontrada con ID: " + shop.getId()));

        var res = assertThrows(ShopNotFoundException.class, () -> shopRestController.updateShop(UUID.randomUUID(), updateShopDto));

        assertAll(
                () -> assertEquals("Tienda no encontrada - Tienda no encontrada con ID: " + shop.getId(), res.getMessage())
        );

        verify(shopService, times(1)).updateShop(any(UUID.class), any(UpdateShopDto.class));
    }

    @Test
    void deleteShop_ShouldReturnNoContent() throws ShopNotFoundException {
        doNothing().when(shopService).deleteShop(any(UUID.class));

        var res = shopRestController.deleteShop(UUID.randomUUID());

        assertEquals(204, res.getStatusCodeValue());

        verify(shopService, times(1)).deleteShop(any(UUID.class));
    }

    @Test
    void deleteShop_ShouldThrowShopNotFoundException() {
        doThrow(new ShopNotFoundException("Tienda no encontrada con ID: " + shop.getId())).when(shopService).deleteShop(any(UUID.class));

        var res = assertThrows(ShopNotFoundException.class, () -> shopRestController.deleteShop(UUID.randomUUID()));

        assertAll(
                () -> assertEquals("Tienda no encontrada - Tienda no encontrada con ID: " + shop.getId(), res.getMessage())
        );

        verify(shopService, times(1)).deleteShop(any(UUID.class));
    }

    @Test
    void addBookToShop_ShouldReturnShop() throws ShopNotFoundException {
        when(shopService.addBookToShop(any(UUID.class), any(Long.class))).thenReturn(getShopDto);

        var res = shopRestController.addBookToShop(UUID.randomUUID(), 1L);

        assertAll(
                () -> assertEquals(shop.getId(), res.getBody().getId()),
                () -> assertEquals(shop.getName(), res.getBody().getName()),
                () -> assertEquals(shop.getBooks().stream().map(Book::getId).collect(Collectors.toSet()), res.getBody().getBooks_id()),
                () -> assertEquals(shop.getClients().stream().map(Client::getId).collect(Collectors.toSet()), res.getBody().getClients_id()),
                () -> assertEquals(shop.getLocation(), res.getBody().getLocation()),
                () -> assertEquals(200, res.getStatusCodeValue())
        );

        verify(shopService, times(1)).addBookToShop(any(UUID.class), any(Long.class));
    }

    @Test
    void addBookToShop_ShouldReturnShopNotFoundException(){
        when(shopService.addBookToShop(any(UUID.class), any(Long.class))).thenThrow(new ShopNotFoundException("Tienda no encontrada con ID: " + shop.getId()));

        var res = assertThrows(ShopNotFoundException.class, () -> shopRestController.addBookToShop(UUID.randomUUID(), 1L));

        assertAll(
                () -> assertEquals("Tienda no encontrada - Tienda no encontrada con ID: " + shop.getId(), res.getMessage())
        );

        verify(shopService, times(1)).addBookToShop(any(UUID.class), any(Long.class));
    }

    @Test
    void addBootToShoop_ShouldReturnBookNotFoundException(){
        when(shopService.addBookToShop(any(UUID.class), any(Long.class))).thenThrow(new BookNotFoundException(book.getId().toString()));

        var res = assertThrows(BookNotFoundException.class, () -> shopRestController.addBookToShop(UUID.randomUUID(), 1L));

        assertAll(
                () -> assertEquals("Libro no encontrado - " + book.getId(), res.getMessage())
        );

        verify(shopService, times(1)).addBookToShop(any(UUID.class), any(Long.class));
    }

    @Test
    void removeBookFromShop_ShouldReturnShop() throws ShopNotFoundException {
        when(shopService.removeBookFromShop(any(UUID.class), any(Long.class))).thenReturn(getShopDto);

        var res = shopRestController.removeBookFromShop(UUID.randomUUID(), 1L);

        assertAll(
                () -> assertEquals(shop.getId(), res.getBody().getId()),
                () -> assertEquals(shop.getName(), res.getBody().getName()),
                () -> assertEquals(shop.getBooks().stream().map(Book::getId).collect(Collectors.toSet()), res.getBody().getBooks_id()),
                () -> assertEquals(shop.getClients().stream().map(Client::getId).collect(Collectors.toSet()), res.getBody().getClients_id()),
                () -> assertEquals(shop.getLocation(), res.getBody().getLocation()),
                () -> assertEquals(200, res.getStatusCodeValue())
        );

        verify(shopService, times(1)).removeBookFromShop(any(UUID.class), any(Long.class));
    }

    @Test
    void removeBookFromShop_ShouldReturnShopNotFoundException(){
        when(shopService.removeBookFromShop(any(UUID.class), any(Long.class))).thenThrow(new ShopNotFoundException("Tienda no encontrada con ID: " + shop.getId()));

        var res = assertThrows(ShopNotFoundException.class, () -> shopRestController.removeBookFromShop(UUID.randomUUID(), 1L));

        assertAll(
                () -> assertEquals("Tienda no encontrada - Tienda no encontrada con ID: " + shop.getId(), res.getMessage())
        );

        verify(shopService, times(1)).removeBookFromShop(any(UUID.class), any(Long.class));
    }

    @Test
    void removeBookFromShop_ShouldReturnBookNotFoundException(){
        when(shopService.removeBookFromShop(any(UUID.class), any(Long.class))).thenThrow(new BookNotFoundException(book.getId().toString()));

        var res = assertThrows(BookNotFoundException.class, () -> shopRestController.removeBookFromShop(UUID.randomUUID(), 1L));

        assertAll(
                () -> assertEquals("Libro no encontrado - " + book.getId(), res.getMessage())
        );

        verify(shopService, times(1)).removeBookFromShop(any(UUID.class), any(Long.class));
    }

    @Test
    void addClientToShop_ShouldReturnShop() throws ShopNotFoundException {
        when(shopService.addClientToShop(any(UUID.class), any(UUID.class))).thenReturn(getShopDto);

        var res = shopRestController.addClientToShop(UUID.randomUUID(), UUID.randomUUID());

        assertAll(
                () -> assertEquals(shop.getId(), res.getBody().getId()),
                () -> assertEquals(shop.getName(), res.getBody().getName()),
                () -> assertEquals(shop.getBooks().stream().map(Book::getId).collect(Collectors.toSet()), res.getBody().getBooks_id()),
                () -> assertEquals(shop.getClients().stream().map(Client::getId).collect(Collectors.toSet()), res.getBody().getClients_id()),
                () -> assertEquals(shop.getLocation(), res.getBody().getLocation()),
                () -> assertEquals(200, res.getStatusCodeValue())
        );

        verify(shopService, times(1)).addClientToShop(any(UUID.class), any(UUID.class));
    }

    @Test
    void addClientToShop_ShouldReturnShopNotFoundException(){
        when(shopService.addClientToShop(any(UUID.class), any(UUID.class))).thenThrow(new ShopNotFoundException("Tienda no encontrada con ID: " + shop.getId()));

        var res = assertThrows(ShopNotFoundException.class, () -> shopRestController.addClientToShop(UUID.randomUUID(), UUID.randomUUID()));

        assertAll(
                () -> assertEquals("Tienda no encontrada - Tienda no encontrada con ID: " + shop.getId(), res.getMessage())
        );

        verify(shopService, times(1)).addClientToShop(any(UUID.class), any(UUID.class));
    }

    @Test
    void addClientToShop_ShouldReturnClientNotFoundException(){
        when(shopService.addClientToShop(any(UUID.class), any(UUID.class))).thenThrow(new ClientNotFound("id", clientTest.getId()));

        var res = assertThrows(ClientNotFound.class, () -> shopRestController.addClientToShop(UUID.randomUUID(), UUID.randomUUID()));

        assertAll(
                () -> assertEquals("Client con id: " + clientTest.getId() + " no existe", res.getMessage())
        );

        verify(shopService, times(1)).addClientToShop(any(UUID.class), any(UUID.class));
    }

    @Test
    void removeClientFromShop_ShouldReturnShop() throws ShopNotFoundException {
        when(shopService.removeClientFromShop(any(UUID.class), any(UUID.class))).thenReturn(getShopDto);

        var res = shopRestController.removeClientFromShop(UUID.randomUUID(), UUID.randomUUID());

        assertAll(
                () -> assertEquals(shop.getId(), res.getBody().getId()),
                () -> assertEquals(shop.getName(), res.getBody().getName()),
                () -> assertEquals(shop.getBooks().stream().map(Book::getId).collect(Collectors.toSet()), res.getBody().getBooks_id()),
                () -> assertEquals(shop.getClients().stream().map(Client::getId).collect(Collectors.toSet()), res.getBody().getClients_id()),
                () -> assertEquals(shop.getLocation(), res.getBody().getLocation()),
                () -> assertEquals(200, res.getStatusCodeValue())
        );

        verify(shopService, times(1)).removeClientFromShop(any(UUID.class), any(UUID.class));
    }

    @Test
    void removeClientFromShop_ShouldReturnShopNotFoundException(){
        when(shopService.removeClientFromShop(any(UUID.class), any(UUID.class))).thenThrow(new ShopNotFoundException("Tienda no encontrada con ID: " + shop.getId()));

        var res = assertThrows(ShopNotFoundException.class, () -> shopRestController.removeClientFromShop(UUID.randomUUID(), UUID.randomUUID()));

        assertAll(
                () -> assertEquals("Tienda no encontrada - Tienda no encontrada con ID: " + shop.getId(), res.getMessage())
        );

        verify(shopService, times(1)).removeClientFromShop(any(UUID.class), any(UUID.class));
    }

    @Test
    void removeClientFromShop_ShouldReturnClientNotFoundException(){
        when(shopService.removeClientFromShop(any(UUID.class), any(UUID.class))).thenThrow(new ClientNotFound("id", clientTest.getId()));

        var res = assertThrows(ClientNotFound.class, () -> shopRestController.removeClientFromShop(UUID.randomUUID(), UUID.randomUUID()));

        assertAll(
                () -> assertEquals("Client con id: " + clientTest.getId() + " no existe", res.getMessage())
        );

        verify(shopService, times(1)).removeClientFromShop(any(UUID.class), any(UUID.class));
    }




}
