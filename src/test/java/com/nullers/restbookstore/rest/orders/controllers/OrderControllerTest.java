package com.nullers.restbookstore.rest.orders.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nullers.restbookstore.pagination.models.ErrorResponse;
import com.nullers.restbookstore.pagination.models.PageResponse;
import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.book.model.Book;
import com.nullers.restbookstore.rest.category.model.Categoria;
import com.nullers.restbookstore.rest.client.exceptions.ClientNotFound;
import com.nullers.restbookstore.rest.client.model.Client;
import com.nullers.restbookstore.rest.common.Address;
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
import com.nullers.restbookstore.rest.user.models.Role;
import com.nullers.restbookstore.rest.user.models.User;
import org.aspectj.weaver.ast.Or;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest(properties = "spring.config.name=application-test")
@WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
public class OrderControllerTest {

    private final String endpoint = "/api/orders";

    private ObjectMapper mapper;

    @MockBean
    OrderServiceImpl orderService;

    @Autowired
    MockMvc mockMvc;

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

    Shop shop = Shop.builder()
            .id(UUID.fromString("b5f29063-77d8-4d5d-98ea-def0cc9ebc5f"))
            .name("name")
            .books(List.of(book))
            .clients(List.of(clientTest))
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


    @Autowired
    public OrderControllerTest(ObjectMapper mapper, OrderServiceImpl orderService) {
        this.mapper = mapper;
        this.orderService = orderService;
    }


