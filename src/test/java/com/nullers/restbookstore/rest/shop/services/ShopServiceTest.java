package com.nullers.restbookstore.rest.shop.services;

import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.book.model.Book;
import com.nullers.restbookstore.rest.book.repository.BookRepository;
import com.nullers.restbookstore.rest.category.model.Category;
import com.nullers.restbookstore.rest.client.exceptions.ClientNotFound;
import com.nullers.restbookstore.rest.client.model.Client;
import com.nullers.restbookstore.rest.client.repository.ClientRepository;
import com.nullers.restbookstore.rest.common.Address;
import com.nullers.restbookstore.rest.orders.models.Order;
import com.nullers.restbookstore.rest.orders.models.OrderLine;
import com.nullers.restbookstore.rest.orders.repositories.OrderRepository;
import com.nullers.restbookstore.rest.publisher.model.Publisher;
import com.nullers.restbookstore.rest.shop.dto.CreateShopDto;
import com.nullers.restbookstore.rest.shop.dto.GetShopDto;
import com.nullers.restbookstore.rest.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.rest.shop.exceptions.ShopHasOrders;
import com.nullers.restbookstore.rest.shop.exceptions.ShopNotFoundException;
import com.nullers.restbookstore.rest.shop.mappers.ShopMapperImpl;
import com.nullers.restbookstore.rest.shop.model.Shop;
import com.nullers.restbookstore.rest.shop.repository.ShopRepository;
import com.nullers.restbookstore.rest.user.models.User;
import com.nullers.restbookstore.rest.user.models.Role;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShopServiceTest {

    @Mock
    private ShopRepository shopRepository;

    @Mock
    private ShopMapperImpl shopMapper;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private ShopServiceImpl shopService;


    CreateShopDto createShopDto = CreateShopDto.builder()
            .name("Tienda 1")
            .location(Address.builder()
                    .province("Madrid")
                    .city("Madrid")
                    .street("Calle 1")
                    .number("1")
                    .postalCode("28001")
                    .country("España")
                    .build())
            .build();

    UpdateShopDto updateShopDto = UpdateShopDto.builder()
            .name("Tienda 2")
            .location(Address.builder()
                    .province("Madrid")
                    .city("Madrid")
                    .street("Calle 2")
                    .number("2")
                    .postalCode("28002")
                    .country("España")
                    .build())
            .build();

    Shop shop = Shop.builder()
            .id(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
            .name("Tienda 1")
            .location(Address.builder()
                    .province("Madrid")
                    .city("Madrid")
                    .street("Calle 1")
                    .number("1")
                    .postalCode("28001")
                    .country("España")
                    .build())
            .clients(Set.of())
            .books(Set.of())
            .build();

    GetShopDto getShopDto;

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


    ShopServiceTest() {
        getShopDto = GetShopDto.builder()
                .id(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .name("Tienda 1")
                .location(Address.builder()
                        .province("Madrid")
                        .city("Madrid")
                        .street("Calle 1")
                        .number("1")
                        .postalCode("28001")
                        .country("España")
                        .build())
                .clients_id(Set.of())
                .books_id(Set.of())
                .build();
    }

    @Test
    void getAllShops_ShoouldReturnShops() {
        when(shopRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(shop)));
        when(shopMapper.toGetShopDto(any(Shop.class))).thenReturn(getShopDto);

        var result = shopService.getAllShops(Optional.empty(), Optional.empty(), PageRequest.of(0, 10));

        System.out.println(result.getContent());
        assertAll(
                () -> assertEquals(shop.getId(), result.getContent().get(0).getId()),
                () -> assertEquals(shop.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(shop.getLocation(), result.getContent().get(0).getLocation())
        );

        verify(shopRepository, times(1)).findAll(any(Specification.class), any(PageRequest.class));
        verify(shopMapper, times(1)).toGetShopDto(any(Shop.class));
    }

    @Test
    void getAllShops_ShouldReturnEmptyList() {
        when(shopRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of()));

        var result = shopService.getAllShops(Optional.empty(), Optional.empty(), PageRequest.of(0, 10));

        assertAll(
                () -> assertEquals(0, result.getContent().size())
        );

        verify(shopRepository, times(1)).findAll(any(Specification.class), any(PageRequest.class));
    }

    @Test
    void getAllShops_ShouldReturnShopsByName() {
        when(shopRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(shop)));
        when(shopMapper.toGetShopDto(any(Shop.class))).thenReturn(getShopDto);

        var result = shopService.getAllShops(Optional.of("Tienda 1"), Optional.empty(), PageRequest.of(0, 10));

        assertAll(
                () -> assertEquals(shop.getId(), result.getContent().get(0).getId()),
                () -> assertEquals(shop.getName(), result.getContent().get(0).getName()),
                () -> assertEquals(shop.getLocation(), result.getContent().get(0).getLocation())
        );

        verify(shopRepository, times(1)).findAll(any(Specification.class), any(PageRequest.class));
        verify(shopMapper, times(1)).toGetShopDto(any(Shop.class));
    }

    @Test
    void getShopById_ShouldReturnShop() {
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.of(shop));
        when(shopMapper.toGetShopDto(any(Shop.class))).thenReturn(getShopDto);

        var result = shopService.getShopById(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        assertAll(
                () -> assertEquals(shop.getId(), result.getId()),
                () -> assertEquals(shop.getName(), result.getName()),
                () -> assertEquals(shop.getLocation(), result.getLocation())
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(shopMapper, times(1)).toGetShopDto(any(Shop.class));
    }

    @Test
    void getShopById_ShouldShopNotFoundException() {
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var res = assertThrows(ShopNotFoundException.class, () -> shopService.getShopById(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")));

        assertAll(
                () -> assertEquals("Tienda no encontrada - " + "Tienda no encontrada con ID: 123e4567-e89b-12d3-a456-426614174000", res.getMessage())
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void createShop_ShouldReturnShop() {
        when(shopRepository.save(any(Shop.class))).thenReturn(shop);
        when(shopMapper.toGetShopDto(any(Shop.class))).thenReturn(getShopDto);
        when(shopMapper.toShop(any(CreateShopDto.class))).thenReturn(shop);

        var result = shopService.createShop(createShopDto);

        assertAll(
                () -> assertEquals(shop.getId(), result.getId()),
                () -> assertEquals(shop.getName(), result.getName()),
                () -> assertEquals(shop.getLocation(), result.getLocation())
        );

        verify(shopRepository, times(1)).save(any(Shop.class));
        verify(shopMapper, times(1)).toGetShopDto(any(Shop.class));
        verify(shopMapper, times(1)).toGetShopDto(any(Shop.class));
    }

    @Test
    void updateShop_ShouldReturnShop() {
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.of(shop));
        when(shopRepository.save(any(Shop.class))).thenReturn(shop);
        when(shopMapper.toShop(any(Shop.class), any(UpdateShopDto.class))).thenReturn(shop);
        when(shopMapper.toGetShopDto(any(Shop.class))).thenReturn(getShopDto);

        var result = shopService.updateShop(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), updateShopDto);

        assertAll(
                () -> assertEquals(shop.getId(), result.getId()),
                () -> assertEquals(shop.getName(), result.getName()),
                () -> assertEquals(shop.getLocation(), result.getLocation())
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(shopRepository, times(1)).save(any(Shop.class));
        verify(shopMapper, times(1)).toGetShopDto(any(Shop.class));
    }

    @Test
    void updateShop_ShouldShopNotFoundException() {
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var res = assertThrows(ShopNotFoundException.class, () -> shopService.updateShop(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), updateShopDto));

        assertAll(
                () -> assertEquals("Tienda no encontrada - " + "Tienda no encontrada con ID: 123e4567-e89b-12d3-a456-426614174000", res.getMessage())
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void deleteShop_ShouldDeleteShop() {
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.of(shop));
        when(orderRepository.findByShopId(any(UUID.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of()));

        shopService.deleteShop(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(orderRepository, times(1)).findByShopId(any(UUID.class), any(PageRequest.class));
        verify(shopRepository, times(1)).delete(any(Shop.class));
    }

    @Test
    void deleteShop_ShouldShopNotFoundException() {
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var res = assertThrows(ShopNotFoundException.class, () -> shopService.deleteShop(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")));

        assertAll(
                () -> assertEquals("Tienda no encontrada - " + "Tienda no encontrada con ID: 123e4567-e89b-12d3-a456-426614174000", res.getMessage())
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void deleteShop_ShouldReturnShopHasOrdersException() {
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.of(shop));
        when(orderRepository.findByShopId(any(UUID.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(order)));

        var res = assertThrows(ShopHasOrders.class, () -> shopService.deleteShop(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")));

        assertAll(
                () -> assertEquals("La tienda no se puede eliminar porque tiene pedidos asociados", res.getMessage())
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(orderRepository, times(1)).findByShopId(any(UUID.class), any(PageRequest.class));
    }

    @Test
    void addBookToShop_ShouldReturnShop() {
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.of(shop));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(shopRepository.save(any(Shop.class))).thenReturn(Shop.builder()
                .id(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .name("Tienda 1")
                .location(Address.builder()
                        .province("Madrid")
                        .city("Madrid")
                        .street("Calle 1")
                        .number("1")
                        .postalCode("28001")
                        .country("España")
                        .build())
                .clients(Set.of())
                .books(Set.of(book))
                .build());
        when(shopMapper.toGetShopDto(any(Shop.class))).thenReturn(GetShopDto.builder()
                .id(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .name("Tienda 1")
                .location(Address.builder()
                        .province("Madrid")
                        .city("Madrid")
                        .street("Calle 1")
                        .number("1")
                        .postalCode("28001")
                        .country("España")
                        .build())
                .clients_id(Set.of())
                .books_id(Set.of(book.getId()))
                .build());

        var result = shopService.addBookToShop(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), 1L);

        assertAll(
                () -> assertEquals(shop.getId(), result.getId()),
                () -> assertEquals(shop.getName(), result.getName()),
                () -> assertEquals(shop.getLocation(), result.getLocation()),
                () -> assertTrue(result.getBooks_id().contains(1L))
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(bookRepository, times(1)).findById(any(Long.class));
        verify(shopRepository, times(1)).save(any(Shop.class));
        verify(shopMapper, times(1)).toGetShopDto(any(Shop.class));
    }

    @Test
    void addBookToShop_ShouldShopNotFoundException() {
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));

        var res = assertThrows(ShopNotFoundException.class, () -> shopService.addBookToShop(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), 1L));

        assertAll(
                () -> assertEquals("Tienda no encontrada - 123e4567-e89b-12d3-a456-426614174000", res.getMessage())
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(shopRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void addBookToShop_ShouldBookNotFoundException() {
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        var res = assertThrows(BookNotFoundException.class, () -> shopService.addBookToShop(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), 1L));

        assertAll(
                () -> assertEquals("Libro no encontrado - 1", res.getMessage())
        );

        verify(bookRepository, times(1)).findById(any(Long.class));
    }


    @Test
    void removeBookFromShop_ShouldReturnShop() {
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.of(shop));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        when(shopRepository.save(any(Shop.class))).thenReturn(shop);
        when(shopMapper.toGetShopDto(any(Shop.class))).thenReturn(getShopDto);

        var result = shopService.removeBookFromShop(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), 1L);

        assertAll(
                () -> assertEquals(shop.getId(), result.getId()),
                () -> assertEquals(shop.getName(), result.getName()),
                () -> assertEquals(shop.getLocation(), result.getLocation()),
                () -> assertTrue(result.getBooks_id().isEmpty())
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(bookRepository, times(1)).findById(any(Long.class));
        verify(shopRepository, times(1)).save(any(Shop.class));
        verify(shopMapper, times(1)).toGetShopDto(any(Shop.class));
    }

    @Test
    void removeBookFromShop_ShouldShopNotFoundException() {
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.of(book));
        var res = assertThrows(ShopNotFoundException.class, () -> shopService.removeBookFromShop(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), 1L));

        assertAll(
                () -> assertEquals("Tienda no encontrada - 123e4567-e89b-12d3-a456-426614174000", res.getMessage())
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(shopRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void removeBookFromShop_ShouldBookNotFoundException() {
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        var res = assertThrows(BookNotFoundException.class, () -> shopService.removeBookFromShop(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), 1L));

        assertAll(
                () -> assertEquals("Libro no encontrado - 1", res.getMessage())
        );

        verify(bookRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void addClientToShop_ShoulReturnGetShopDto() {
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.of(shop));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));
        when(shopRepository.save(any(Shop.class))).thenReturn(
                Shop.builder()
                        .id(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                        .name("Tienda 1")
                        .location(Address.builder()
                                .province("Madrid")
                                .city("Madrid")
                                .street("Calle 1")
                                .number("1")
                                .postalCode("28001")
                                .country("España")
                                .build())
                        .clients(Set.of(clientTest))
                        .books(Set.of())
                        .build()
        );
        when(shopMapper.toGetShopDto(any(Shop.class))).thenReturn(
                GetShopDto.builder()
                        .id(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                        .name("Tienda 1")
                        .location(Address.builder()
                                .province("Madrid")
                                .city("Madrid")
                                .street("Calle 1")
                                .number("1")
                                .postalCode("28001")
                                .country("España")
                                .build())
                        .clients_id(Set.of(clientTest.getId()))
                        .books_id(Set.of())
                        .build()
        );

        var result = shopService.addClientToShop(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"));

        assertAll(
                () -> assertEquals(shop.getId(), result.getId()),
                () -> assertEquals(shop.getName(), result.getName()),
                () -> assertEquals(shop.getLocation(), result.getLocation()),
                () -> assertTrue(result.getClients_id().contains(clientTest.getId()))
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
        verify(shopRepository, times(1)).save(any(Shop.class));
        verify(shopMapper, times(1)).toGetShopDto(any(Shop.class));
    }

    @Test
    void addClientToShop_ShouldShopNotFoundException() {
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));

        var res = assertThrows(ShopNotFoundException.class, () -> shopService.addClientToShop(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0")));

        assertAll(
                () -> assertEquals("Tienda no encontrada - 123e4567-e89b-12d3-a456-426614174000", res.getMessage())
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(shopRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void addClientToShop_ShouldClientNotFoundException() {
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var res = assertThrows(ClientNotFound.class, () -> shopService.addClientToShop(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0")));

        assertAll(
                () -> assertEquals("Client con id: 9def16db-362b-44c4-9fc9-77117758b5b0 no existe", res.getMessage())
        );

        verify(clientRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void removeClientFromShop_ShouldReturnGetShopDto() {
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.of(shop));
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));
        when(shopRepository.save(any(Shop.class))).thenReturn(shop);
        when(shopMapper.toGetShopDto(any(Shop.class))).thenReturn(getShopDto);

        var result = shopService.removeClientFromShop(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0"));

        assertAll(
                () -> assertEquals(shop.getId(), result.getId()),
                () -> assertEquals(shop.getName(), result.getName()),
                () -> assertEquals(shop.getLocation(), result.getLocation()),
                () -> assertTrue(result.getClients_id().isEmpty())
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(clientRepository, times(1)).findById(any(UUID.class));
        verify(shopRepository, times(1)).save(any(Shop.class));
        verify(shopMapper, times(1)).toGetShopDto(any(Shop.class));
    }

    @Test
    void removeClientFromShop_ShouldShopNotFoundException() {
        when(shopRepository.findById(any(UUID.class))).thenReturn(Optional.empty());
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.of(clientTest));

        var res = assertThrows(ShopNotFoundException.class, () -> shopService.removeClientFromShop(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0")));

        assertAll(
                () -> assertEquals("Tienda no encontrada - 123e4567-e89b-12d3-a456-426614174000", res.getMessage())
        );

        verify(shopRepository, times(1)).findById(any(UUID.class));
        verify(shopRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void removeClientFromShop_ShouldClientNotFoundException() {
        when(clientRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        var res = assertThrows(ClientNotFound.class, () -> shopService.removeClientFromShop(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), UUID.fromString("9def16db-362b-44c4-9fc9-77117758b5b0")));

        assertAll(
                () -> assertEquals("Client con id: 9def16db-362b-44c4-9fc9-77117758b5b0 no existe", res.getMessage())
        );

        verify(clientRepository, times(1)).findById(any(UUID.class));
    }


}
