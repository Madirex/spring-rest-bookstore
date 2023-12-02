package com.nullers.restbookstore.rest.orders.services;

import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.book.model.Book;
import com.nullers.restbookstore.rest.book.repository.BookRepository;
import com.nullers.restbookstore.rest.category.model.Categoria;
import com.nullers.restbookstore.rest.client.exceptions.ClientNotFound;
import com.nullers.restbookstore.rest.common.Address;
import com.nullers.restbookstore.rest.client.model.Client;
import com.nullers.restbookstore.rest.client.repository.ClientRepository;
import com.nullers.restbookstore.rest.orders.dto.OrderCreateDto;
import com.nullers.restbookstore.rest.orders.exceptions.OrderBadPriceException;
import com.nullers.restbookstore.rest.orders.exceptions.OrderNotFoundException;
import com.nullers.restbookstore.rest.orders.exceptions.OrderNotItemsExceptions;
import com.nullers.restbookstore.rest.orders.exceptions.OrderNotStockException;
import com.nullers.restbookstore.rest.orders.models.Order;
import com.nullers.restbookstore.rest.orders.models.OrderLine;
import com.nullers.restbookstore.rest.orders.repositories.OrderRepository;
import com.nullers.restbookstore.rest.publisher.model.Publisher;
import com.nullers.restbookstore.rest.shop.exceptions.ShopNotFoundException;
import com.nullers.restbookstore.rest.shop.model.Shop;
import com.nullers.restbookstore.rest.shop.repository.ShopRepository;
import com.nullers.restbookstore.rest.user.exceptions.UserNotFound;
import com.nullers.restbookstore.rest.user.models.Role;
import com.nullers.restbookstore.rest.user.models.User;
import com.nullers.restbookstore.rest.user.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ShopRepository shopRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

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

    @Test
    void getAllOrdersTest_ShouldReturnAllOrders() {
        List<Order> orders = List.of(order);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        when(orderRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(orders));

        Page<Order> result = orderService.getAllOrders(pageable);

        assertAll(
             () -> assertEquals(1, result.getTotalElements()),
             () -> assertEquals(1, result.getTotalPages()),
             () -> assertEquals(1, result.getContent().size()),
             () -> assertEquals(order.getId(), result.getContent().get(0).getId()),
            () -> assertEquals(order.getUserId(), result.getContent().get(0).getUserId()),
            () -> assertEquals(order.getClientId(), result.getContent().get(0).getClientId()),
            () -> assertEquals(order.getOrderLines().size(), result.getContent().get(0).getOrderLines().size()),
            () -> assertEquals(order.getTotal(), result.getContent().get(0).getTotal()),
            () -> assertEquals(order.getTotalBooks(), result.getContent().get(0).getTotalBooks()),
            () -> assertEquals(order.getCreatedAt(), result.getContent().get(0).getCreatedAt()),
            () -> assertEquals(order.getUpdatedAt(), result.getContent().get(0).getUpdatedAt())
        );

        verify(orderRepository, times(1)).findAll(any(PageRequest.class));
    }


    @Test
    void getAllOrdersTest_ShouldReturnEmptyList() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        when(orderRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of()));

        Page<Order> result = orderService.getAllOrders(pageable);

        assertAll(
                () -> assertEquals(0, result.getTotalElements()),
                () -> assertEquals(1, result.getTotalPages()),
                () -> assertEquals(0, result.getContent().size())
        );

        verify(orderRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void getOrderById_ShouldReturnOrder(){
        when(orderRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(new ObjectId());

        assertAll(
                () -> assertEquals(order.getId(), result.getId()),
                () -> assertEquals(order.getUserId(), result.getUserId()),
                () -> assertEquals(order.getClientId(), result.getClientId()),
                () -> assertEquals(order.getOrderLines().size(), result.getOrderLines().size()),
                () -> assertEquals(order.getTotal(), result.getTotal()),
                () -> assertEquals(order.getTotalBooks(), result.getTotalBooks()),
                () -> assertEquals(order.getCreatedAt(), result.getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), result.getUpdatedAt())
        );

        verify(orderRepository, times(1)).findById(any(ObjectId.class));
    }

    @Test
    void getOrderById_ShouldThrowOrderNotFoundException(){
        when(orderRepository.findById(any(ObjectId.class))).thenReturn(Optional.empty());

        var res = assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(order.getId()));

        assertAll(
                () -> assertEquals("El pedido con id " + order.get_id() + " no existe", res.getMessage())
        );

        verify(orderRepository, times(1)).findById(any(ObjectId.class));
    }

    @Test
    void createOrder_ShouldReturnOrder_Created(){
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTest));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.of(shop));
        Order result = orderService.createOrder(orderCreateDto);

        assertAll(
                () -> assertEquals(order.getId(), result.getId()),
                () -> assertEquals(order.getUserId(), result.getUserId()),
                () -> assertEquals(order.getClientId(), result.getClientId()),
                () -> assertEquals(order.getOrderLines().size(), result.getOrderLines().size()),
                () -> assertEquals(order.getTotal(), result.getTotal()),
                () -> assertEquals(order.getTotalBooks(), result.getTotalBooks()),
                () -> assertEquals(order.getCreatedAt(), result.getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), result.getUpdatedAt())
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(bookRepository, times(4)).findById(any(Long.class));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
        verify(bookRepository, times(2)).save(any(Book.class));
    }

    @Test
    void createOrder_ShouldThrowUserNotFoundException(){
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var res = assertThrows(UserNotFound.class, () -> orderService.createOrder(orderCreateDto));

        assertAll(
                () -> assertEquals("El usuario con id " + orderCreateDto.getUserId() + " no existe", res.getMessage())
        );

        verify(orderRepository, times(0)).save(any(Order.class));
        verify(bookRepository, times(0)).findById(any(Long.class));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(0)).findById(any(UUID.class));
    }


    @Test
    void createOrder_ShouldThrowClientNotFoundException() {
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTest));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var res = assertThrows(ClientNotFound.class, () -> orderService.createOrder(orderCreateDto));

        assertAll(
                () -> assertEquals("Client con "+"id"+": " + clientTest.getId() + " no existe", res.getMessage())
        );

        verify(orderRepository, times(0)).save(any(Order.class));
        verify(bookRepository, times(0)).findById(any(Long.class));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void createOrder_WithEmptyLines_ShouldThrowException() {
        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(userTest.getId())
                .shopId(shop.getId())
                .clientId(clientTest.getId())
                .orderLines(List.of())
                .build();

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTest));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.of(shop));

        var res = assertThrows(OrderNotItemsExceptions.class, () -> orderService.createOrder(orderCreateDto));

        assertAll(
                () -> assertTrue(res.getMessage().contains("El pedido con id ")),
                () -> assertTrue(res.getMessage().contains(" no tiene items"))
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(orderRepository, times(0)).save(any(Order.class));
        verify(bookRepository, times(0)).findById(any(Long.class));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void createOrder_WithNotEnoughStock_ShouldThrowException() {
        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(userTest.getId())
                .clientId(clientTest.getId())
                .shopId(shop.getId())
                .orderLines(List.of(
                        OrderLine.builder()
                                .bookId(book.getId()).price(book.getPrice()).quantity(11).build()
                                ,orderLine2)
                        ).build();

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTest));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.of(shop));

        var res = assertThrows(OrderNotStockException.class, () -> orderService.createOrder(orderCreateDto));

        assertAll(
                () -> assertTrue(res.getMessage().contains("El producto con id ")),
                () -> assertTrue(res.getMessage().contains(" no tiene stock"))
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(orderRepository, times(0)).save(any(Order.class));
        verify(bookRepository, times(1)).findById(any(Long.class));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void createOrder_WithBadPrice_ShouldThrowException() {
        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(userTest.getId())
                .clientId(clientTest.getId())
                .shopId(shop.getId())
                .orderLines(List.of(
                        OrderLine.builder()
                                .bookId(book.getId()).price(2.0).quantity(1).build()
                        ,orderLine2)
                ).build();

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTest));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.of(shop));

        var res = assertThrows(OrderBadPriceException.class, () -> orderService.createOrder(orderCreateDto));

        assertAll(
                () -> assertTrue(res.getMessage().contains("El precio del producto con id ")),
                () -> assertTrue(res.getMessage().contains(" no es correcto"))
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(orderRepository, times(0)).save(any(Order.class));
        verify(bookRepository, times(1)).findById(any(Long.class));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void createOrder_ShouldReturnShopNotFoundException() throws Exception{
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTest));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var res = assertThrows(ShopNotFoundException.class, () -> orderService.createOrder(orderCreateDto));

        assertAll(
                () -> assertEquals("Tienda no encontrada - La tienda con id b5f29063-77d8-4d5d-98ea-def0cc9ebc5f no existe", res.getMessage())
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(orderRepository, times(0)).save(any(Order.class));
        verify(bookRepository, times(0)).findById(any(Long.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
        verify(userRepository, times(1)).findById(any(UUID.class));

    }

    @Test
    void updateOrder_ShouldReturnShopNotFoundException() throws Exception{
        when(orderRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(order));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTest));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var res = assertThrows(ShopNotFoundException.class, () -> orderService.updateOrder(order.getId(), orderCreateDto));

        assertAll(
                () -> assertEquals("Tienda no encontrada - La tienda con id b5f29063-77d8-4d5d-98ea-def0cc9ebc5f no existe", res.getMessage())
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(orderRepository, times(1)).findById(any(ObjectId.class));
        verify(orderRepository, times(0)).save(any(Order.class));
        verify(bookRepository, times(0)).findById(any(Long.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
        verify(userRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void updateOrder_ReturnOrder_Updated(){
        when(orderRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTest));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.of(shop));

        Order result = orderService.updateOrder(order.getId(), orderCreateDto);

        assertAll(
                () -> assertEquals(order.getId(), result.getId()),
                () -> assertEquals(order.getUserId(), result.getUserId()),
                () -> assertEquals(order.getClientId(), result.getClientId()),
                () -> assertEquals(order.getOrderLines().size(), result.getOrderLines().size()),
                () -> assertEquals(order.getTotal(), result.getTotal()),
                () -> assertEquals(order.getTotalBooks(), result.getTotalBooks()),
                () -> assertEquals(order.getCreatedAt(), result.getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), result.getUpdatedAt())
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(orderRepository, times(1)).findById(any(ObjectId.class));
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(bookRepository, times(6)).findById(any(Long.class));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
        verify(bookRepository, times(4)).save(any(Book.class));
    }

    @Test
    void updateOrder_ShouldThrowOrderNotFoundException(){
        when(orderRepository.findById(any(ObjectId.class))).thenReturn(Optional.empty());

        var res = assertThrows(OrderNotFoundException.class, () -> orderService.updateOrder(order.getId(), orderCreateDto));

        assertAll(
                () -> assertEquals("El pedido con id " + order.get_id() + " no existe", res.getMessage())
        );

        verify(orderRepository, times(1)).findById(any(ObjectId.class));
        verify(orderRepository, times(0)).save(any(Order.class));
        verify(bookRepository, times(0)).findById(any(Long.class));
        verify(userRepository, times(0)).findById(any(UUID.class));
        verify(clientRepository, times(0)).findById(any(UUID.class));
    }

    @Test
    void updateOrder_ShouldThrowUserNotFoundException(){
        when(orderRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(order));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var res = assertThrows(UserNotFound.class, () -> orderService.updateOrder(order.getId(), orderCreateDto));

        assertAll(
                () -> assertEquals("El usuario con id " + orderCreateDto.getUserId() + " no existe", res.getMessage())
        );

        verify(orderRepository, times(1)).findById(any(ObjectId.class));
        verify(orderRepository, times(0)).save(any(Order.class));
        verify(bookRepository, times(0)).findById(any(Long.class));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(0)).findById(any(UUID.class));
    }

    @Test
    void updateOrder_ShouldThrowClientNotFoundException(){
        when(orderRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(order));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTest));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var res = assertThrows(ClientNotFound.class, () -> orderService.updateOrder(order.getId(), orderCreateDto));

        assertAll(
                () -> assertEquals("Client con "+"id"+": " + clientTest.getId() + " no existe", res.getMessage())
        );

        verify(orderRepository, times(1)).findById(any(ObjectId.class));
        verify(orderRepository, times(0)).save(any(Order.class));
        verify(bookRepository, times(0)).findById(any(Long.class));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void updateOrder_WithEmptyLines_ShouldThrowException() {
        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(userTest.getId())
                .clientId(clientTest.getId())
                .shopId(shop.getId())
                .orderLines(List.of())
                .build();

        when(orderRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(order));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTest));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.of(shop));

        var res = assertThrows(OrderNotItemsExceptions.class, () -> orderService.updateOrder(order.getId(), orderCreateDto));

        assertAll(
                () -> assertTrue(res.getMessage().contains("El pedido con id ")),
                () -> assertTrue(res.getMessage().contains(" no tiene items"))
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(orderRepository, times(1)).findById(any(ObjectId.class));
        verify(orderRepository, times(0)).save(any(Order.class));
        verify(bookRepository, times(0)).findById(any(Long.class));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void updateOrder_WithNotEnoughStock_ShouldThrowException() {
        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(userTest.getId())
                .clientId(clientTest.getId())
                .shopId(shop.getId())
                .orderLines(List.of(
                        OrderLine.builder()
                                .bookId(book.getId()).price(book.getPrice()).quantity(20).build()
                        ,orderLine2)
                ).build();

        when(orderRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(order));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTest));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.of(shop));

        var res = assertThrows(OrderNotStockException.class, () -> orderService.updateOrder(order.getId(), orderCreateDto));

        assertAll(
                () -> assertTrue(res.getMessage().contains("El producto con id ")),
                () -> assertTrue(res.getMessage().contains(" no tiene stock"))
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(orderRepository, times(1)).findById(any(ObjectId.class));
        verify(orderRepository, times(0)).save(any(Order.class));
        verify(bookRepository, times(1)).findById(any(Long.class));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void updateOrder_WithBadPrice_ShouldThrowException() {
        OrderCreateDto orderCreateDto = OrderCreateDto.builder()
                .userId(userTest.getId())
                .clientId(clientTest.getId())
                .shopId(shop.getId())
                .orderLines(List.of(
                        OrderLine.builder()
                                .bookId(book.getId()).price(2.0).quantity(1).build()
                        ,orderLine2)
                ).build();

        when(orderRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(order));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTest));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.of(shop));

        var res = assertThrows(OrderBadPriceException.class, () -> orderService.updateOrder(order.getId(), orderCreateDto));

        assertAll(
                () -> assertTrue(res.getMessage().contains("El precio del producto con id ")),
                () -> assertTrue(res.getMessage().contains(" no es correcto"))
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(orderRepository, times(1)).findById(any(ObjectId.class));
        verify(orderRepository, times(0)).save(any(Order.class));
        verify(bookRepository, times(1)).findById(any(Long.class));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void deleteOrder_ShouldDeleteOrder(){
        when(orderRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(order));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        orderService.deleteOrder(order.getId());

        verify(orderRepository, times(1)).findById(any(ObjectId.class));
        verify(orderRepository, times(1)).deleteById(any(ObjectId.class));
        verify(bookRepository, times(2)).save(any(Book.class));
    }

    @Test
    void deleteOrder_ShouldThrowOrderNotFoundException(){
        when(orderRepository.findById(any(ObjectId.class))).thenReturn(Optional.empty());
        var res = assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(order.getId()));

        assertAll(
                () -> assertEquals("El pedido con id " + order.get_id() + " no existe", res.getMessage())
        );

        verify(orderRepository, times(1)).findById(any(ObjectId.class));
        verify(orderRepository, times(0)).deleteById(any(ObjectId.class));
    }

    @Test
    void deleteOrder_ShouldReturnBookNotFound(){
        when(orderRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(order));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        var res = assertThrows(BookNotFoundException.class, () -> orderService.deleteOrder(order.getId()));

        assertAll(
                () -> assertEquals("Libro no encontrado - El libro con id 1 no existe", res.getMessage())
        );

        verify(orderRepository, times(1)).findById(any(ObjectId.class));
        verify(orderRepository, times(0)).deleteById(any(ObjectId.class));
        verify(bookRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void deleteLogicOrder_ShouldDeleteOrder(){
        when(orderRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        Order orderDeleted = orderService.deleteLogicOrder(order.getId());

        assertAll(
                () -> assertEquals(order.getId(), orderDeleted.getId()),
                () -> assertEquals(order.getUserId(), orderDeleted.getUserId()),
                () -> assertEquals(order.getClientId(), orderDeleted.getClientId()),
                () -> assertEquals(order.getOrderLines().size(), orderDeleted.getOrderLines().size()),
                () -> assertEquals(order.getTotal(), orderDeleted.getTotal()),
                () -> assertEquals(order.getTotalBooks(), orderDeleted.getTotalBooks()),
                () -> assertEquals(order.getCreatedAt(), orderDeleted.getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), orderDeleted.getUpdatedAt()),
                () -> assertTrue(orderDeleted.isDeleted())
        );

        verify(orderRepository, times(1)).findById(any(ObjectId.class));
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void deleteLogicOrder_ShouldThrowOrderNotFoundException(){
        when(orderRepository.findById(any(ObjectId.class))).thenReturn(Optional.empty());
        var res = assertThrows(OrderNotFoundException.class, () -> orderService.deleteLogicOrder(order.getId()));

        assertAll(
                () -> assertEquals("El pedido con id " + order.get_id() + " no existe", res.getMessage())
        );

        verify(orderRepository, times(1)).findById(any(ObjectId.class));
        verify(orderRepository, times(0)).save(any(Order.class));
    }

    @Test
    void getOrdersByUserId_ShoudReturnOrder(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        when(orderRepository.findByUserId(any(UUID.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(order)));

        Page<Order> result = orderService.getOrdersByUserId(userTest.getId(), pageable);

        assertAll(
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(1, result.getTotalPages()),
                () -> assertEquals(1, result.getContent().size()),
                () -> assertEquals(order.getId(), result.getContent().get(0).getId()),
                () -> assertEquals(order.getUserId(), result.getContent().get(0).getUserId()),
                () -> assertEquals(order.getClientId(), result.getContent().get(0).getClientId()),
                () -> assertEquals(order.getOrderLines().size(), result.getContent().get(0).getOrderLines().size()),
                () -> assertEquals(order.getTotal(), result.getContent().get(0).getTotal()),
                () -> assertEquals(order.getTotalBooks(), result.getContent().get(0).getTotalBooks()),
                () -> assertEquals(order.getCreatedAt(), result.getContent().get(0).getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), result.getContent().get(0).getUpdatedAt())
        );

        verify(orderRepository, times(1)).findByUserId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void getOrdersByUserId_ShoudReturnEmptyList(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        when(orderRepository.findByUserId(any(UUID.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        Page<Order> result = orderService.getOrdersByUserId(userTest.getId(), pageable);

        assertAll(
                () -> assertEquals(0, result.getTotalElements()),
                () -> assertEquals(1, result.getTotalPages()),
                () -> assertEquals(0, result.getContent().size())
        );

        verify(orderRepository, times(1)).findByUserId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void getOrdersByClientId_ShoudReturnOrder(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        when(orderRepository.findByClientId(any(UUID.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(order)));

        Page<Order> result = orderService.getOrdersByClientId(clientTest.getId(), pageable);

        assertAll(
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(1, result.getTotalPages()),
                () -> assertEquals(1, result.getContent().size()),
                () -> assertEquals(order.getId(), result.getContent().get(0).getId()),
                () -> assertEquals(order.getUserId(), result.getContent().get(0).getUserId()),
                () -> assertEquals(order.getClientId(), result.getContent().get(0).getClientId()),
                () -> assertEquals(order.getOrderLines().size(), result.getContent().get(0).getOrderLines().size()),
                () -> assertEquals(order.getTotal(), result.getContent().get(0).getTotal()),
                () -> assertEquals(order.getTotalBooks(), result.getContent().get(0).getTotalBooks()),
                () -> assertEquals(order.getCreatedAt(), result.getContent().get(0).getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), result.getContent().get(0).getUpdatedAt())
        );

        verify(orderRepository, times(1)).findByClientId(any(UUID.class), any(Pageable.class));
    }


    @Test
    void getOrdersByClientId_ShoudReturnEmptyList(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        when(orderRepository.findByClientId(any(UUID.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        Page<Order> result = orderService.getOrdersByClientId(clientTest.getId(), pageable);

        assertAll(
                () -> assertEquals(0, result.getTotalElements()),
                () -> assertEquals(1, result.getTotalPages()),
                () -> assertEquals(0, result.getContent().size())
        );

        verify(orderRepository, times(1)).findByClientId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void getOrdersByShopId_ShouldReturnOrder() throws Exception{
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        when(orderRepository.findByShopId(any(UUID.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(order)));

        Page<Order> result = orderService.getOrdersByShopId(shop.getId(), pageable);

        assertAll(
                () -> assertEquals(1, result.getTotalElements()),
                () -> assertEquals(1, result.getTotalPages()),
                () -> assertEquals(1, result.getContent().size()),
                () -> assertEquals(order.getId(), result.getContent().get(0).getId()),
                () -> assertEquals(order.getUserId(), result.getContent().get(0).getUserId()),
                () -> assertEquals(order.getClientId(), result.getContent().get(0).getClientId()),
                () -> assertEquals(order.getOrderLines().size(), result.getContent().get(0).getOrderLines().size()),
                () -> assertEquals(order.getTotal(), result.getContent().get(0).getTotal()),
                () -> assertEquals(order.getTotalBooks(), result.getContent().get(0).getTotalBooks()),
                () -> assertEquals(order.getCreatedAt(), result.getContent().get(0).getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), result.getContent().get(0).getUpdatedAt())
        );

        verify(orderRepository, times(1)).findByShopId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void getOrdersByShopId_ShouldReturnEmptyList() throws Exception{
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        when(orderRepository.findByShopId(any(UUID.class), any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        Page<Order> result = orderService.getOrdersByShopId(shop.getId(), pageable);

        assertAll(
                () -> assertEquals(0, result.getTotalElements()),
                () -> assertEquals(1, result.getTotalPages()),
                () -> assertEquals(0, result.getContent().size())
        );

        verify(orderRepository, times(1)).findByShopId(any(UUID.class), any(Pageable.class));
    }

    @Test
    void existsByUserId_ShouldReturnTrue(){
        when(orderRepository.existsByUserId(any(UUID.class))).thenReturn(true);

        boolean result = orderService.existsByUserId(userTest.getId());

        assertAll(
                () -> assertTrue(result)
        );

        verify(orderRepository, times(1)).existsByUserId(any(UUID.class));
    }

    @Test
    void existsByUserId_ShouldReturnFalse(){
        when(orderRepository.existsByUserId(any(UUID.class))).thenReturn(false);

        boolean result = orderService.existsByUserId(userTest.getId());

        assertAll(
                () -> assertFalse(result)
        );

        verify(orderRepository, times(1)).existsByUserId(any(UUID.class));
    }


    @Test
    void checkOrder_ShoudOkChecked(){
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTest));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.of(shop));
        orderService.checkOrder(order);

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(bookRepository, times(2)).findById(any(Long.class));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void checkOrder_ShouldReturnUserNotFoundException(){
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var res = assertThrows(UserNotFound.class, () -> orderService.checkOrder(order));

        assertAll(
                () -> assertEquals("El usuario con id " + order.getUserId() + " no existe", res.getMessage())
        );

        verify(bookRepository, times(0)).findById(any(Long.class));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(0)).findById(any(UUID.class));
    }

    @Test
    void checkOrder_ShouldReturnClientNotFoundException(){
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTest));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var res = assertThrows(ClientNotFound.class, () -> orderService.checkOrder(order));

        assertAll(
                () -> assertEquals("Client con "+"id"+": " + order.getClientId() + " no existe", res.getMessage())
        );

        verify(bookRepository, times(0)).findById(any(Long.class));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
    }




    @Test
    void checkOrder_ShouldReturnBookNotFoundException(){
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTest));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.of(shop));

        var res = assertThrows(BookNotFoundException.class, () -> orderService.checkOrder(order));

        assertAll(
                () -> assertEquals("Libro no encontrado - El libro con id " + orderLine.getBookId() + " no existe", res.getMessage())
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(bookRepository, times(1)).findById(any(Long.class));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void checkOrder_ShouldReturnOrderNotStockException(){
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTest));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.of(shop));

        var res = assertThrows(OrderNotStockException.class, () -> orderService.checkOrder(
                Order.builder()
                        .id(order.getId())
                        .userId(order.getUserId())
                        .shopId(shop.getId())
                        .clientId(order.getClientId())
                        .isDeleted(order.isDeleted())
                        .orderLines(List.of(
                                OrderLine.builder()
                                        .bookId(book.getId()).price(book.getPrice()).quantity(11).build()
                                ,orderLine2))
                        .updatedAt(order.getUpdatedAt())
                        .createdAt(order.getCreatedAt())
                        .total(order.getTotal())
                        .totalBooks(order.getTotalBooks())
                        .build()
        ));

        assertAll(
                () -> assertEquals("El producto con id " + orderLine.getBookId() + " no tiene stock", res.getMessage())
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(bookRepository, times(1)).findById(any(Long.class));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void checkOrder_ShouldReturnOrderBadPriceException(){
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(Book.builder()
                        .id(book.getId()).price(2.0).stock(11).category(categoria).description("desc").publisher(publisher).name(book.getName()).active(true).build()));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTest));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.of(shop));

        var res = assertThrows(OrderBadPriceException.class, () -> orderService.checkOrder(order));

        assertAll(
                () -> assertEquals("El precio del producto con id " + orderLine.getBookId() + " no es correcto", res.getMessage())
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(bookRepository, times(1)).findById(any(Long.class));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void checkOrder_ShouldReturnOrderNotItemsExceptions(){
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTest));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.of(shop));
        var res = assertThrows(OrderNotItemsExceptions.class, () -> orderService.checkOrder(Order.builder()
                        .id(order.getId())
                        .userId(order.getUserId())
                        .shopId(shop.getId())
                        .clientId(order.getClientId())
                        .isDeleted(order.isDeleted())
                        .orderLines(List.of())
                        .updatedAt(order.getUpdatedAt())
                        .createdAt(order.getCreatedAt())
                        .total(order.getTotal())
                        .totalBooks(order.getTotalBooks())
                .build()));

        assertAll(
                () -> assertEquals("El pedido con id " + order.get_id() + " no tiene items", res.getMessage())
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(bookRepository, times(0)).findById(any(Long.class));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void reserverStockOrder_ShouldReturnOrder(){
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));


        Order result = orderService.reserveStockOrder(order);

        assertAll(
                () -> assertEquals(order.getId(), result.getId()),
                () -> assertEquals(order.getUserId(), result.getUserId()),
                () -> assertEquals(order.getClientId(), result.getClientId()),
                () -> assertEquals(order.getOrderLines().size(), result.getOrderLines().size()),
                () -> assertEquals(order.getTotal(), result.getTotal()),
                () -> assertEquals(order.getTotalBooks(), result.getTotalBooks()),
                () -> assertEquals(order.getCreatedAt(), result.getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), result.getUpdatedAt())
        );

        verify(bookRepository, times(2)).findById(any(Long.class));
        verify(bookRepository, times(2)).save(any(Book.class));
    }

    @Test
    void reserverStockOrder_ShouldReturnBookNotFoundException(){
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        var res = assertThrows(BookNotFoundException.class, () -> orderService.reserveStockOrder(order));

        assertAll(
                () -> assertEquals("Libro no encontrado - El libro con id " + orderLine.getBookId() + " no existe", res.getMessage())
        );

        verify(bookRepository, times(1)).findById(any(Long.class));
        verify(bookRepository, times(0)).save(any(Book.class));
    }

    @Test
    void reserveStockOrder_NotItemsOrder(){

        var res = assertThrows(OrderNotItemsExceptions.class, () -> orderService.reserveStockOrder(Order.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .clientId(order.getClientId())
                .isDeleted(order.isDeleted())
                .orderLines(List.of())
                .updatedAt(order.getUpdatedAt())
                .createdAt(order.getCreatedAt())
                .total(order.getTotal())
                .totalBooks(order.getTotalBooks())
                .build()));

        assertAll(
                () -> assertEquals("El pedido con id " + order.get_id() + " no tiene items", res.getMessage())
        );

        verify(bookRepository, times(0)).findById(any(Long.class));
        verify(bookRepository, times(0)).save(any(Book.class));
    }

    @Test
    void returnStokOrder_ShouldOk(){
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));

        orderService.returnStockPedido(order);

        verify(bookRepository, times(2)).findById(any(Long.class));
        verify(bookRepository, times(2)).save(any(Book.class));
    }

    @Test
    void returnStokOrder_ShouldThrowBookNotFoundException(){
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        var res = assertThrows(BookNotFoundException.class, () -> orderService.returnStockPedido(order));

        assertAll(
                () -> assertEquals("Libro no encontrado - El libro con id " + orderLine.getBookId() + " no existe", res.getMessage())
        );

        verify(bookRepository, times(1)).findById(any(Long.class));
        verify(bookRepository, times(0)).save(any(Book.class));
    }

    @Test
    void returnStokOrder_NotItemsOrder(){

        orderService.returnStockPedido(Order.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .clientId(order.getClientId())
                .isDeleted(order.isDeleted())
                .orderLines(List.of())
                .updatedAt(order.getUpdatedAt())
                .createdAt(order.getCreatedAt())
                .total(order.getTotal())
                .totalBooks(order.getTotalBooks())
                .build());


        verify(bookRepository, times(0)).findById(any(Long.class));
        verify(bookRepository, times(0)).save(any(Book.class));
    }


    @Test
    void createOrder_ShouldReturnOrderWithLinesGroupById() throws Exception{
        Order order = Order.builder()
                .id(new ObjectId("5f8f4f8b9d6b9f2b3c9d4f1a"))
                .userId(userTest.getId())
                .shopId(shop.getId())
                .clientId(clientTest.getId())
                .isDeleted(false)
                .orderLines(List.of(
                        OrderLine.builder()
                                .bookId(book.getId()).price(book.getPrice()).quantity(10).build()))
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .total(10.0)
                .totalBooks(1)
                .build();

        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTest));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.of(shop));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        Order result = orderService.createOrder(OrderCreateDto.builder()
                .userId(userTest.getId())
                .shopId(shop.getId())
                .clientId(clientTest.getId())
                .orderLines(List.of(
                        OrderLine.builder()
                                .bookId(book.getId()).price(book.getPrice()).quantity(5).build()
                        ,OrderLine.builder()
                                .bookId(book.getId()).price(book.getPrice()).quantity(5).build()))
                .build());

        assertAll(
                () -> assertEquals(order.getId(), result.getId()),
                () -> assertEquals(order.getUserId(), result.getUserId()),
                () -> assertEquals(order.getClientId(), result.getClientId()),
                () -> assertEquals(order.getOrderLines().size(), result.getOrderLines().size()),
                () -> assertEquals(order.getOrderLines().get(0).getQuantity(), 10),
                () -> assertEquals(order.getOrderLines().get(0).getPrice(), 1.0),
                () -> assertEquals(order.getOrderLines().get(0).getBookId(), result.getOrderLines().get(0).getBookId()),
                () -> assertEquals(order.getTotal(), result.getTotal()),
                () -> assertEquals(order.getTotalBooks(), result.getTotalBooks()),
                () -> assertEquals(order.getCreatedAt(), result.getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), result.getUpdatedAt())
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(bookRepository, times(2)).findById(any(Long.class));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void updateOrder_ShouldReturnOrderWithLinesGroupById() throws Exception{
        Order order = Order.builder()
                .id(new ObjectId("5f8f4f8b9d6b9f2b3c9d4f1a"))
                .userId(userTest.getId())
                .shopId(shop.getId())
                .clientId(clientTest.getId())
                .isDeleted(false)
                .orderLines(List.of(
                        OrderLine.builder()
                                .bookId(book.getId()).price(book.getPrice()).quantity(10).build()))
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .total(10.0)
                .totalBooks(1)
                .build();

        when(orderRepository.findById(any(ObjectId.class))).thenReturn(Optional.of(order));
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userTest));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.of(shop));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        Order result = orderService.updateOrder(order.getId(), OrderCreateDto.builder()
                .userId(userTest.getId())
                .shopId(shop.getId())
                .clientId(clientTest.getId())
                .orderLines(List.of(
                        OrderLine.builder()
                                .bookId(book.getId()).price(book.getPrice()).quantity(5).build()
                        ,OrderLine.builder()
                                .bookId(book.getId()).price(book.getPrice()).quantity(5).build()))
                .build());

        assertAll(
                () -> assertEquals(order.getId(), result.getId()),
                () -> assertEquals(order.getUserId(), result.getUserId()),
                () -> assertEquals(order.getClientId(), result.getClientId()),
                () -> assertEquals(order.getOrderLines().size(), result.getOrderLines().size()),
                () -> assertEquals(order.getOrderLines().get(0).getQuantity(), 10),
                () -> assertEquals(order.getOrderLines().get(0).getPrice(), 1.0),
                () -> assertEquals(order.getOrderLines().get(0).getBookId(), result.getOrderLines().get(0).getBookId()),
                () -> assertEquals(order.getTotal(), result.getTotal()),
                () -> assertEquals(order.getTotalBooks(), result.getTotalBooks()),
                () -> assertEquals(order.getCreatedAt(), result.getCreatedAt()),
                () -> assertEquals(order.getUpdatedAt(), result.getUpdatedAt())
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(orderRepository, times(1)).findById(any(ObjectId.class));
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(bookRepository, times(3)).findById(any(Long.class));
        verify(userRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
        verify(bookRepository, times(2)).save(any(Book.class));
    }



}
