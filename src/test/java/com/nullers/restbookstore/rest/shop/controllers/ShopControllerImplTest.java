package com.nullers.restshopstore.rest.shop.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nullers.restbookstore.rest.shop.dto.GetShopDto;
import com.nullers.restbookstore.rest.shop.model.Shop;
import com.nullers.restbookstore.rest.shop.services.ShopServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Clase ShopControllerImplTest
 */
@SpringBootTest(properties = "spring.config.name=application-test")
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ShopControllerImplTest {
    private final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ShopServiceImpl service;
    @MockBean
    ShopServiceImpl shopRepository;
    @MockBean
    ShopServiceImpl shopMapper;
    private final Shop shop = Shop.builder()
            .id(UUID.randomUUID())
            .name("Shop1")
            .location("https://via.placeholder.com/150")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    private final Shop shop2 = Shop.builder()
            .id(UUID.randomUUID())
            .name("Shop2")
            .location("https://via.placeholder.com/150")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    String endpoint = "/api/shops";

    /**
     * Test para comprobar que se obtienen todos los Shops
     *
     * @throws Exception excepción
     */
    @Test
    void testGetAll() throws Exception {
        var shopList = List.of(shop, shop2);
        var pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        var page = new PageImpl<>(shopList);
        when(service.getAllShops(Optional.empty(), Optional.empty(), Optional.empty()
                , pageable)).thenReturn(page);
        MockHttpServletResponse response = mockMvc.perform(get(endpoint)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("\"name\":" + "\"" + shop.getName() + "\"")),
                () -> assertTrue(response.getContentAsString().contains("\"price\":" + shop.getPrice())),
                () -> assertTrue(response.getContentAsString().contains("\"image\":" + "\"" + shop.getImage() + "\"")),
                () -> assertTrue(response.getContentAsString().contains("\"name\":" + "\"" + shop2.getName() + "\"")),
                () -> assertTrue(response.getContentAsString().contains("\"price\":" + shop2.getPrice())),
                () -> assertTrue(response.getContentAsString().contains("\"image\":" + "\"" + shop2.getImage() + "\""))
        );
    }

