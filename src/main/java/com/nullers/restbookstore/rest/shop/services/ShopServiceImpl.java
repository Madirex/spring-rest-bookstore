package com.nullers.restbookstore.rest.shop.services;

import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.book.model.Book;
import com.nullers.restbookstore.rest.book.repository.BookRepository;
import com.nullers.restbookstore.rest.client.exceptions.ClientNotFound;
import com.nullers.restbookstore.rest.client.model.Client;
import com.nullers.restbookstore.rest.client.repository.ClientRepository;
import com.nullers.restbookstore.rest.orders.models.Order;
import com.nullers.restbookstore.rest.orders.repositories.OrderRepository;
import com.nullers.restbookstore.rest.shop.dto.CreateShopDto;
import com.nullers.restbookstore.rest.shop.dto.GetShopDto;
import com.nullers.restbookstore.rest.shop.dto.UpdateShopDto;
import com.nullers.restbookstore.rest.shop.exceptions.ShopHasOrders;
import com.nullers.restbookstore.rest.shop.exceptions.ShopNotFoundException;
import com.nullers.restbookstore.rest.shop.mappers.ShopMapperImpl;
import com.nullers.restbookstore.rest.shop.model.Shop;
import com.nullers.restbookstore.rest.shop.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio que implementa las operaciones de negocio para la entidad Shop.
 * Utiliza ShopRepository para acceso a datos y ShopMapper para la conversión entre entidades y DTO.
 *
 * @author alexdor00
 */
@Service
public class ShopServiceImpl implements ShopService {

    public static final String SHOP_NOT_FOUND_WITH_ID_MSG = "Tienda no encontrada con ID: ";
    private final ShopRepository shopRepository;
    private final ShopMapperImpl shopMapper;

    private final BookRepository bookRepository;

    private final ClientRepository clientRepository;

    private final OrderRepository orderRepository;

