package com.nullers.restbookstore.rest.orders.services;

import com.nullers.restbookstore.rest.book.exceptions.BookNotFoundException;
import com.nullers.restbookstore.rest.book.model.Book;
import com.nullers.restbookstore.rest.book.repository.BookRepository;
import com.nullers.restbookstore.rest.client.exceptions.ClientNotFound;
import com.nullers.restbookstore.rest.client.repository.ClientRepository;
import com.nullers.restbookstore.rest.orders.dto.OrderCreateDto;
import com.nullers.restbookstore.rest.orders.exceptions.OrderBadPriceException;
import com.nullers.restbookstore.rest.orders.exceptions.OrderNotFoundException;
import com.nullers.restbookstore.rest.orders.exceptions.OrderNotItemsExceptions;
import com.nullers.restbookstore.rest.orders.exceptions.OrderNotStockException;
import com.nullers.restbookstore.rest.orders.mappers.OrderCreateMapper;
import com.nullers.restbookstore.rest.orders.models.Order;
import com.nullers.restbookstore.rest.orders.models.OrderLine;
import com.nullers.restbookstore.rest.orders.repositories.OrderRepository;
import com.nullers.restbookstore.rest.shop.exceptions.ShopNotFoundException;
import com.nullers.restbookstore.rest.shop.repository.ShopRepository;
import com.nullers.restbookstore.rest.user.exceptions.UserNotFound;
import com.nullers.restbookstore.rest.user.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Clase OrderServiceImpl
 */
@Service
@CacheConfig(cacheNames = "orders")
public class OrderServiceImpl implements OrderService {

    public static final String BOOK_WITH_ID_STR = "El libro con id ";
    public static final String NO_EXISTS_MSG = " no existe";
    private final OrderRepository orderRepository;

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    private final ClientRepository clientRepository;

    private final ShopRepository shopRepository;