    @Test
    @WithAnonymousUser
    void getAllOrders_ShouldReturnForbidden() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(endpoint)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.FORBIDDEN .value(), response.getStatus())
        );

        verify(orderService, times(0)).getAllOrders(any(Pageable.class));
    }

    @Test
    @WithAnonymousUser
    void getOrderById_ShouldReturnForbidden() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/{id}", order.getId())
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.FORBIDDEN .value(), response.getStatus())
        );

        verify(orderService, times(0)).getOrderById(any(ObjectId.class));
    }

    @Test
    @WithAnonymousUser
    void createOrder_ShouldReturnForbidden() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.FORBIDDEN .value(), response.getStatus())
        );

        verify(orderService, times(0)).createOrder(any(OrderCreateDto.class));
    }

    @Test
    @WithAnonymousUser
    void updateOrder_ShouldReturnForbidden() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/{id}", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.FORBIDDEN .value(), response.getStatus())
        );

        verify(orderService, times(0)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    @WithAnonymousUser
    void deleteOrder_ShouldReturnForbidden() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(delete(endpoint + "/{id}", order.getId())
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.FORBIDDEN .value(), response.getStatus())
        );

        verify(orderService, times(0)).deleteOrder(any(ObjectId.class));
    }

    @Test
    @WithAnonymousUser
    void deleteLogicOrder_ShouldReturnForbidden() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/delete/{id}", order.getId())
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.FORBIDDEN .value(), response.getStatus())
        );

        verify(orderService, times(0)).deleteLogicOrder(any(ObjectId.class));
    }

    @Test
    @WithAnonymousUser
    void getOrdersByClientId_ShouldReturnForbidden() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/client/{id}", clientTest.getId())
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.FORBIDDEN .value(), response.getStatus())
        );

        verify(orderService, times(0)).getOrdersByClientId(any(UUID.class), any(Pageable.class));
    }

    @Test
    @WithAnonymousUser
    void getOrdersByUserId_ShouldReturnForbidden() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/user/{id}", userTest.getId())
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.FORBIDDEN .value(), response.getStatus())
        );

        verify(orderService, times(0)).getOrdersByUserId(any(UUID.class), any(Pageable.class));
    }

    @Test
    @WithAnonymousUser
    void getOrdersByShopId_ShouldReturnForbidden() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/shop/{id}", shop.getId())
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.FORBIDDEN .value(), response.getStatus())
        );

        verify(orderService, times(0)).getOrdersByShopId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void getAllOrders_ShouldReturnOrders() throws Exception {
        List<Order> orders = List.of(order);
        when(orderService.getAllOrders(any(Pageable.class))).thenReturn(new PageImpl<>(orders));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        PageResponse<Order> pageResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructParametricType(PageResponse.class, Order.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(1, pageResponse.totalElements()),
                () -> assertEquals(1, pageResponse.totalPages()),
                () -> assertEquals(1, pageResponse.content().size()),
                () -> assertEquals(order.getId(), pageResponse.content().get(0).getId()),
                () -> assertEquals(order.getUserId(), pageResponse.content().get(0).getUserId()),
                () -> assertEquals(order.getClientId(), pageResponse.content().get(0).getClientId()),
                () -> assertEquals(order.getShopId(), pageResponse.content().get(0).getShopId()),
                () -> assertEquals(order.getOrderLines(), pageResponse.content().get(0).getOrderLines()),
                () -> assertEquals(order.getTotal(), pageResponse.content().get(0).getTotal()),
                () -> assertEquals(order.getTotalBooks(), pageResponse.content().get(0).getTotalBooks()),
                () -> assertEquals(order.getCreatedAt(), pageResponse.content().get(0).getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), pageResponse.content().get(0).getUpdatedAt()),
                () -> assertEquals(order.isDeleted(), pageResponse.content().get(0).isDeleted())
        );

        verify(orderService, times(1)).getAllOrders(any(Pageable.class));

    }

    @Test
    void getAllOrder_ShouldReturnEmptyPage() throws Exception{
        when(orderService.getAllOrders(any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint)
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        PageResponse<Order> pageResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructParametricType(PageResponse.class, Order.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(0, pageResponse.totalElements()),
                () -> assertEquals(1, pageResponse.totalPages()),
                () -> assertEquals(0, pageResponse.content().size())
        );

        verify(orderService, times(1)).getAllOrders(any(Pageable.class));
    }


    @Test
    void getAllOrders_ReturnErrorWithInvalidPage() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "?page=-1")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("La página no puede ser inferior a 0", errors.get("page"))
        );

        verify(orderService, times(0)).getAllOrders(any(Pageable.class));
    }

    @Test
    void getAllOrder_ShouldReturnErrrorWithInvalidSizePage() throws Exception{
        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "?size=0")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El tamaño de la pagina no puede ser inferior a 1", errors.get("size"))
        );

        verify(orderService, times(0)).getAllOrders(any(Pageable.class));
    }

    @Test
    void getAllOrder_ShouldReturnErrrorWithInvalidSizePageAndPage() throws Exception{
        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "?size=0&page=-1")
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("La página no puede ser inferior a 0", errors.get("page")),
                () -> assertEquals("El tamaño de la pagina no puede ser inferior a 1", errors.get("size"))
        );

        verify(orderService, times(0)).getAllOrders(any(Pageable.class));
    }

    @Test
    void getOrderById_ShouldReturnOrder() throws Exception{
        when(orderService.getOrderById(any(ObjectId.class))).thenReturn(order);

        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/{id}", order.getId())
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        Order orderResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), Order.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(order.getId(), orderResponse.getId()),
                () -> assertEquals(order.getUserId(), orderResponse.getUserId()),
                () -> assertEquals(order.getClientId(), orderResponse.getClientId()),
                () -> assertEquals(order.getShopId(), orderResponse.getShopId()),
                () -> assertEquals(order.getOrderLines(), orderResponse.getOrderLines()),
                () -> assertEquals(order.getTotal(), orderResponse.getTotal()),
                () -> assertEquals(order.getTotalBooks(), orderResponse.getTotalBooks()),
                () -> assertEquals(order.getCreatedAt(), orderResponse.getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), orderResponse.getUpdatedAt()),
                () -> assertEquals(order.isDeleted(), orderResponse.isDeleted())
        );

        verify(orderService, times(1)).getOrderById(any(ObjectId.class));
    }

    @Test
    void getOrderbyId_ShouldReturnNotFound() throws Exception{
        when(orderService.getOrderById(any(ObjectId.class))).thenThrow(new OrderNotFoundException(order.getId()));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/{id}", order.getId())
                .accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("El pedido con id " + order.get_id() + " no existe", errorResponse.msg())
        );

        verify(orderService, times(1)).getOrderById(any(ObjectId.class));
    }

    @Test
    void createOrder_ShouldReturnOrder() throws Exception{
        when(orderService.createOrder(any(OrderCreateDto.class))).thenReturn(order);

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        Order orderResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), Order.class);

        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), response.getStatus()),
                () -> assertEquals(order.getId(), orderResponse.getId()),
                () -> assertEquals(order.getUserId(), orderResponse.getUserId()),
                () -> assertEquals(order.getClientId(), orderResponse.getClientId()),
                () -> assertEquals(order.getShopId(), orderResponse.getShopId()),
                () -> assertEquals(order.getOrderLines(), orderResponse.getOrderLines()),
                () -> assertEquals(order.getTotal(), orderResponse.getTotal()),
                () -> assertEquals(order.getTotalBooks(), orderResponse.getTotalBooks()),
                () -> assertEquals(order.getCreatedAt(), orderResponse.getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), orderResponse.getUpdatedAt()),
                () -> assertEquals(order.isDeleted(), orderResponse.isDeleted())
        );

        verify(orderService, times(1)).createOrder(any(OrderCreateDto.class));
    }

    @Test
    void createOrder_ShouldReturnOrderNotFoundException() throws Exception{
        when(orderService.createOrder(any(OrderCreateDto.class))).thenThrow(new OrderNotFoundException(order.getId()));

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("El pedido con id " + order.get_id() + " no existe", errorResponse.msg())
        );

        verify(orderService, times(1)).createOrder(any(OrderCreateDto.class));
    }

    @Test
    void createOrder_ShouldReturnClientNotFoundException() throws Exception{
        when(orderService.createOrder(any(OrderCreateDto.class))).thenThrow(new ClientNotFound("id", clientTest.getId()));

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Client con id: " + clientTest.getId() + " no existe", errorResponse.msg())
        );

        verify(orderService, times(1)).createOrder(any(OrderCreateDto.class));
    }

    @Test
    void createOrder_ShouldReturnUserNotFoundException() throws Exception{
        when(orderService.createOrder(any(OrderCreateDto.class))).thenThrow(new UserNotFound(userTest.getId() ));

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Usuario con id" + userTest.getId().toString() + " no encontrado", errorResponse.msg())
        );

        verify(orderService, times(1)).createOrder(any(OrderCreateDto.class));
    }

    @Test
    void createOrder_ShouldReturnShopNotFoundException() throws Exception{
        when(orderService.createOrder(any(OrderCreateDto.class))).thenThrow(new ShopNotFoundException("La tienda con id " + shop.getId().toString() + " no existe"));

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Tienda no encontrada - " + "La tienda con id " + shop.getId().toString() + " no existe", errorResponse.msg())
        );

        verify(orderService, times(1)).createOrder(any(OrderCreateDto.class));
    }

    @Test
    void createOrder_ShouldReturnOrderNotItemsException() throws Exception{
        when(orderService.createOrder(any(OrderCreateDto.class))).thenThrow(new OrderNotItemsExceptions(order.getId().toHexString()));

        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(userTest.getId())
                .clientId(clientTest.getId())
                .orderLines(List.of())
                .shopId(shop.getId())
                .build();

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El pedido con id " + order.get_id() + " no tiene items", errorResponse.msg())
        );

        verify(orderService, times(1)).createOrder(any(OrderCreateDto.class));
    }

    @Test
    void createOrderShouldReturnBookNotFound() throws Exception{
        when(orderService.createOrder(any(OrderCreateDto.class))).thenThrow(new BookNotFoundException("El libro con id " + book.getId() + " no existe"));


        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Libro no encontrado - "+ "El libro con id " + book.getId() + " no existe", errorResponse.msg())
        );

        verify(orderService, times(1)).createOrder(any(OrderCreateDto.class));
    }

    @Test
    void createOrder_ShouldReturnNotStockException() throws Exception{
        when(orderService.createOrder(any(OrderCreateDto.class))).thenThrow(new OrderNotStockException(book.getId()));

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El producto con id " + book.getId() + " no tiene stock", errorResponse.msg())
        );
    }

    @Test
    void createOrder_ShouldReturnOrderBadPriceException() throws Exception{
        when(orderService.createOrder(any(OrderCreateDto.class))).thenThrow(new OrderBadPriceException(book.getId()));

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El precio del producto con id " + book.getId() + " no es correcto", errorResponse.msg())
        );
    }

    @Test
    void createOrder_ShouldReturnWithOrderLinesGroupByIdBook() throws Exception{
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

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        Order orderResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), Order.class);

        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), response.getStatus()),
                () -> assertEquals(order.getId(), orderResponse.getId()),
                () -> assertEquals(order.getUserId(), orderResponse.getUserId()),
                () -> assertEquals(order.getClientId(), orderResponse.getClientId()),
                () -> assertEquals(order.getShopId(), orderResponse.getShopId()),
                () -> assertEquals(order.getTotal(), orderResponse.getTotal()),
                () -> assertEquals(order.getTotalBooks(), orderResponse.getTotalBooks()),
                () -> assertEquals(order.getCreatedAt(), orderResponse.getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), orderResponse.getUpdatedAt()),
                () -> assertEquals(order.isDeleted(), orderResponse.isDeleted()),
                () -> assertEquals(2, orderResponse.getOrderLines().size()),
                () -> assertEquals(2, orderResponse.getOrderLines().get(0).getQuantity()),
                () -> assertEquals(1, orderResponse.getOrderLines().get(1).getQuantity())
        );

        verify(orderService, times(1)).createOrder(any(OrderCreateDto.class));
    }

    @Test
    void createOrder_ShouldReturnErrorReponseUserIdNotNull() throws Exception{
        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(null)
                .clientId(clientTest.getId())
                .orderLines(List.of(orderLine, orderLine2, orderLine))
                .shopId(shop.getId())
                .build();

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El id del usuario no puede ser nulo", errors.get("userId"))
        );

        verify(orderService, times(0)).createOrder(any(OrderCreateDto.class));
    }

    @Test
    void createOrder_ShouldReturnErrorReponseClientIdNotNull() throws Exception{
        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(userTest.getId())
                .clientId(null)
                .orderLines(List.of(orderLine, orderLine2, orderLine))
                .shopId(shop.getId())
                .build();

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El cliente no puede ser nulo", errors.get("clientId"))
        );

        verify(orderService, times(0)).createOrder(any(OrderCreateDto.class));
    }

    @Test
    void createOrder_ShouldReturnErrorReponseShopIdNotNull() throws Exception{
        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(userTest.getId())
                .clientId(clientTest.getId())
                .orderLines(List.of(orderLine, orderLine2, orderLine))
                .shopId(null)
                .build();

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("La tienda no puede ser nula", errors.get("shopId"))
        );

        verify(orderService, times(0)).createOrder(any(OrderCreateDto.class));
    }

    @Test
    void createOrder_ShouldReturnErrorReponseOrderLinesNotNull() throws Exception{
        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(userTest.getId())
                .clientId(clientTest.getId())
                .orderLines(null)
                .shopId(shop.getId())
                .build();

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El pedido debe tener al menos una línea de pedido", errors.get("orderLines"))
        );

        verify(orderService, times(0)).createOrder(any(OrderCreateDto.class));
    }

    @Test
    void createOrder_ShouldReturnWithOrderLinesIdBookNotNull() throws Exception{
        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(userTest.getId())
                .clientId(clientTest.getId())
                .orderLines(List.of(OrderLine.builder()
                        .bookId(null)
                        .quantity(2)
                        .price(1.0)
                        .build(), orderLine2))
                .shopId(shop.getId())
                .build();

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El id del libro no puede ser nulo", errors.get("orderLines[0].bookId"))
        );

        verify(orderService, times(0)).createOrder(any(OrderCreateDto.class));
    }

    @Test
    void createOrder_ShouldReturnWithOrderLinesQuantityMin1() throws Exception{
        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(userTest.getId())
                .clientId(clientTest.getId())
                .orderLines(List.of(OrderLine.builder()
                        .bookId(1L)
                        .quantity(0)
                        .price(1.0)
                        .build(), orderLine2))
                .shopId(shop.getId())
                .build();

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("La cantidad del libro no puede ser negativa", errors.get("orderLines[0].quantity"))
        );

        verify(orderService, times(0)).createOrder(any(OrderCreateDto.class));
    }

    @Test
    void createOrder_ShouldReturnWithOrderLinesPriceMin0() throws Exception{
        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(userTest.getId())
                .clientId(clientTest.getId())
                .orderLines(List.of(OrderLine.builder()
                        .bookId(1L)
                        .quantity(1)
                        .price(-1.0)
                        .build(), orderLine2))
                .shopId(shop.getId())
                .build();

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El precio del libro no puede ser negativo", errors.get("orderLines[0].price"))
        );

        verify(orderService, times(0)).createOrder(any(OrderCreateDto.class));
    }



    @Test
    void updateOrder_ShouldReturnOrder() throws Exception{
        when(orderService.updateOrder(any(ObjectId.class), any(OrderCreateDto.class))).thenReturn(order);

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/{id}", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        Order orderResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), Order.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(order.getId(), orderResponse.getId()),
                () -> assertEquals(order.getUserId(), orderResponse.getUserId()),
                () -> assertEquals(order.getClientId(), orderResponse.getClientId()),
                () -> assertEquals(order.getShopId(), orderResponse.getShopId()),
                () -> assertEquals(order.getOrderLines(), orderResponse.getOrderLines()),
                () -> assertEquals(order.getTotal(), orderResponse.getTotal()),
                () -> assertEquals(order.getTotalBooks(), orderResponse.getTotalBooks()),
                () -> assertEquals(order.getCreatedAt(), orderResponse.getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), orderResponse.getUpdatedAt()),
                () -> assertEquals(order.isDeleted(), orderResponse.isDeleted())
        );

        verify(orderService, times(1)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void updateOrder_ShouldReturnOrderNotFoundException() throws Exception{
        when(orderService.updateOrder(any(ObjectId.class), any(OrderCreateDto.class))).thenThrow(new OrderNotFoundException(order.getId()));

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/{id}", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("El pedido con id " + order.get_id() + " no existe", errorResponse.msg())
        );

        verify(orderService, times(1)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void updateOrder_ShouldReturnClientNotFoundException() throws Exception{
        when(orderService.updateOrder(any(ObjectId.class), any(OrderCreateDto.class))).thenThrow(new ClientNotFound("id", clientTest.getId()));

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/{id}", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Client con id: " + clientTest.getId() + " no existe", errorResponse.msg())
        );

        verify(orderService, times(1)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void updateOrder_ShouldReturnUserNotFoundException() throws Exception{
        when(orderService.updateOrder(any(ObjectId.class), any(OrderCreateDto.class))).thenThrow(new UserNotFound(userTest.getId() ));

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/{id}", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Usuario con id" + userTest.getId().toString() + " no encontrado", errorResponse.msg())
        );

        verify(orderService, times(1)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void updateOrder_ShouldReturnShopNotFoundException() throws Exception{
        when(orderService.updateOrder(any(ObjectId.class), any(OrderCreateDto.class))).thenThrow(new ShopNotFoundException("La tienda con id " + shop.getId().toString() + " no existe"));

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/{id}", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Tienda no encontrada - " + "La tienda con id " + shop.getId().toString() + " no existe", errorResponse.msg())
        );

        verify(orderService, times(1)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void updateOrder_ShouldReturnOrderNotItemsException() throws Exception{
        when(orderService.updateOrder(any(ObjectId.class), any(OrderCreateDto.class))).thenThrow(new OrderNotItemsExceptions(order.getId().toHexString()));

        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(userTest.getId())
                .clientId(clientTest.getId())
                .orderLines(List.of())
                .shopId(shop.getId())
                .build();

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/{id}", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El pedido con id " + order.get_id() + " no tiene items", errorResponse.msg())
        );

        verify(orderService, times(1)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void updateOrderShouldReturnBookNotFound() throws Exception {
        when(orderService.updateOrder(any(ObjectId.class), any(OrderCreateDto.class))).thenThrow(new BookNotFoundException("El libro con id " + book.getId() + " no existe"));

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/{id}", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Libro no encontrado - " + "El libro con id " + book.getId() + " no existe", errorResponse.msg())
        );

        verify(orderService, times(1)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void updateOrder_ShouldReturnNotStockException() throws Exception{
        when(orderService.updateOrder(any(ObjectId.class), any(OrderCreateDto.class))).thenThrow(new OrderNotStockException(book.getId()));

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/{id}", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El producto con id " + book.getId() + " no tiene stock", errorResponse.msg())
        );

        verify(orderService, times(1)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void updateOrder_ShouldReturnOrderBadPriceException() throws Exception{
        when(orderService.updateOrder(any(ObjectId.class), any(OrderCreateDto.class))).thenThrow(new OrderBadPriceException(book.getId()));

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/{id}", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El precio del producto con id " + book.getId() + " no es correcto", errorResponse.msg())
        );

        verify(orderService, times(1)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void updateOrder_ShouldReturnWithOrderLinesGroupByIdBook() throws Exception{
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

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/{id}", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        Order orderResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), Order.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(order.getId(), orderResponse.getId()),
                () -> assertEquals(order.getUserId(), orderResponse.getUserId()),
                () -> assertEquals(order.getClientId(), orderResponse.getClientId()),
                () -> assertEquals(order.getShopId(), orderResponse.getShopId()),
                () -> assertEquals(order.getTotal(), orderResponse.getTotal()),
                () -> assertEquals(order.getTotalBooks(), orderResponse.getTotalBooks()),
                () -> assertEquals(order.getCreatedAt(), orderResponse.getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), orderResponse.getUpdatedAt()),
                () -> assertEquals(order.isDeleted(), orderResponse.isDeleted()),
                () -> assertEquals(2, orderResponse.getOrderLines().size()),
                () -> assertEquals(2, orderResponse.getOrderLines().get(0).getQuantity()),
                () -> assertEquals(1, orderResponse.getOrderLines().get(1).getQuantity())
        );

        verify(orderService, times(1)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }


    @Test
    void updateOrder_ShouldReturnErrorReponseUserIdNotNull() throws Exception{
        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(null)
                .clientId(clientTest.getId())
                .orderLines(List.of(orderLine, orderLine2, orderLine))
                .shopId(shop.getId())
                .build();

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/{id}", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El id del usuario no puede ser nulo", errors.get("userId"))
        );

        verify(orderService, times(0)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void updateOrder_ShouldReturnErrorReponseClientIdNotNull() throws Exception{
        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(userTest.getId())
                .clientId(null)
                .orderLines(List.of(orderLine, orderLine2, orderLine))
                .shopId(shop.getId())
                .build();

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/{id}", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El cliente no puede ser nulo", errors.get("clientId"))
        );

        verify(orderService, times(0)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void updateOrder_ShouldReturnErrorReponseShopIdNotNull() throws Exception{
        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(userTest.getId())
                .clientId(clientTest.getId())
                .orderLines(List.of(orderLine, orderLine2, orderLine))
                .shopId(null)
                .build();

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/{id}", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("La tienda no puede ser nula", errors.get("shopId"))
        );

        verify(orderService, times(0)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void updateOrder_ShouldReturnErrorReponseOrderLinesNotNull() throws Exception{
        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(userTest.getId())
                .clientId(clientTest.getId())
                .orderLines(null)
                .shopId(shop.getId())
                .build();

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/{id}", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class) );
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El pedido debe tener al menos una línea de pedido", errors.get("orderLines"))
        );

        verify(orderService, times(0)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void updateOrder_ShouldReturnWithOrderLinesIdBookNotNull() throws Exception{
        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(userTest.getId())
                .clientId(clientTest.getId())
                .orderLines(List.of(OrderLine.builder()
                        .bookId(null)
                        .quantity(2)
                        .price(1.0)
                        .build(), orderLine2))
                .shopId(shop.getId())
                .build();

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/{id}", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El id del libro no puede ser nulo", errors.get("orderLines[0].bookId"))
        );

        verify(orderService, times(0)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void updateOrder_ShouldReturnWithOrderLinesQuantityMin1() throws Exception{
        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(userTest.getId())
                .clientId(clientTest.getId())
                .orderLines(List.of(OrderLine.builder()
                        .bookId(1L)
                        .quantity(0)
                        .price(1.0)
                        .build(), orderLine2))
                .shopId(shop.getId())
                .build();

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/{id}", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("La cantidad del libro no puede ser negativa", errors.get("orderLines[0].quantity"))
        );

        verify(orderService, times(0)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void updateOrder_ShouldReturnWithOrderLinesPriceMin0() throws Exception{
        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(userTest.getId())
                .clientId(clientTest.getId())
                .orderLines(List.of(OrderLine.builder()
                        .bookId(1L)
                        .quantity(1)
                        .price(-1.0)
                        .build(), orderLine2))
                .shopId(shop.getId())
                .build();

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/{id}", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(orderCreateDto))).andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals("El precio del libro no puede ser negativo", errors.get("orderLines[0].price"))
        );

        verify(orderService, times(0)).updateOrder(any(ObjectId.class), any(OrderCreateDto.class));
    }

    @Test
    void deleteOrder_ShouldReturnNoContentOk() throws Exception{
        doNothing().when(orderService).deleteOrder(any(ObjectId.class));

        MockHttpServletResponse response = mockMvc.perform(delete(endpoint + "/{id}", order.getId())).andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus())
        );

        verify(orderService, times(1)).deleteOrder(any(ObjectId.class));
    }

    @Test
    void deleteOrder_ShouldReturnOrderNotFoundException() throws Exception{
        doThrow(new OrderNotFoundException(order.getId())).when(orderService).deleteOrder(any(ObjectId.class));

        MockHttpServletResponse response = mockMvc.perform(delete(endpoint + "/{id}", order.getId())).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("El pedido con id " + order.getId() + " no existe", errorResponse.msg())
        );

        verify(orderService, times(1)).deleteOrder(any(ObjectId.class));
    }

    @Test
    void deleteOrder_ShouldReturnBookNotFoundException() throws Exception{
        doThrow(new BookNotFoundException("El libro con id " + book.getId() + " no existe")).when(orderService).deleteOrder(any(ObjectId.class));

        MockHttpServletResponse response = mockMvc.perform(delete(endpoint + "/{id}", order.getId())).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Libro no encontrado - " + "El libro con id " + book.getId() + " no existe", errorResponse.msg())
        );

        verify(orderService, times(1)).deleteOrder(any(ObjectId.class));
    }

    @Test
    void getOrderByUserId_ShouldReturnOrder() throws Exception{
        when(orderService.getOrdersByUserId(any(UUID.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(order)));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/user/{id}", userTest.getId())).andReturn().getResponse();


        PageResponse<Order> pageResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructParametricType(PageResponse.class, Order.class));


        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(1, pageResponse.totalElements()),
                () -> assertEquals(1, pageResponse.totalPages()),
                () -> assertEquals(1, pageResponse.content().size()),
                () -> assertEquals(order.getId(), pageResponse.content().get(0).getId()),
                () -> assertEquals(order.getUserId(), pageResponse.content().get(0).getUserId()),
                () -> assertEquals(order.getClientId(), pageResponse.content().get(0).getClientId()),
                () -> assertEquals(order.getShopId(), pageResponse.content().get(0).getShopId()),
                () -> assertEquals(order.getTotal(), pageResponse.content().get(0).getTotal()),
                () -> assertEquals(order.getTotalBooks(), pageResponse.content().get(0).getTotalBooks()),
                () -> assertEquals(order.getCreatedAt(), pageResponse.content().get(0).getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), pageResponse.content().get(0).getUpdatedAt()),
                () -> assertEquals(order.isDeleted(), pageResponse.content().get(0).isDeleted())
        );

        verify(orderService, times(1)).getOrdersByUserId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void getOrderByUserId_ShouldReturnOrderNotFoundException() throws Exception{
        when(orderService.getOrdersByUserId(any(UUID.class), any(Pageable.class))).thenThrow(new OrderNotFoundException(order.getId()));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/user/{id}", userTest.getId())).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("El pedido con id " + order.getId() + " no existe", errorResponse.msg())
        );

        verify(orderService, times(1)).getOrdersByUserId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void getOrderByUserId_ShouldReturnUserNotFoundException() throws Exception{
        when(orderService.getOrdersByUserId(any(UUID.class), any(Pageable.class))).thenThrow(new UserNotFound(userTest.getId()));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/user/{id}", userTest.getId())).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Usuario con id" + userTest.getId().toString() + " no encontrado", errorResponse.msg())
        );

        verify(orderService, times(1)).getOrdersByUserId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void getOrderByUserId_ShouldReturnEmptyPage() throws Exception{
        when(orderService.getOrdersByUserId(any(UUID.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/user/{id}", userTest.getId())).andReturn().getResponse();


        PageResponse<Order> pageResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructParametricType(PageResponse.class, Order.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(0, pageResponse.totalElements()),
                () -> assertEquals(1, pageResponse.totalPages()),
                () -> assertEquals(0, pageResponse.content().size())
        );

        verify(orderService, times(1)).getOrdersByUserId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void getOrderByClientId_ShouldReturnOrder() throws Exception {
        when(orderService.getOrdersByClientId(any(UUID.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(order)));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/client/{id}", clientTest.getId())).andReturn().getResponse();

        PageResponse<Order> pageResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructParametricType(PageResponse.class, Order.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(1, pageResponse.totalElements()),
                () -> assertEquals(1, pageResponse.totalPages()),
                () -> assertEquals(1, pageResponse.content().size()),
                () -> assertEquals(order.getId(), pageResponse.content().get(0).getId()),
                () -> assertEquals(order.getUserId(), pageResponse.content().get(0).getUserId()),
                () -> assertEquals(order.getClientId(), pageResponse.content().get(0).getClientId()),
                () -> assertEquals(order.getShopId(), pageResponse.content().get(0).getShopId()),
                () -> assertEquals(order.getTotal(), pageResponse.content().get(0).getTotal()),
                () -> assertEquals(order.getTotalBooks(), pageResponse.content().get(0).getTotalBooks()),
                () -> assertEquals(order.getCreatedAt(), pageResponse.content().get(0).getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), pageResponse.content().get(0).getUpdatedAt()),
                () -> assertEquals(order.isDeleted(), pageResponse.content().get(0).isDeleted())
        );

        verify(orderService, times(1)).getOrdersByClientId(any(UUID.class), any(Pageable.class));
    }


    @Test
    void getOrderByClientId_ShouldReturnOrderNotFoundException() throws Exception {
        when(orderService.getOrdersByClientId(any(UUID.class), any(Pageable.class))).thenThrow(new OrderNotFoundException(order.getId()));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/client/{id}", clientTest.getId())).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("El pedido con id " + order.getId() + " no existe", errorResponse.msg())
        );

        verify(orderService, times(1)).getOrdersByClientId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void getOrderByClientId_ShouldReturnClientNotFoundException() throws Exception {
        when(orderService.getOrdersByClientId(any(UUID.class), any(Pageable.class))).thenThrow(new ClientNotFound("id", clientTest.getId()));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/client/{id}", clientTest.getId())).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Client con id: " + clientTest.getId() + " no existe", errorResponse.msg())
        );

        verify(orderService, times(1)).getOrdersByClientId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void getOrderByClientId_ShouldReturnEmptyPage() throws Exception{
        when(orderService.getOrdersByClientId(any(UUID.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/client/{id}", clientTest.getId())).andReturn().getResponse();

        PageResponse<Order> pageResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructParametricType(PageResponse.class, Order.class));


        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(0, pageResponse.totalElements()),
                () -> assertEquals(1, pageResponse.totalPages()),
                () -> assertEquals(0, pageResponse.content().size())
        );

        verify(orderService, times(1)).getOrdersByClientId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void getOrdersByShopId_ShouldReturnOrder() throws Exception{
        when(orderService.getOrdersByShopId(any(UUID.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(order)));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/shop/{id}", shop.getId())).andReturn().getResponse();


        PageResponse<Order> pageResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructParametricType(PageResponse.class, Order.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(1, pageResponse.totalElements()),
                () -> assertEquals(1, pageResponse.totalPages()),
                () -> assertEquals(1, pageResponse.content().size()),
                () -> assertEquals(order.getId(), pageResponse.content().get(0).getId()),
                () -> assertEquals(order.getUserId(), pageResponse.content().get(0).getUserId()),
                () -> assertEquals(order.getClientId(), pageResponse.content().get(0).getClientId()),
                () -> assertEquals(order.getShopId(), pageResponse.content().get(0).getShopId()),
                () -> assertEquals(order.getTotal(), pageResponse.content().get(0).getTotal()),
                () -> assertEquals(order.getTotalBooks(), pageResponse.content().get(0).getTotalBooks()),
                () -> assertEquals(order.getCreatedAt(), pageResponse.content().get(0).getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), pageResponse.content().get(0).getUpdatedAt()),
                () -> assertEquals(order.isDeleted(), pageResponse.content().get(0).isDeleted())
        );

        verify(orderService, times(1)).getOrdersByShopId(any(UUID.class), any(Pageable.class));
    }


    @Test
    void getOrdersByShopId_ShouldReturnOrderNotFoundException() throws Exception{
        when(orderService.getOrdersByShopId(any(UUID.class), any(Pageable.class))).thenThrow(new OrderNotFoundException(order.getId()));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/shop/{id}", shop.getId())).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("El pedido con id " + order.getId() + " no existe", errorResponse.msg())
        );

        verify(orderService, times(1)).getOrdersByShopId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void getOrdersByShopId_ShouldReturnShopNotFoundException() throws Exception{
        when(orderService.getOrdersByShopId(any(UUID.class), any(Pageable.class))).thenThrow(new ShopNotFoundException("La tienda con id " + shop.getId().toString() + " no existe"));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/shop/{id}", shop.getId())).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Tienda no encontrada - " + "La tienda con id " + shop.getId().toString() + " no existe", errorResponse.msg())
        );

        verify(orderService, times(1)).getOrdersByShopId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void getOrdersByShopId_ShouldReturnEmptyPage() throws Exception{
        when(orderService.getOrdersByShopId(any(UUID.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/shop/{id}", shop.getId())).andReturn().getResponse();


        PageResponse<Order> pageResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructParametricType(PageResponse.class, Order.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(0, pageResponse.totalElements()),
                () -> assertEquals(1, pageResponse.totalPages()),
                () -> assertEquals(0, pageResponse.content().size())
        );

        verify(orderService, times(1)).getOrdersByShopId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void deleteLogic_ShouldReturnOrder() throws Exception{
        Order order = Order.builder()
                .id(new ObjectId())
                .userId(userTest.getId())
                .clientId(clientTest.getId())
                .shopId(shop.getId())
                .isDeleted(true)
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
        when(orderService.deleteLogicOrder(any(ObjectId.class))).thenReturn(order);

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/delete/{id}", order.getId())).andReturn().getResponse();

        Order orderResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), Order.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(order.getId(), orderResponse.getId()),
                () -> assertEquals(order.getUserId(), orderResponse.getUserId()),
                () -> assertEquals(order.getClientId(), orderResponse.getClientId()),
                () -> assertEquals(order.getShopId(), orderResponse.getShopId()),
                () -> assertEquals(order.getTotal(), orderResponse.getTotal()),
                () -> assertEquals(order.getTotalBooks(), orderResponse.getTotalBooks()),
                () -> assertEquals(order.getCreatedAt(), orderResponse.getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), orderResponse.getUpdatedAt()),
                () -> assertTrue(orderResponse.isDeleted())
        );

        verify(orderService, times(1)).deleteLogicOrder(any(ObjectId.class));
    }

    @Test
    void deleteLogic_ShouldReturnOrderNotFoundException() throws Exception{
        when(orderService.deleteLogicOrder(any(ObjectId.class))).thenThrow(new OrderNotFoundException(order.getId()));

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/delete/{id}", order.getId())).andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("El pedido con id " + order.getId() + " no existe", errorResponse.msg())
        );

        verify(orderService, times(1)).deleteLogicOrder(any(ObjectId.class));
    }









}