    /**
     * Constructor que inyecta el repositorio de tiendas y el mapper.
     *
     * @param shopRepository   Repositorio para las operaciones de base de datos de Shop.
     * @param shopMapper       Mapper para convertir entre Shop y sus DTOs.
     * @param bookRepository   Repositorio para las operaciones de base de datos de Book.
     * @param clientRepository Repositorio para las operaciones de base de datos de Client.
     * @param orderRepository  Repositorio para las operaciones de base de datos de Order.
     */
    @Autowired
    public ShopServiceImpl(ShopRepository shopRepository, ShopMapperImpl shopMapper, BookRepository bookRepository, ClientRepository clientRepository, OrderRepository orderRepository) {
        this.shopRepository = shopRepository;
        this.shopMapper = shopMapper;
        this.bookRepository = bookRepository;
        this.clientRepository = clientRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * Obtiene todas las tiendas y las convierte a DTO.
     * Los resultados se almacenan en caché para mejorar el rendimiento.
     *
     * @return Lista de tiendas en forma de DTO.
     */
    @Cacheable("shops")
    public Page<GetShopDto> getAllShops(Optional<String> name, Optional<String> locate, PageRequest pageable) {
        Specification<Shop> nameType = (root, query, criteriaBuilder) -> name.map(m -> {
            try {
                return criteriaBuilder.equal(criteriaBuilder.upper(root.get("name")), m.toUpperCase());
            } catch (IllegalArgumentException e) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(false));
            }
        }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Shop> locateType = (root, query, criteriaBuilder) -> locate.map(m -> {
            try {
                return criteriaBuilder.equal(criteriaBuilder.upper(root.get("location")), m.toUpperCase());
            } catch (IllegalArgumentException e) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(false));
            }
        }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));
        Specification<Shop> criterion = Specification.where(nameType)
                .and(locateType);
        Page<Shop> shopPage = shopRepository.findAll(criterion, pageable);
        List<GetShopDto> dtoList = shopPage.getContent().stream()
                .map(shopMapper::toGetShopDto)
                .toList();

        return new PageImpl<>(dtoList, shopPage.getPageable(), shopPage.getTotalElements());
    }

    /**
     * Obtiene una tienda por su UUID y la convierte a DTO.
     *
     * @param id Identificador UUID de la tienda.
     * @return DTO de la tienda encontrada.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     */
    @Override
    public GetShopDto getShopById(UUID id) throws ShopNotFoundException {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new ShopNotFoundException(SHOP_NOT_FOUND_WITH_ID_MSG + id));
        return shopMapper.toGetShopDto(shop);
    }

    /**
     * Crea una nueva tienda a partir de un DTO y la guarda en la base de datos.
     *
     * @param shopDto DTO con los datos para crear la tienda.
     * @return DTO de la tienda creada.
     */
    @Override
    public GetShopDto createShop(CreateShopDto shopDto) {
        Shop shop = shopMapper.toShop(shopDto);
        shop = shopRepository.save(shop);
        return shopMapper.toGetShopDto(shop);
    }

    /**
     * Actualiza una tienda existente con los datos proporcionados en el DTO.
     *
     * @param id      Identificador UUID de la tienda a actualizar.
     * @param shopDto DTO con los datos actualizados.
     * @return DTO de la tienda actualizada.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     */
    @Override
    public GetShopDto updateShop(UUID id, UpdateShopDto shopDto) throws ShopNotFoundException {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new ShopNotFoundException(SHOP_NOT_FOUND_WITH_ID_MSG + id));
        shop = shopMapper.toShop(shop, shopDto);
        shop = shopRepository.save(shop);
        return shopMapper.toGetShopDto(shop);
    }

    /**
     * Elimina una tienda de la base de datos por su UUID y evita la caché relacionada.
     *
     * @param id Identificador UUID de la tienda a eliminar.
     * @throws ShopNotFoundException Si la tienda no se encuentra.
     */
    @CacheEvict(value = "shops", allEntries = true)
    @Override
    public void deleteShop(UUID id) {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new ShopNotFoundException(SHOP_NOT_FOUND_WITH_ID_MSG + id));
        Page<Order> order = orderRepository.findByShopId(id, PageRequest.of(0, 1));
        if (order.getTotalElements() > 0) {
            throw new ShopHasOrders("La tienda no se puede eliminar porque tiene pedidos asociados");
        }

        shopRepository.delete(shop);
    }

    /**
     * Añade un libro a la tienda
     *
     * @param id     Shop id
     * @param bookId Book id
     * @return GetShopDto
     */
    @Override
    public GetShopDto addBookToShop(UUID id, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId.toString()));
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new ShopNotFoundException(id.toString()));
        Set<Book> books = new HashSet<>(shop.getBooks());

        books.add(book);
        shop.setBooks(books);
        return shopMapper.toGetShopDto(shopRepository.save(shop));
    }

    /**
     * Elimina un libro de la tienda
     *
     * @param id     Shop id
     * @param bookId Book id
     * @return GetShopDto
     */
    @Override
    public GetShopDto removeBookFromShop(UUID id, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId.toString()));
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new ShopNotFoundException(id.toString()));

        Set<Book> books = shop.getBooks()
                .stream()
                .filter(b -> !b.equals(book))
                .collect(Collectors.toSet());
        shop.setBooks(books);
        return shopMapper.toGetShopDto(shopRepository.save(shop));
    }

    /**
     * Añade un cliente a la tienda
     *
     * @param id       Shop id
     * @param clientId Client id
     * @return GetShopDto
     */
    @Override
    public GetShopDto addClientToShop(UUID id, UUID clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFound("id", clientId.toString()));
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new ShopNotFoundException(id.toString()));

        Set<Client> clients = new HashSet<>(shop.getClients());
        clients.add(client);
        shop.setClients(clients);
        return shopMapper.toGetShopDto(shopRepository.save(shop));
    }

    /**
     * Elimina un cliente de la tienda
     *
     * @param id       Shop id
     * @param clientId Client id
     * @return GetShopDto
     */
    @Override
    public GetShopDto removeClientFromShop(UUID id, UUID clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFound("id", clientId.toString()));
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new ShopNotFoundException(id.toString()));

        Set<Client> clients = shop.getClients()
                .stream()
                .filter(c -> !c.equals(client))
                .collect(Collectors.toSet());
        shop.setClients(clients);
        return shopMapper.toGetShopDto(shopRepository.save(shop));
    }

}