//    /**
//     * Test para comprobar que se obtiene un Shop por su id
//     *
//     * @throws Exception excepción
//     */
//    @Test
//    void testFindById() throws Exception {
//        when(service.getShopById(shop.getId())).thenReturn(shop);
//        MockHttpServletResponse response = mockMvc.perform(
//                        get(endpoint + "/{id}", shop.getId().toString())
//                                .accept(MediaType.APPLICATION_JSON))
//                .andReturn()
//                .getResponse();
//        assertAll(
//                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
//                () -> assertTrue(response.getContentAsString().contains("\"name\":" + "\"" + shop.getName() + "\"")),
//                () -> assertTrue(response.getContentAsString().contains("\"price\":" + shop.getPrice())),
//                () -> assertTrue(response.getContentAsString().contains("\"image\":" + "\"" + shop.getImage() + "\""))
//        );
//    }
//
//    /**
//     * Test para comprobar que se obtiene un Shop por su id
//     *
//     * @throws Exception excepción
//     */
//    @Test
//    void testFindByIdNotValidID() throws Exception {
//        when(service.getShopById(null)).thenThrow(new ShopNotValidIDException(""));
//        MockHttpServletResponse response = mockMvc.perform(
//                        get(endpoint + "/{id}", "()")
//                                .accept(MediaType.APPLICATION_JSON))
//                .andReturn()
//                .getResponse();
//        assertEquals(400, response.getStatus());
//    }
//
//    /**
//     * Test para comprobar que se obtiene un Shop por su id
//     *
//     * @throws Exception excepción
//     */
//    @Test
//    void testDeleteShop() throws Exception {
//        service.deleteShop(1L);
//        mockMvc.perform(delete(endpoint + "/" + 1L))
//                .andExpect(status().isNoContent());
//    }
//
//    /**
//     * Test para comprobar ID no válida al intentar eliminar a un Shop
//     *
//     * @throws Exception excepción
//     */
//    @Test
//    void testDeleteNotValidIDPublisher() throws Exception {
//        doThrow(new PublisherIDNotValid("")).when(service).deleteShop(null);
//        MockHttpServletResponse response = mockMvc.perform(
//                        delete(endpoint + "/{id}", "()")
//                                .accept(MediaType.APPLICATION_JSON))
//                .andReturn()
//                .getResponse();
//        assertEquals(400, response.getStatus());
//    }
//
//    /**
//     * Test para comprobar ID no válida al intentar hacer un Patch de un Shop
//     *
//     * @throws Exception excepción
//     */
//    @Test
//    void testPatchNotValidIDPublisher() throws Exception {
//        doThrow(new PublisherIDNotValid("")).when(service).patchShop(null, PatchShopDTO.builder()
//                .publisherId(-234L).build());
//        MockHttpServletResponse response = mockMvc.perform(
//                        delete(endpoint + "/{id}", "()")
//                                .accept(MediaType.APPLICATION_JSON))
//                .andReturn()
//                .getResponse();
//        assertEquals(400, response.getStatus());
//    }
//
//    /**
//     * Test para comprobar ID no válida al intentar hacer un Post de un Shop
//     *
//     * @throws Exception excepción
//     */
//    @Test
//    void testPostNotValidIDPublisher() throws Exception {
//        doThrow(new PublisherIDNotValid("")).when(service).postShop(CreateShopDTO.builder().publisherId(-234L).build());
//        MockHttpServletResponse response = mockMvc.perform(
//                        delete(endpoint + "/{id}", "()")
//                                .accept(MediaType.APPLICATION_JSON))
//                .andReturn()
//                .getResponse();
//        assertEquals(400, response.getStatus());
//    }
//
//    /**
//     * Test para comprobar que se crea un Shop
//     *
//     * @throws Exception excepción
//     */
//    @Test
//    void testPostShop() throws Exception {
//        var newShop = CreateShopDTO.builder()
//                .name("nombre")
//                .publisherId(1L)
//                .price(2.2)
//                .image("imagen")
//                .category("category")
//                .description("descripción")
//                .build();
//
//        GetShopDTO createdShop = GetShopDTO.builder()
//                .id(shop.getId())
//                .name("nombre")
//                .publisher(PublisherData.builder().id(1L).build())
//                .price(2.2)
//                .image("imagen")
//                .description("descripción")
//                .category("category")
//                .createdAt(shop.getCreatedAt())
//                .updatedAt(shop.getUpdatedAt())
//                .active(shop.getActive())
//                .build();
//
//        when(service.postShop(newShop)).thenReturn(createdShop);
//        mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(newShop)))
//                .andExpect(status().isCreated());
//    }
//
//    /**
//     * Test para comprobar que se actualiza un Shop
//     *
//     * @throws Exception excepción
//     */
//    @Test
//    void testPutShop() throws Exception {
//        long id = 1L;
//
//        UpdateShopDTO updatedShop = UpdateShopDTO.builder()
//                .name("nombre")
//                .publisherId(1L)
//                .price(2.2)
//                .image("imagen")
//                .description("descripción")
//                .build();
//
//        GetShopDTO updatedShopResponse = GetShopDTO.builder()
//                .id(shop.getId())
//                .name("nombre")
//                .publisher(PublisherData.builder().id(1L).build())
//                .price(2.2)
//                .image("imagen")
//                .description("descripción")
//                .createdAt(shop.getCreatedAt())
//                .updatedAt(shop.getUpdatedAt())
//                .active(shop.getActive())
//                .build();
//
//        when(service.putShop(any(), eq(updatedShop))).thenReturn(updatedShopResponse);
//
//        mockMvc.perform(MockMvcRequestBuilders.put(endpoint + "/" + id)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(updatedShop)))
//                .andExpect(status().isOk());
//    }
//
//    /**
//     * Test para comprobar ID no válida al hacer un Put
//     *
//     * @throws Exception excepción
//     */
//    @Test
//    void testPutNotValidID() throws Exception {
//        doThrow(new ShopNotValidIDException("")).when(service).putShop(null, UpdateShopDTO.builder()
//                .name("nombre")
//                .publisherId(1L)
//                .price(2.2)
//                .image("imagen")
//                .description("descripción")
//                .build());
//        MockHttpServletResponse response = mockMvc.perform(
//                        put(endpoint + "/{id}", "a()a")
//                                .accept(MediaType.APPLICATION_JSON))
//                .andReturn()
//                .getResponse();
//        assertEquals(400, response.getStatus());
//    }
//
//    /**
//     * Test para comprobar que se actualiza parcialmente un Shop
//     *
//     * @throws Exception excepción
//     */
//    @Test
//    void testPatchShop() throws Exception {
//        long id = 1L;
//
//        PatchShopDTO patchedShop = PatchShopDTO.builder()
//                .price(14.99)
//                .build();
//
//        GetShopDTO patchedShopResponse = GetShopDTO.builder()
//                .id(shop.getId())
//                .name("nombre")
//                .publisher(PublisherData.builder().id(1L).build())
//                .price(2.2)
//                .image("imagen")
//                .description("descripción")
//                .createdAt(shop.getCreatedAt())
//                .updatedAt(shop.getUpdatedAt())
//                .active(shop.getActive())
//                .build();
//
//        when(service.patchShop(any(), eq(patchedShop))).thenReturn(patchedShopResponse);
//
//        mockMvc.perform(MockMvcRequestBuilders.patch(endpoint + "/" + id)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(patchedShop)))
//                .andExpect(status().isOk());
//    }
//
//    /**
//     * Test de ValidationException
//     *
//     * @throws Exception excepción
//     */
//    @Test
//    void testValidationExceptionHandling() throws Exception {
//        CreateShopDTO invalidShop = CreateShopDTO.builder()
//                .build();
//
//        mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(invalidShop)))
//                .andExpect(status().isBadRequest());
//    }
//
//    /**
//     * Test para comprobar que se obtienen todos los Shops
//     *
//     * @throws Exception excepción
//     */
//    @Test
//    void updateShopImage() throws Exception {
//        var myLocalEndpoint = endpoint + "/image/" + shop.getId().toString();
//
//        when(service.updateImage(anyLong(), any(MultipartFile.class), anyBoolean())).thenReturn(shop);
//
//        MockMultipartFile file = new MockMultipartFile(
//                "file",
//                "filename.jpg",
//                MediaType.IMAGE_JPEG_VALUE,
//                "contenido del archivo".getBytes()
//        );
//
//        MockHttpServletResponse response = mockMvc.perform(
//                multipart(myLocalEndpoint)
//                        .file(file)
//                        .with(req -> {
//                            req.setMethod("PATCH");
//                            return req;
//                        })
//        ).andReturn().getResponse();
//
//        var res = mapper.readValue(response.getContentAsString(), GetShopDTO.class);
//
//        assertAll(
//                () -> assertEquals(200, response.getStatus()),
//                () -> assertEquals(shop.getId(), res.getId())
//        );
//
//        verify(service, times(1)).updateImage(anyLong(), any(MultipartFile.class), anyBoolean());
//    }
//
//    /**
//     * Test para comprobar cuando no se le asigna una imagen cuando se intenta actualizar la imagen de un Shop
//     *
//     * @throws Exception excepción
//     */
//    @Test
//    void testUpdateShopImageNoImageProvided() throws Exception {
//        var myLocalEndpoint = endpoint + "/image/" + shop.getId().toString();
//        when(service.updateImage(anyLong(), any(MultipartFile.class), anyBoolean()))
//                .thenReturn(shop); // Esto no se va a invocar debido a la excepción
//
//        MockHttpServletResponse response = mockMvc.perform(
//                multipart(myLocalEndpoint)
//                        .file(new MockMultipartFile("file", "", "text/plain", new byte[0]))
//                        .with(requestPostProcessor -> {
//                            requestPostProcessor.setMethod("PATCH");
//                            return requestPostProcessor;
//                        })
//        ).andReturn().getResponse();
//
//        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
//        assertTrue(response.getContentAsString().contains("No se ha enviado una imagen para el Shop"));
//
//        verify(service, never()).updateImage(anyLong(), any(MultipartFile.class), anyBoolean());
//    }

}