    /**
     * Constructor para crear una nueva OrderServiceImpl
     *
     * @param orderRepository  order repository
     * @param bookRepository   book repository
     * @param userRepository   user repository
     * @param clientRepository client repository
     * @param shopRepository   shop repository
     */
    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, BookRepository bookRepository, UserRepository userRepository, ClientRepository clientRepository, ShopRepository shopRepository) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.shopRepository = shopRepository;
    }

    /**
     * Método que devuelve todos los pedidos
     *
     * @param pageable paginación
     * @return todos los pedidos
     */
    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    /**
     * Método que devuelve un pedido por el ID
     *
     * @param id id del pedido
     * @return pedido por el ID
     */
    @Override
    @Cacheable(key = "#id")
    public Order getOrderById(ObjectId id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    /**
     * Método que crea un pedido
     *
     * @param orderCreateDto pedido
     * @return pedido creado
     */
    @Override
    @CachePut(key = "#result.id")
    public Order createOrder(OrderCreateDto orderCreateDto) {
        Order order = OrderCreateMapper.toOrder(orderCreateDto);
        checkOrder(order);
        order = reserveStockOrder(order);
        return orderRepository.save(order);
    }

    /**
     * Método que actualiza un pedido por el ID
     *
     * @param id             id del pedido
     * @param orderCreateDto pedido
     * @return pedido actualizado
     */
    @Override
    @CachePut(key = "#id")
    public Order updateOrder(ObjectId id, OrderCreateDto orderCreateDto) {
        Order orderToUpdate = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        Order order = OrderCreateMapper.toOrder(orderCreateDto);
        checkOrder(order);
        returnStockOrder(orderToUpdate);
        ObjectId idOrder = orderToUpdate.getId();
        orderToUpdate = reserveStockOrder(order);
        orderToUpdate.setId(idOrder);
        return orderRepository.save(orderToUpdate);
    }

    /**
     * Método que elimina un pedido por el ID
     *
     * @param id id del pedido
     * @throws OrderNotFoundException excepción si no existe el pedido
     */
    @Override
    @CacheEvict(key = "#id")
    public void deleteOrder(ObjectId id) throws OrderNotFoundException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        returnStockOrder(order);
        orderRepository.deleteById(id);
    }

    /**
     * Método que devuelve los pedidos de un usuario por el ID del usuario
     *
     * @param userId   id del usuario
     * @param pageable paginación
     * @return pedidos de un usuario por el ID del usuario
     */
    @Override
    public Page<Order> getOrdersByUserId(UUID userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable);
    }

    /**
     * Método que devuelve los pedidos de un usuario por el ID del cliente
     *
     * @param clientId id del cliente
     * @param pageable paginación
     * @return pedidos de un usuario por el ID del cliente
     */
    @Override
    public Page<Order> getOrdersByClientId(UUID clientId, Pageable pageable) {
        return orderRepository.findByClientId(clientId, pageable);
    }

    /**
     * Método que devuelve los pedidos de una tienda por el ID de la tienda
     *
     * @param shopId   id de la tienda
     * @param pageable paginación
     * @return pedidos de una tienda por el ID de la tienda
     */
    @Override
    public Page<Order> getOrdersByShopId(UUID shopId, Pageable pageable) {
        return orderRepository.findByShopId(shopId, pageable);
    }

    /**
     * Método que devuelve si existe un pedido por el ID del usuario
     *
     * @param userId id del usuario
     * @return true si existe un pedido por el ID del usuario
     */
    @Override
    public boolean existsByUserId(UUID userId) {
        return orderRepository.existsByUserId(userId);
    }

    /**
     * Método que devuelve si existe un pedido por el ID del cliente
     *
     * @param id id del pedido
     * @return true si existe un pedido por el ID del cliente
     */
    @Override
    public Order deleteLogicOrder(ObjectId id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        order.setIsDeleted(true);
        return orderRepository.save(order);
    }

    /**
     * Método que comprueba si un pedido es correcto
     *
     * @param order pedido
     */
    public void checkOrder(Order order) {
        UUID idUser = order.getUserId();
        UUID idClient = order.getClientId();
        UUID idShop = order.getShopId();
        var user = userRepository.findById(idUser);
        if (user.isEmpty()) {
            throw new UserNotFound("El usuario con id " + idUser + NO_EXISTS_MSG);
        }
        var client = clientRepository.findById(idClient);
        if (client.isEmpty()) {
            throw new ClientNotFound("id", idClient.toString());
        }
        var shop = shopRepository.findById(idShop);
        if (shop.isEmpty()) {
            throw new ShopNotFoundException("La tienda con id " + idShop + NO_EXISTS_MSG);
        }

        List<OrderLine> orderLines = order.getOrderLines();

        Map<Long, List<OrderLine>> groupedOrderLines = orderLines.stream()
                .collect(Collectors.groupingBy(OrderLine::getBookId));
        List<OrderLine> mergedOrderLines = groupedOrderLines.values().stream()
                .map(lines -> lines.stream()
                        .reduce((line1, line2) -> {
                            line1.setQuantity(line1.getQuantity() + line2.getQuantity());
                            line1.calculatePrice(line1.getPrice());
                            return line1;
                        })
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();

        order.setOrderLines(mergedOrderLines);
        orderLines = order.getOrderLines();

        if (orderLines == null || orderLines.isEmpty()) {
            throw new OrderNotItemsExceptions(order.getIdStr());
        }

        orderLines.forEach(lp -> {
            Book book = bookRepository.findById(lp.getBookId()).orElseThrow(() ->
                    new BookNotFoundException(BOOK_WITH_ID_STR + lp.getBookId() + NO_EXISTS_MSG));
            if (book.getStock() < lp.getQuantity() && lp.getQuantity() > 0) {
                throw new OrderNotStockException(book.getId());
            }
            if (!lp.getPrice().equals(book.getPrice())) {
                throw new OrderBadPriceException(book.getId());
            }
        });
    }

    /**
     * Método que reserva el stock de un pedido
     *
     * @param order pedido
     * @return pedido
     */
    public Order reserveStockOrder(Order order) {
        List<OrderLine> orderLines = order.getOrderLines();
        if (orderLines == null || orderLines.isEmpty()) {
            throw new OrderNotItemsExceptions(order.getIdStr());
        }

        orderLines.forEach(lp -> {
            Book book = bookRepository.findById(lp.getBookId()).orElseThrow(() ->
                    new BookNotFoundException(BOOK_WITH_ID_STR + lp.getBookId() + NO_EXISTS_MSG));
            book.setStock(book.getStock() - lp.getQuantity());
            bookRepository.save(book);
            lp.setTotal(lp.getQuantity() * lp.getPrice());
        });

        order.calculateLines();
        order.getOrderLines().forEach(line -> line.calculatePrice(line.getPrice()));
        return order;
    }

    /**
     * Método que devuelve el stock de un pedido
     *
     * @param order pedido
     */
    public void returnStockOrder(Order order) {
        if (order.getOrderLines() != null && !order.getOrderLines().isEmpty()) {
            order.getOrderLines().forEach(lp -> {
                Book book = bookRepository.findById(lp.getBookId())
                        .orElseThrow(() -> new BookNotFoundException(BOOK_WITH_ID_STR + lp.getBookId() + NO_EXISTS_MSG));
                book.setStock(book.getStock() + lp.getQuantity());
                bookRepository.save(book);
            });
        }
    }
}
