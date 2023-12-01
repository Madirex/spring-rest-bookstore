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

@Service
@CacheConfig(cacheNames = "orders")
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    private final ClientRepository clientRepository;

    private final ShopRepository shopRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, BookRepository bookRepository, UserRepository userRepository, ClientRepository clientRepository, ShopRepository shopRepository) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.shopRepository = shopRepository;
    }

    @Override
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    @Cacheable(key = "#id")
    public Order getOrderById(ObjectId id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    @CachePut(key = "#result.id")
    public Order createOrder(OrderCreateDto orderCreateDto) {
        Order order = OrderCreateMapper.toOrder(orderCreateDto);
        checkOrder(order);
        order = reserveStockOrder(order);
        return orderRepository.save(order);
    }

    @Override
    @CachePut(key = "#id")
    public Order updateOrder(ObjectId id, OrderCreateDto orderCreateDto) {
        Order orderToUpdate = getOrderById(id);
        Order order = OrderCreateMapper.toOrder(orderCreateDto);
        checkOrder(order);
        returnStockPedido(orderToUpdate);
        ObjectId idOrder = orderToUpdate.getId();
        orderToUpdate = reserveStockOrder(order);
        orderToUpdate.setId(idOrder);

        return orderRepository.save(orderToUpdate);
    }

    @Override
    @CacheEvict(key = "#id")
    public void deleteOrder(ObjectId id) throws OrderNotFoundException {
        Order order = getOrderById(id);
        returnStockPedido(order);
        orderRepository.deleteById(id);
    }

    @Override
    public Page<Order> getOrdersByUserId(UUID userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable);
    }

    @Override
    public Page<Order> getOrdersByClientId(UUID clientId, Pageable pageable) {
        return orderRepository.findByClientId(clientId, pageable);
    }

    @Override
    public Page<Order> getOrdersByShopId(UUID shopId, Pageable pageable) {
        return orderRepository.findByShopId(shopId, pageable);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return orderRepository.existsByUserId(userId);
    }

    @Override
    public Order deleteLogicOrder(ObjectId id) {
        Order order = getOrderById(id);
        order.setDeleted(true);
        return orderRepository.save(order);
    }


    public void checkOrder(Order order){

        UUID idUser = order.getUserId();
        userRepository.findById(idUser).orElseThrow(() -> new UserNotFound("El usuario con id " + idUser + " no existe"));
        UUID idClient = order.getClientId();
        clientRepository.findById(idClient).orElseThrow(() -> new ClientNotFound("id", idClient.toString()));
        UUID idShop = order.getShopId();
        shopRepository.findById(idShop).orElseThrow(() -> new ShopNotFoundException("La tienda con id " + idShop + " no existe"));

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
                .collect(Collectors.toList());

        order.setOrderLines(mergedOrderLines);
        orderLines = order.getOrderLines();

        if(orderLines == null || orderLines.isEmpty()) {
            throw new OrderNotItemsExceptions(order.get_id());
        }

        orderLines.stream().forEach(lp -> {
            Book book = bookRepository.findById(lp.getBookId()).orElseThrow(() -> new BookNotFoundException("El libro con id " + lp.getBookId() + " no existe"));
            if(book.getStock() < lp.getQuantity() && lp.getQuantity() > 0) {
                throw new OrderNotStockException(book.getId());
            }
            if(!lp.getPrice().equals(book.getPrice())) {
                throw new OrderBadPriceException(book.getId());
            }
        });
    }

    Order reserveStockOrder(Order order){
        List<OrderLine> orderLines = order.getOrderLines();
        if(orderLines == null || orderLines.isEmpty()) {
            throw new OrderNotItemsExceptions(order.get_id());
        }

        orderLines.stream().forEach(lp -> {
            Book book = bookRepository.findById(lp.getBookId()).orElseThrow(() -> new BookNotFoundException("El libro con id " + lp.getBookId() + " no existe"));
            book.setStock(book.getStock() - lp.getQuantity());
            bookRepository.save(book);
            lp.setTotal(lp.getQuantity() * lp.getPrice());
        });

        order.calculateLines();
        order.getOrderLines().stream().forEach(lineaPedido -> lineaPedido.calculatePrice(lineaPedido.getPrice()));
        return order;
    }

    void returnStockPedido(Order order){
        if(order.getOrderLines() != null || !order.getOrderLines().isEmpty() ){
            order.getOrderLines().stream().forEach(lp -> {
                Book book = bookRepository.findById(lp.getBookId()).orElseThrow(() -> new BookNotFoundException("El libro con id " + lp.getBookId() + " no existe"));
                book.setStock(book.getStock() + lp.getQuantity());
                bookRepository.save(book);
            });
        }
    }

}
