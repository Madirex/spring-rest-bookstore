package com.nullers.restbookstore.rest.shop.services;


import com.nullers.restbookstore.shop.model.Shop;

@ExtendWith(MockitoExtension.class)
public class ShopServiceImplTest {
    List<Shop> list;

    @Mock
    private ShopRepository shopRepository;

    @Mock
    private ShopMapperImpl bookMapperImpl;

    @Mock
    private PublisherMapper publisherMapper;


    @Mock


    @Mock
    private WebSocketConfig webSocketConfig;

    @Mock
    private WebSocketHandler webSocketHandlerMock;

    @Mock
    private BookNotificationMapper bookNotificationMapper;
    @Mock
    private ObjectMapper objectMapper;

}
