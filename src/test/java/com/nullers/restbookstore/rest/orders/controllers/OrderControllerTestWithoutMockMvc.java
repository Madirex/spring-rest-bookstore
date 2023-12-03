package com.nullers.restbookstore.rest.orders.controllers;

import com.nullers.restbookstore.pagination.util.PaginationLinksUtils;
import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.book.model.Book;
import com.nullers.restbookstore.rest.category.model.Category;
import com.nullers.restbookstore.rest.client.exceptions.ClientNotFound;
import com.nullers.restbookstore.rest.client.model.Client;
import com.nullers.restbookstore.rest.common.Address;
import com.nullers.restbookstore.rest.common.PageableRequest;
import com.nullers.restbookstore.rest.orders.dto.OrderCreateDto;
import com.nullers.restbookstore.rest.orders.exceptions.OrderBadPriceException;
import com.nullers.restbookstore.rest.orders.exceptions.OrderNotFoundException;
import com.nullers.restbookstore.rest.orders.exceptions.OrderNotItemsExceptions;
import com.nullers.restbookstore.rest.orders.exceptions.OrderNotStockException;
import com.nullers.restbookstore.rest.orders.models.Order;
import com.nullers.restbookstore.rest.orders.models.OrderLine;
import com.nullers.restbookstore.rest.orders.services.OrderServiceImpl;
import com.nullers.restbookstore.rest.publisher.model.Publisher;
import com.nullers.restbookstore.rest.shop.exceptions.ShopNotFoundException;
import com.nullers.restbookstore.rest.shop.model.Shop;
import com.nullers.restbookstore.rest.user.exceptions.UserNotFound;
import com.nullers.restbookstore.rest.user.models.User;
import com.nullers.restbookstore.rest.user.models.Role;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTestWithoutMockMvc {

    @InjectMocks
    private OrderRestController orderController;

    @Mock
    private OrderServiceImpl orderService;

    @Mock
    private PaginationLinksUtils paginationLinksUtils;


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

    private final User userTest = User.builder()
            .id(UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b9"))
            .name("Daniel")
            .email("daniel@gmail.com")
            .password("123456789")
            .surname("García")
            .isDeleted(false)
            .roles(Set.of(Role.USER))
            .build();

    Shop shop = Shop.builder()
            .id(UUID.fromString("b5f29063-77d8-4d5d-98ea-def0cc9ebc5f"))
            .name("name")
            .books(Set.of(book))
            .clients(Set.of(clientTest))
            .location(address)
            .build();


    Order order = Order.builder()
            .id(new ObjectId())
            .userId(userTest.getId())
            .clientId(clientTest.getId())
            .shopId(shop.getId())
            .isDeleted(false)
            .orderLines(List.of(orderLine, orderLine2))
            .updatedAt(LocalDateTime.now())
            .createdAt(LocalDateTime.now())
            .total(2.0)
            .totalBooks(2)
            .build();


    OrderCreateDto orderCreateDto = OrderCreateDto.builder()
            .userId(userTest.getId())
            .clientId(clientTest.getId())
            .orderLines(List.of(orderLine, orderLine2))
            .shopId(shop.getId())
            .build();

    MockHttpServletRequest requestMock;

    @BeforeEach
    void setUp() {
        requestMock = new MockHttpServletRequest();
        requestMock.setRequestURI("/api/orders");
        requestMock.setServerPort(8080);
    }

    @Test
    void getAllOrders_ShouldReturnOrders() {
        when(orderService.getAllOrders(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(order)));

        var res = orderController.getAllOrders(
                new PageableRequest(0, 10, "id", "asc"), requestMock
        );

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(1, res.getBody().totalPages()),
                () -> assertEquals(1, res.getBody().totalElements()),
                () -> assertEquals(order.getId().toHexString(), res.getBody().content().get(0).getId().toHexString()),
                () -> assertEquals(order.getTotal(), res.getBody().content().get(0).getTotal()),
                () -> assertEquals(order.getTotalBooks(), res.getBody().content().get(0).getTotalBooks()),
                () -> assertEquals(order.getUserId(), res.getBody().content().get(0).getUserId()),
                () -> assertEquals(order.getClientId(), res.getBody().content().get(0).getClientId()),
                () -> assertEquals(order.getShopId(), res.getBody().content().get(0).getShopId()),
                () -> assertEquals(order.getOrderLines().size(), res.getBody().content().get(0).getOrderLines().size()),
                () -> assertEquals(order.getCreatedAt(), res.getBody().content().get(0).getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), res.getBody().content().get(0).getUpdatedAt()),
                () -> assertEquals(order.getIsDeleted(), res.getBody().content().get(0).getIsDeleted()),
                () -> assertEquals(200, res.getStatusCodeValue())
        );
    }


    @Test
    void getAllOrder_ShouldReturnEmptyPage() {
        when(orderService.getAllOrders(any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        var res = orderController.getAllOrders(
                new PageableRequest(0, 10, "id", "asc"), requestMock
        );

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(1, res.getBody().totalPages()),
                () -> assertEquals(0, res.getBody().totalElements()),
                () -> assertEquals(0, res.getBody().content().size()),
                () -> assertEquals(200, res.getStatusCodeValue())
        );

        verify(orderService, times(1)).getAllOrders(any(Pageable.class));
    }


    @Test
    void getOrderById_ShouldReturnOrder() {
        when(orderService.getOrderById(any(ObjectId.class))).thenReturn(order);

        var res = orderController.getOrderById(order.getId());

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(order.getId().toHexString(), res.getBody().getId().toHexString()),
                () -> assertEquals(order.getTotal(), res.getBody().getTotal()),
                () -> assertEquals(order.getTotalBooks(), res.getBody().getTotalBooks()),
                () -> assertEquals(order.getUserId(), res.getBody().getUserId()),
                () -> assertEquals(order.getClientId(), res.getBody().getClientId()),
                () -> assertEquals(order.getShopId(), res.getBody().getShopId()),
                () -> assertEquals(order.getOrderLines().size(), res.getBody().getOrderLines().size()),
                () -> assertEquals(order.getCreatedAt(), res.getBody().getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), res.getBody().getUpdatedAt()),
                () -> assertEquals(order.getIsDeleted(), res.getBody().getIsDeleted()),
                () -> assertEquals(200, res.getStatusCodeValue())
        );

        verify(orderService, times(1)).getOrderById(any(ObjectId.class));
    }

    @Test
    void getOrderById_ShouldReturnOrderNotFound() {
        when(orderService.getOrderById(any(ObjectId.class))).thenThrow(new OrderNotFoundException(order.getId()));


        var res = assertThrows(OrderNotFoundException.class, () -> orderController.getOrderById(order.getId()));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("El pedido con id " + order.getId() + " no existe", res.getMessage())
        );

        verify(orderService, times(1)).getOrderById(any(ObjectId.class));
    }

    @Test
    void createOrder_ShouldReturnOrder() {
        when(orderService.createOrder(any(OrderCreateDto.class))).thenReturn(order);

        var res = orderController.createOrder(orderCreateDto);

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(order.getId().toHexString(), res.getBody().getId().toHexString()),
                () -> assertEquals(order.getTotal(), res.getBody().getTotal()),
                () -> assertEquals(order.getTotalBooks(), res.getBody().getTotalBooks()),
                () -> assertEquals(order.getUserId(), res.getBody().getUserId()),
                () -> assertEquals(order.getClientId(), res.getBody().getClientId()),
                () -> assertEquals(order.getShopId(), res.getBody().getShopId()),
                () -> assertEquals(order.getOrderLines().size(), res.getBody().getOrderLines().size()),
                () -> assertEquals(order.getCreatedAt(), res.getBody().getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), res.getBody().getUpdatedAt()),
                () -> assertEquals(order.getIsDeleted(), res.getBody().getIsDeleted()),
                () -> assertEquals(201, res.getStatusCodeValue())
        );

        verify(orderService, times(1)).createOrder(any(OrderCreateDto.class));
    }

    @Test
    void createOrder_ShouldReturnClientNotFoundExceptions() {
        when(orderService.createOrder(any(OrderCreateDto.class))).thenThrow(new ClientNotFound("id", clientTest.getId()));

        var res = assertThrows(ClientNotFound.class, () -> orderController.createOrder(orderCreateDto));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("Client con id: " + clientTest.getId() + " no existe", res.getMessage())
        );

        verify(orderService, times(1)).createOrder(any(OrderCreateDto.class));
    }

    @Test
    void createOrder_ShouldReturnUserNotFoundExceptions() {
        when(orderService.createOrder(any(OrderCreateDto.class))).thenThrow(new UserNotFound(userTest.getId()));

        var res = assertThrows(UserNotFound.class, () -> orderController.createOrder(orderCreateDto));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("Usuario con id" + userTest.getId() + " no encontrado", res.getMessage())
        );

        verify(orderService, times(1)).createOrder(any(OrderCreateDto.class));
    }

    @Test
    void createOrder_ShouldReturnShopNotFoundExceptions() {
        when(orderService.createOrder(any(OrderCreateDto.class))).thenThrow(new ShopNotFoundException("La tienda con id " + shop.getId() + " no existe"));

        var res = assertThrows(ShopNotFoundException.class, () -> orderController.createOrder(orderCreateDto));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("Tienda no encontrada - " + "La tienda con id " + shop.getId() + " no existe", res.getMessage())
        );

        verify(orderService, times(1)).createOrder(any(OrderCreateDto.class));
    }

    @Test
    void createOrder_ShouldReturnOrderNotItemsException() {
        when(orderService.createOrder(any(OrderCreateDto.class))).thenThrow(new OrderNotItemsExceptions(order.getId()));

        var res = assertThrows(OrderNotItemsExceptions.class, () -> orderController.createOrder(orderCreateDto));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("El pedido con id " + order.getId() + " no tiene items", res.getMessage())
        );

        verify(orderService, times(1)).createOrder(any(OrderCreateDto.class));
    }

    @Test
    void createOrder_ShouldReturnOrderNotStockException() {
        when(orderService.createOrder(any(OrderCreateDto.class))).thenThrow(new OrderNotStockException(book.getId()));

        var res = assertThrows(OrderNotStockException.class, () -> orderController.createOrder(orderCreateDto));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("El producto con id " + book.getId() + " no tiene stock", res.getMessage())
        );

        verify(orderService, times(1)).createOrder(any(OrderCreateDto.class));
    }

    @Test
    void createOrder_ShouldOrderBadPriceException() {
        when(orderService.createOrder(any(OrderCreateDto.class))).thenThrow(new OrderBadPriceException(book.getId()));

        var res = assertThrows(OrderBadPriceException.class, () -> orderController.createOrder(orderCreateDto));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("El precio del producto con id " + book.getId() + " no es correcto", res.getMessage())
        );

        verify(orderService, times(1)).createOrder(any(OrderCreateDto.class));
    }

    @Test
    void createOrder_ShouldReturnBookNotFoundException() {
        when(orderService.createOrder(any(OrderCreateDto.class))).thenThrow(new BookNotFoundException("El libro con id " + book.getId() + " no existe"));

        var res = assertThrows(BookNotFoundException.class, () -> orderController.createOrder(orderCreateDto));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("Libro no encontrado - El libro con id " + book.getId() + " no existe", res.getMessage())
        );

        verify(orderService, times(1)).createOrder(any(OrderCreateDto.class));
    }

    @Test
    void createOrder_ShouldReturnOrderGroupByOrderLinesByIdBook() {
        Order order = Order.builder()
                .id(new ObjectId())
                .userId(userTest.getId())
                .clientId(clientTest.getId())
                .shopId(shop.getId())
                .isDeleted(false)
                .orderLines(List.of(OrderLine.builder()
                        .bookId(1L)
                        .quantity(2)
                        .price(1.0)
                        .build(), orderLine2))
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .total(3.0)
                .totalBooks(2)
                .build();

        when(orderService.createOrder(any(OrderCreateDto.class))).thenReturn(order);

        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(userTest.getId())
                .clientId(clientTest.getId())
                .orderLines(List.of(orderLine, orderLine2, orderLine))
                .shopId(shop.getId())
                .build();

        var res = orderController.createOrder(orderCreateDto);

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(order.getId().toHexString(), res.getBody().getId().toHexString()),
                () -> assertEquals(order.getTotal(), res.getBody().getTotal()),
                () -> assertEquals(order.getTotalBooks(), res.getBody().getTotalBooks()),
                () -> assertEquals(order.getUserId(), res.getBody().getUserId()),
                () -> assertEquals(order.getClientId(), res.getBody().getClientId()),
                () -> assertEquals(order.getShopId(), res.getBody().getShopId()),
                () -> assertEquals(order.getOrderLines().size(), res.getBody().getOrderLines().size()),
                () -> assertEquals(order.getCreatedAt(), res.getBody().getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), res.getBody().getUpdatedAt()),
                () -> assertEquals(order.getIsDeleted(), res.getBody().getIsDeleted()),
                () -> assertEquals(201, res.getStatusCodeValue())
        );

        verify(orderService, times(1)).createOrder(any(OrderCreateDto.class));
    }


    @Test
    void updateOrder_ShouldReturnOrder() {
        when(orderService.updateOrder(any(ObjectId.class), any(OrderCreateDto.class))).thenReturn(order);

        var res = orderController.updateOrder(order.getId(), orderCreateDto);

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(order.getId().toHexString(), res.getBody().getId().toHexString()),
                () -> assertEquals(order.getTotal(), res.getBody().getTotal()),
                () -> assertEquals(order.getTotalBooks(), res.getBody().getTotalBooks()),
                () -> assertEquals(order.getUserId(), res.getBody().getUserId()),
                () -> assertEquals(order.getClientId(), res.getBody().getClientId()),
                () -> assertEquals(order.getShopId(), res.getBody().getShopId()),
                () -> assertEquals(order.getOrderLines().size(), res.getBody().getOrderLines().size()),
                () -> assertEquals(order.getCreatedAt(), res.getBody().getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), res.getBody().getUpdatedAt()),
                () -> assertEquals(order.getIsDeleted(), res.getBody().getIsDeleted()),
                () -> assertEquals(200, res.getStatusCodeValue())
        );

        verify(orderService, times(1)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void updateOrder_ShouldReturnOrderNotFoundException() {
        when(orderService.updateOrder(any(ObjectId.class), any(OrderCreateDto.class))).thenThrow(new OrderNotFoundException(order.getId()));

        var res = assertThrows(OrderNotFoundException.class, () -> orderController.updateOrder(order.getId(), orderCreateDto));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("El pedido con id " + order.getId() + " no existe", res.getMessage())
        );

        verify(orderService, times(1)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void updateOrder_ShouldReturnClientNotFoundException() {
        when(orderService.updateOrder(any(ObjectId.class), any(OrderCreateDto.class))).thenThrow(new ClientNotFound("id", clientTest.getId()));

        var res = assertThrows(ClientNotFound.class, () -> orderController.updateOrder(order.getId(), orderCreateDto));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("Client con id: " + clientTest.getId() + " no existe", res.getMessage())
        );

        verify(orderService, times(1)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void updateOrder_ShouldReturnUserNotFoundException() {
        when(orderService.updateOrder(any(ObjectId.class), any(OrderCreateDto.class))).thenThrow(new UserNotFound(userTest.getId()));

        var res = assertThrows(UserNotFound.class, () -> orderController.updateOrder(order.getId(), orderCreateDto));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("Usuario con id" + userTest.getId() + " no encontrado", res.getMessage())
        );

        verify(orderService, times(1)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void updateOrder_ShouldReturnShopNotFoundException() {
        when(orderService.updateOrder(any(ObjectId.class), any(OrderCreateDto.class))).thenThrow(new ShopNotFoundException("La tienda con id " + shop.getId() + " no existe"));

        var res = assertThrows(ShopNotFoundException.class, () -> orderController.updateOrder(order.getId(), orderCreateDto));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("Tienda no encontrada - " + "La tienda con id " + shop.getId() + " no existe", res.getMessage())
        );

        verify(orderService, times(1)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    void updateOrder_ShouldReturnBookNotFoundException() {
        when(orderService.updateOrder(any(ObjectId.class), any(OrderCreateDto.class))).thenThrow(new BookNotFoundException("El libro con id " + book.getId() + " no existe"));

        var res = assertThrows(BookNotFoundException.class, () -> orderController.updateOrder(order.getId(), orderCreateDto));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("El libro con id " + book.getId() + " no existe", res.getMessage())
        );

        verify(orderService, times(1)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void updateOrder_ShouldReturnOrderNotItemsException() {
        when(orderService.updateOrder(any(ObjectId.class), any(OrderCreateDto.class))).thenThrow(new OrderNotItemsExceptions(order.getId()));

        var res = assertThrows(OrderNotItemsExceptions.class, () -> orderController.updateOrder(order.getId(), orderCreateDto));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("El pedido con id " + order.getId() + " no tiene items", res.getMessage())
        );

        verify(orderService, times(1)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void updateOrder_ShouldReturnOrderNotStockException() {
        when(orderService.updateOrder(any(ObjectId.class), any(OrderCreateDto.class))).thenThrow(new OrderNotStockException(book.getId()));

        var res = assertThrows(OrderNotStockException.class, () -> orderController.updateOrder(order.getId(), orderCreateDto));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("El producto con id " + book.getId() + " no tiene stock", res.getMessage())
        );

        verify(orderService, times(1)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void updateOrder_ShouldReturnOrderBadPriceException() {
        when(orderService.updateOrder(any(ObjectId.class), any(OrderCreateDto.class))).thenThrow(new OrderBadPriceException(book.getId()));

        var res = assertThrows(OrderBadPriceException.class, () -> orderController.updateOrder(order.getId(), orderCreateDto));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("El precio del producto con id " + book.getId() + " no es correcto", res.getMessage())
        );

        verify(orderService, times(1)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void updateOrder_ShouldReturnOrderGroupByOrderLinesByIdBook() {
        Order order = Order.builder()
                .id(new ObjectId())
                .userId(userTest.getId())
                .clientId(clientTest.getId())
                .shopId(shop.getId())
                .isDeleted(false)
                .orderLines(List.of(OrderLine.builder()
                        .bookId(1L)
                        .quantity(2)
                        .price(1.0)
                        .build(), orderLine2))
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .total(3.0)
                .totalBooks(2)
                .build();

        when(orderService.updateOrder(any(ObjectId.class), any(OrderCreateDto.class))).thenReturn(order);

        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(userTest.getId())
                .clientId(clientTest.getId())
                .orderLines(List.of(orderLine, orderLine2, orderLine))
                .shopId(shop.getId())
                .build();

        var res = orderController.updateOrder(order.getId(), orderCreateDto);

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(order.getId().toHexString(), res.getBody().getId().toHexString()),
                () -> assertEquals(order.getTotal(), res.getBody().getTotal()),
                () -> assertEquals(order.getTotalBooks(), res.getBody().getTotalBooks()),
                () -> assertEquals(order.getUserId(), res.getBody().getUserId()),
                () -> assertEquals(order.getClientId(), res.getBody().getClientId()),
                () -> assertEquals(order.getShopId(), res.getBody().getShopId()),
                () -> assertEquals(order.getOrderLines().size(), res.getBody().getOrderLines().size()),
                () -> assertEquals(order.getCreatedAt(), res.getBody().getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), res.getBody().getUpdatedAt()),
                () -> assertEquals(order.getIsDeleted(), res.getBody().getIsDeleted()),
                () -> assertEquals(200, res.getStatusCodeValue())
        );

        verify(orderService, times(1)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void deleteOrder_ShouldReturnOrder() {
        doNothing().when(orderService).deleteOrder(any(ObjectId.class));

        var res = orderController.deleteOrder(order.getId());

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(204, res.getStatusCodeValue())
        );

        verify(orderService, times(1)).deleteOrder(any(ObjectId.class));
    }

    @Test
    void deleteOrder_ShouldReturnOrderNotFoundException() {
        doThrow(new OrderNotFoundException(order.getId())).when(orderService).deleteOrder(any(ObjectId.class));

        var res = assertThrows(OrderNotFoundException.class, () -> orderController.deleteOrder(order.getId()));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("El pedido con id " + order.getId() + " no existe", res.getMessage())
        );

        verify(orderService, times(1)).deleteOrder(any(ObjectId.class));
    }

    @Test
    void deleteOrder_ShouldReturnBookNotFoundException() {
        doThrow(new BookNotFoundException("El libro con id " + book.getId() + " no existe")).when(orderService).deleteOrder(any(ObjectId.class));

        var res = assertThrows(BookNotFoundException.class, () -> orderController.deleteOrder(order.getId()));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("Libro no encontrado - El libro con id " + book.getId() + " no existe", res.getMessage())
        );

        verify(orderService, times(1)).deleteOrder(any(ObjectId.class));
    }

    @Test
    void deleteLogicOrder_ShouldReturnOrder() {
        Order order = Order.builder()
                .id(new ObjectId())
                .userId(userTest.getId())
                .clientId(clientTest.getId())
                .shopId(shop.getId())
                .isDeleted(true)
                .orderLines(List.of(orderLine, orderLine2))
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .total(2.0)
                .totalBooks(2)
                .build();

        when(orderService.deleteLogicOrder(any(ObjectId.class))).thenReturn(order);

        var res = orderController.deleteLogicOrder(order.getId());

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(order.getId().toHexString(), res.getBody().getId().toHexString()),
                () -> assertEquals(order.getTotal(), res.getBody().getTotal()),
                () -> assertEquals(order.getTotalBooks(), res.getBody().getTotalBooks()),
                () -> assertEquals(order.getUserId(), res.getBody().getUserId()),
                () -> assertEquals(order.getClientId(), res.getBody().getClientId()),
                () -> assertEquals(order.getShopId(), res.getBody().getShopId()),
                () -> assertEquals(order.getOrderLines().size(), res.getBody().getOrderLines().size()),
                () -> assertEquals(order.getCreatedAt(), res.getBody().getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), res.getBody().getUpdatedAt()),
                () -> assertEquals(order.getIsDeleted(), res.getBody().getIsDeleted()),
                () -> assertEquals(200, res.getStatusCodeValue())
        );

        verify(orderService, times(1)).deleteLogicOrder(any(ObjectId.class));
    }

    @Test
    void deleteLogicOrder_ShouldReturnOrderNotFoundException() {
        when(orderService.deleteLogicOrder(any(ObjectId.class))).thenThrow(new OrderNotFoundException(order.getId()));

        var res = assertThrows(OrderNotFoundException.class, () -> orderController.deleteLogicOrder(order.getId()));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("El pedido con id " + order.getId() + " no existe", res.getMessage())
        );

        verify(orderService, times(1)).deleteLogicOrder(any(ObjectId.class));
    }

    @Test
    void getOrdersByClientId_ShouldReturnOrder() {
        when(orderService.getOrdersByClientId(any(UUID.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(order)));

        var res = orderController.getOrdersByClientId(clientTest.getId(), new PageableRequest(0, 10, "id", "asc"), requestMock);

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(1, res.getBody().totalPages()),
                () -> assertEquals(1, res.getBody().totalElements()),
                () -> assertEquals(order.getId().toHexString(), res.getBody().content().get(0).getId().toHexString()),
                () -> assertEquals(order.getTotal(), res.getBody().content().get(0).getTotal()),
                () -> assertEquals(order.getTotalBooks(), res.getBody().content().get(0).getTotalBooks()),
                () -> assertEquals(order.getUserId(), res.getBody().content().get(0).getUserId()),
                () -> assertEquals(order.getClientId(), res.getBody().content().get(0).getClientId()),
                () -> assertEquals(order.getShopId(), res.getBody().content().get(0).getShopId()),
                () -> assertEquals(order.getOrderLines().size(), res.getBody().content().get(0).getOrderLines().size()),
                () -> assertEquals(order.getCreatedAt(), res.getBody().content().get(0).getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), res.getBody().content().get(0).getUpdatedAt()),
                () -> assertEquals(order.getIsDeleted(), res.getBody().content().get(0).getIsDeleted()),
                () -> assertEquals(200, res.getStatusCodeValue())
        );

        verify(orderService, times(1)).getOrdersByClientId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void getOrdersByClientId_ShouldReturnEmptyPage() {
        when(orderService.getOrdersByClientId(any(UUID.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        var res = orderController.getOrdersByClientId(clientTest.getId(), new PageableRequest(0, 10, "id", "asc"), requestMock);

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(1, res.getBody().totalPages()),
                () -> assertEquals(0, res.getBody().totalElements()),
                () -> assertEquals(0, res.getBody().content().size()),
                () -> assertEquals(200, res.getStatusCodeValue())
        );

        verify(orderService, times(1)).getOrdersByClientId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void getOrdersByClientId_ShouldReturnClientNotFoundException() {
        when(orderService.getOrdersByClientId(any(UUID.class), any(Pageable.class))).thenThrow(new ClientNotFound("id", clientTest.getId()));

        var res = assertThrows(ClientNotFound.class, () -> orderController.getOrdersByClientId(clientTest.getId(), new PageableRequest(0, 10, "id", "asc"), requestMock));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("Client con id: " + clientTest.getId() + " no existe", res.getMessage())
        );

        verify(orderService, times(1)).getOrdersByClientId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void getOrdersByUserId_ShouldReturnOrder() {
        when(orderService.getOrdersByUserId(any(UUID.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(order)));

        var res = orderController.getOrdersByUserId(userTest.getId(), new PageableRequest(0, 10, "id", "asc"), requestMock);

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(1, res.getBody().totalPages()),
                () -> assertEquals(1, res.getBody().totalElements()),
                () -> assertEquals(order.getId().toHexString(), res.getBody().content().get(0).getId().toHexString()),
                () -> assertEquals(order.getTotal(), res.getBody().content().get(0).getTotal()),
                () -> assertEquals(order.getTotalBooks(), res.getBody().content().get(0).getTotalBooks()),
                () -> assertEquals(order.getUserId(), res.getBody().content().get(0).getUserId()),
                () -> assertEquals(order.getClientId(), res.getBody().content().get(0).getClientId()),
                () -> assertEquals(order.getShopId(), res.getBody().content().get(0).getShopId()),
                () -> assertEquals(order.getOrderLines().size(), res.getBody().content().get(0).getOrderLines().size()),
                () -> assertEquals(order.getCreatedAt(), res.getBody().content().get(0).getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), res.getBody().content().get(0).getUpdatedAt()),
                () -> assertEquals(order.getIsDeleted(), res.getBody().content().get(0).getIsDeleted()),
                () -> assertEquals(200, res.getStatusCodeValue())
        );

        verify(orderService, times(1)).getOrdersByUserId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void getOrdersByUserId_ShouldReturnEmptyPage() {
        when(orderService.getOrdersByUserId(any(UUID.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        var res = orderController.getOrdersByUserId(userTest.getId(), new PageableRequest(0, 10, "id", "asc"), requestMock);

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(1, res.getBody().totalPages()),
                () -> assertEquals(0, res.getBody().totalElements()),
                () -> assertEquals(0, res.getBody().content().size()),
                () -> assertEquals(200, res.getStatusCodeValue())
        );

        verify(orderService, times(1)).getOrdersByUserId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void getOrdersByUserId_ShouldReturnUserNotFoundException() {
        when(orderService.getOrdersByUserId(any(UUID.class), any(Pageable.class))).thenThrow(new UserNotFound(userTest.getId()));

        var res = assertThrows(UserNotFound.class, () -> orderController.getOrdersByUserId(userTest.getId(), new PageableRequest(0, 10, "id", "asc"), requestMock));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("Usuario con id" + userTest.getId() + " no encontrado", res.getMessage())
        );

        verify(orderService, times(1)).getOrdersByUserId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void getOrdersByShopId_ShouldReturnOrder() {
        when(orderService.getOrdersByShopId(any(UUID.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(order)));

        var res = orderController.getOrdersByShopId(shop.getId(), new PageableRequest(0, 10, "id", "asc"), requestMock);

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(1, res.getBody().totalPages()),
                () -> assertEquals(1, res.getBody().totalElements()),
                () -> assertEquals(order.getId().toHexString(), res.getBody().content().get(0).getId().toHexString()),
                () -> assertEquals(order.getTotal(), res.getBody().content().get(0).getTotal()),
                () -> assertEquals(order.getTotalBooks(), res.getBody().content().get(0).getTotalBooks()),
                () -> assertEquals(order.getUserId(), res.getBody().content().get(0).getUserId()),
                () -> assertEquals(order.getClientId(), res.getBody().content().get(0).getClientId()),
                () -> assertEquals(order.getShopId(), res.getBody().content().get(0).getShopId()),
                () -> assertEquals(order.getOrderLines().size(), res.getBody().content().get(0).getOrderLines().size()),
                () -> assertEquals(order.getCreatedAt(), res.getBody().content().get(0).getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), res.getBody().content().get(0).getUpdatedAt()),
                () -> assertEquals(order.getIsDeleted(), res.getBody().content().get(0).getIsDeleted()),
                () -> assertEquals(200, res.getStatusCodeValue())
        );

        verify(orderService, times(1)).getOrdersByShopId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void getOrdersByShopId_ShouldReturnEmptyPage() {
        when(orderService.getOrdersByShopId(any(UUID.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        var res = orderController.getOrdersByShopId(shop.getId(), new PageableRequest(0, 10, "id", "asc"), requestMock);

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(1, res.getBody().totalPages()),
                () -> assertEquals(0, res.getBody().totalElements()),
                () -> assertEquals(0, res.getBody().content().size()),
                () -> assertEquals(200, res.getStatusCodeValue())
        );

        verify(orderService, times(1)).getOrdersByShopId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void getOrdersByShopId_ShouldReturnShopNotFoundException() {
        when(orderService.getOrdersByShopId(any(UUID.class), any(Pageable.class))).thenThrow(new ShopNotFoundException("La tienda con id " + shop.getId() + " no existe"));

        var res = assertThrows(ShopNotFoundException.class, () -> orderController.getOrdersByShopId(shop.getId(), new PageableRequest(0, 10, "id", "asc"), requestMock));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("Tienda no encontrada - " + "La tienda con id " + shop.getId() + " no existe", res.getMessage())
        );

        verify(orderService, times(1)).getOrdersByShopId(any(UUID.class), any(Pageable.class));
    }


}
