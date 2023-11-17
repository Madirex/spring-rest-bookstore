package com.nullers.restbookstore.rest.shop.controllers;

import com.nullers.restbookstore.pagination.util.PaginationLinksUtils;
import com.nullers.restbookstore.rest.shop.model.Shop;
import com.nullers.restbookstore.rest.shop.services.ShopServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(properties = "spring.config.name=application-test")
class ShopControllerWithoutMockMvcTest {


    @Mock
    private ShopServiceImpl shopService;

    @Mock
    private PaginationLinksUtils paginationLinksUtils = new PaginationLinksUtils();

    @InjectMocks
    private ShopRestControllerImpl shopController;

    private final Shop shopTest = Shop.builder()
            .id(UUID.randomUUID())
            .name("Shop1")
            .location("https://via.placeholder.com/150")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();


    private final Shop shopTest2 = Shop.builder()
            .id(UUID.randomUUID())
            .name("Shop2")
            .location("https://via.placeholder.com/150")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();


    @BeforeEach
    void setUp() {
    }

//    @Test
//    void getAll() {
//        when(shopService.getAllShop(any(), any(), any(),
//                any(PageRequest.class))).thenReturn(new PageImpl(List.of(shopTest, shopTest2)));
//        MockHttpServletRequest requestMock = new MockHttpServletRequest();
//        requestMock.setRequestURI("/shops");
//        requestMock.setServerPort(8080);
//
//        var res = shopController.getAllShop(
//                Optional.empty(), Optional.empty(), Optional.empty(),
//                0, 10, "id", "asc",
//                requestMock
//        );
//
//        assertAll(
//                () -> assertEquals(2, Objects.requireNonNull(res.getBody()).content().size()),
//                () -> assertEquals(200, res.getStatusCodeValue())
//        );
//
//        verify(shopService, times(1)).
//                getAllShop(any(), any(), any(), any(PageRequest.class));
//    }

}
