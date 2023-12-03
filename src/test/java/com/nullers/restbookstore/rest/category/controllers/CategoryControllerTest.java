package com.nullers.restbookstore.rest.category.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nullers.restbookstore.pagination.models.ErrorResponse;
import com.nullers.restbookstore.pagination.models.PageResponse;
import com.nullers.restbookstore.rest.category.dto.CategoriaCreateDto;
import com.nullers.restbookstore.rest.category.exceptions.CategoriaConflictException;
import com.nullers.restbookstore.rest.category.exceptions.CategoriaNotFoundException;
import com.nullers.restbookstore.rest.category.model.Category;
import com.nullers.restbookstore.rest.category.services.CategoriaServiceJpaImpl;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest(properties = "spring.config.name=application-test")
@WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
class CategoryControllerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    CategoriaServiceJpaImpl service;

    @Autowired
    MockMvc mockMvc;

    Category category1 = Category.builder()
            .id(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))
            .name("categoria 1")
            .isActive(true)
            .build();

    Category category2 = Category.builder()
            .id(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))
            .name("categoria 2")
            .isActive(false)
            .build();

    Category category3 = Category.builder()
            .id(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9634"))
            .name("categoria 3")
            .isActive(true)
            .build();

    CategoriaCreateDto categoriaCreateDto = CategoriaCreateDto.builder()
            .name("categoria 1")
            .isActive(true)
            .build();

    String endPoint = "/api/categories";


    @BeforeEach
    void setUp() {
        categoriaCreateDto = CategoriaCreateDto.builder()
                .name("categoria 1")
                .build();
    }

    @Autowired
    public CategoryControllerTest(CategoriaServiceJpaImpl service) {
        this.service = service;
    }

    @Test
    @WithAnonymousUser
    void getAllCategorias_WithAnonymousUser() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(endPoint)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus())
        );

        verify(service, times(0)).getAll(any(Optional.class), any(Optional.class), any(Pageable.class));
    }

    @Test
    @WithAnonymousUser
    void getCategoria_WithAnonymousUser() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus())
        );

        verify(service, times(0)).getCategoriaById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));
    }

    @Test
    @WithAnonymousUser
    void addCategoria_WithAnonymousUser() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post(endPoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoriaCreateDto)))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus())
        );

        verify(service, times(0)).createCategoria(categoriaCreateDto);
    }

    @Test
    @WithAnonymousUser
    void updateCategoria_WithAnonymousUser() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(put(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoriaCreateDto)))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus())
        );

        verify(service, times(0)).updateCategoria(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"), categoriaCreateDto);
    }

    @Test
    @WithAnonymousUser
    void deleteCategoria_WithAnonymousUser() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(delete(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus())
        );

        verify(service, times(0)).deleteById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));
    }


    @Test
    void getAllCategorias_WithoutParams() throws Exception {
        List<Category> categorys = List.of(category1, category2);
        when(service.getAll(any(Optional.class), any(Optional.class), any(Pageable.class))).thenReturn(new PageImpl(categorys));

        MockHttpServletResponse response = mockMvc.perform(get(endPoint)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        PageResponse<Category> pageResponse = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructParametricType(PageResponse.class, Category.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(2, pageResponse.totalElements()),
                () -> assertEquals(1, pageResponse.totalPages()),
                () -> assertEquals(2, pageResponse.content().size()),
                () -> assertEquals(category1.getId(), pageResponse.content().get(0).getId()),
                () -> assertEquals(category1.getName(), pageResponse.content().get(0).getName()),
                () -> assertEquals(category2.getId(), pageResponse.content().get(1).getId()),
                () -> assertEquals(category2.getName(), pageResponse.content().get(1).getName())
        );

        verify(service, times(1)).getAll(any(Optional.class), any(Optional.class), any(Pageable.class));
    }

    @Test
    void getAllCategorias_ShouldReturnCategorias_withAllParams() throws Exception {
        List<Category> categorys = List.of(category1);
        when(service.getAll(any(Optional.class), any(Optional.class), any(Pageable.class))).thenReturn(new PageImpl(categorys));

        MockHttpServletResponse response = mockMvc.perform(get(endPoint)
                        .param("nombre", "categoría 1")
                        .param("activa", "true")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "name")
                        .param("order", "asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        PageResponse<Category> pageResponse = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructParametricType(PageResponse.class, Category.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(1, pageResponse.totalElements()),
                () -> assertEquals(1, pageResponse.totalPages()),
                () -> assertEquals(1, pageResponse.content().size()),
                () -> assertEquals(category1.getId(), pageResponse.content().get(0).getId()),
                () -> assertEquals(category1.getName(), pageResponse.content().get(0).getName())
        );

        verify(service, times(1)).getAll(any(Optional.class), any(Optional.class), any(Pageable.class));
    }

    @Test
    void getAllCategorias_ShouldReturnCategoria_withNameParam() throws Exception {
        List<Category> categorys = List.of(category1);
        when(service.getAll(any(Optional.class), any(Optional.class), any(Pageable.class))).thenReturn(new PageImpl(categorys));

        MockHttpServletResponse response = mockMvc.perform(get(endPoint)
                        .param("nombre", "categoria 1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        PageResponse<Category> pageResponse = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructParametricType(PageResponse.class, Category.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(1, pageResponse.totalElements()),
                () -> assertEquals(1, pageResponse.totalPages()),
                () -> assertEquals(1, pageResponse.content().size()),
                () -> assertEquals(category1.getId(), pageResponse.content().get(0).getId()),
                () -> assertEquals(category1.getName(), pageResponse.content().get(0).getName())
        );

        verify(service, times(1)).getAll(any(Optional.class), any(Optional.class), any(Pageable.class));
    }

    @Test
    void getAllCategorias_ShouldReturnEmpty_withNameButIsInactive() throws Exception {
        List<Category> categorys = List.of();
        when(service.getAll(any(Optional.class), any(Optional.class), any(Pageable.class))).thenReturn(new PageImpl(categorys));

        MockHttpServletResponse response = mockMvc.perform(get(endPoint)
                        .param("nombre", "categoria 2")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        PageResponse<Category> pageResponse = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructParametricType(PageResponse.class, Category.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(0, pageResponse.totalElements()),
                () -> assertEquals(1, pageResponse.totalPages()),
                () -> assertEquals(0, pageResponse.content().size())
        );

        verify(service, times(1)).getAll(any(Optional.class), any(Optional.class), any(Pageable.class));
    }

    @Test
    void getAllCategorias_ShouldReturnCategoria_withSorted() throws Exception {
        List<Category> categorys = List.of(category3, category1);
        when(service.getAll(any(Optional.class), any(Optional.class), any(Pageable.class))).thenReturn(new PageImpl(categorys));

        MockHttpServletResponse response = mockMvc.perform(get(endPoint)
                        .param("sortBy", "name")
                        .param("order", "desc")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        PageResponse<Category> pageResponse = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructParametricType(PageResponse.class, Category.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(2, pageResponse.totalElements()),
                () -> assertEquals(1, pageResponse.totalPages()),
                () -> assertEquals(2, pageResponse.content().size()),
                () -> assertEquals(category3.getId(), pageResponse.content().get(0).getId()),
                () -> assertEquals(category3.getName(), pageResponse.content().get(0).getName()),
                () -> assertEquals(category1.getId(), pageResponse.content().get(1).getId()),
                () -> assertEquals(category1.getName(), pageResponse.content().get(1).getName())

        );

        verify(service, times(1)).getAll(any(Optional.class), any(Optional.class), any(Pageable.class));
    }

    @Test
    void getAllCategorias_ShouldReturnError_withPageInvalid() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(endPoint)
                        .param("page", "-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.status()),
                () -> assertEquals("Page index must not be less than zero", errorResponse.msg())
        );

        verify(service, times(0)).getAll(any(Optional.class), any(Optional.class), any(Pageable.class));
    }

    @Test
    void getAllCategorias_ShouldReturnError_withSizeInvalid() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(endPoint)
                        .param("size", "-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.status()),
                () -> assertEquals("Page size must not be less than one", errorResponse.msg())
        );

        verify(service, times(0)).getAll(any(Optional.class), any(Optional.class), any(Pageable.class));
    }

    @Test
    void getAllCategorias_shouldReturnCategorias_withNameAndActive() throws Exception {
        List<Category> categorys = List.of(category1);
        when(service.getAll(any(Optional.class), any(Optional.class), any(Pageable.class))).thenReturn(new PageImpl(categorys));

        MockHttpServletResponse response = mockMvc.perform(get(endPoint)
                        .param("nombre", "categoría 1")
                        .param("activa", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        PageResponse<Category> pageResponse = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructParametricType(PageResponse.class, Category.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(1, pageResponse.totalElements()),
                () -> assertEquals(1, pageResponse.totalPages()),
                () -> assertEquals(1, pageResponse.content().size()),
                () -> assertEquals(category1.getId(), pageResponse.content().get(0).getId()),
                () -> assertEquals(category1.getName(), pageResponse.content().get(0).getName())
        );

        verify(service, times(1)).getAll(any(Optional.class), any(Optional.class), any(Pageable.class));
    }

    @Test
    void getAllCategorias_ShouldReturnError_withSortParamIsInvalid() throws Exception {
        when(service.getAll(any(Optional.class), any(Optional.class), any(Pageable.class))).thenThrow(new IllegalArgumentException("Error al procesar la propiedad en la consulta: pepe"));

        MockHttpServletResponse response = mockMvc.perform(get(endPoint)
                        .param("sortBy", "pepe")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.status()),
                () -> assertEquals("Error al procesar la propiedad en la consulta: pepe", errorResponse.msg())
        );

        verify(service, times(1)).getAll(any(Optional.class), any(Optional.class), any(Pageable.class));
    }

    @Test
    void getCategoria() throws Exception {
        when(service.getCategoriaById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))).thenReturn(category1);

        MockHttpServletResponse response = mockMvc.perform(get(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Category category = mapper.readValue(response.getContentAsString(), Category.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(category1.getId(), category.getId()),
                () -> assertEquals(category1.getName(), category.getName())
        );

        verify(service, times(1)).getCategoriaById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));
    }

    @Test
    void getCategorieNotFound() throws Exception {
        when(service.getCategoriaById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))).thenThrow(new CategoriaNotFoundException(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")));

        MockHttpServletResponse response = mockMvc.perform(get(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Categoria con id 3930e05a-7ebf-4aa1-8aa8-5d7466fa9734 no encontrada", errorResponse.msg())
        );

        verify(service, times(1)).getCategoriaById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));
    }

    @Test
    void addCategoria() throws Exception {
        when(service.createCategoria(categoriaCreateDto)).thenReturn(category1);

        MockHttpServletResponse response = mockMvc.perform(post(endPoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoriaCreateDto)))
                .andReturn().getResponse();

        Category category = mapper.readValue(response.getContentAsString(), Category.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(category1.getId(), category.getId()),
                () -> assertEquals(category1.getName(), category.getName())
        );

        verify(service, times(1)).createCategoria(categoriaCreateDto);
    }

    @Test
    void addCategoryWithoutName() throws Exception {
        categoriaCreateDto.setName(null);

        MockHttpServletResponse response = mockMvc.perform(post(endPoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoriaCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8),
                mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), res.get("code")),
                () -> assertEquals("El nombre no puede estar vacío", errors.get("name"))
        );

        verify(service, times(0)).createCategoria(categoriaCreateDto);
    }

    @Test
    void addCategoriaAlreadyExistSameName() throws Exception {
        when(service.createCategoria(categoriaCreateDto)).thenThrow(new CategoriaConflictException("Ya existe una categoria con el nombre: " + categoriaCreateDto.getName()));

        MockHttpServletResponse response = mockMvc.perform(post(endPoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoriaCreateDto)))
                .andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertAll(
                () -> assertEquals(HttpStatus.CONFLICT.value(), response.getStatus()),
                () -> assertEquals("Ya existe una categoria con el nombre: " + categoriaCreateDto.getName(), errorResponse.msg())
        );

        verify(service, times(1)).createCategoria(categoriaCreateDto);
    }

    @Test
    void updateCategoria() throws Exception {
        when(service.updateCategoria(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"), categoriaCreateDto)).thenReturn(category1);

        MockHttpServletResponse response = mockMvc.perform(put(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoriaCreateDto)))
                .andReturn().getResponse();

        Category category = mapper.readValue(response.getContentAsString(), Category.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(category1.getId(), category.getId()),
                () -> assertEquals(category1.getName(), category.getName())
        );

        verify(service, times(1)).updateCategoria(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"), categoriaCreateDto);
    }

    @Test
    void updateWithoutName() throws Exception {
        categoriaCreateDto.setName(null);

        MockHttpServletResponse response = mockMvc.perform(put(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoriaCreateDto)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> res = mapper.readValue(response.getContentAsString(), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");
    }

    @Test
    void updateCategoriaAlreadyExistSameName() throws Exception {
        when(service.updateCategoria(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"), categoriaCreateDto)).thenThrow(new CategoriaConflictException("Ya existe una categoria con el nombre: " + categoriaCreateDto.getName()));

        MockHttpServletResponse response = mockMvc.perform(put(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoriaCreateDto)))
                .andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertAll(
                () -> assertEquals(HttpStatus.CONFLICT.value(), response.getStatus()),
                () -> assertEquals("Ya existe una categoria con el nombre: " + categoriaCreateDto.getName(), errorResponse.msg())
        );

        verify(service, times(1)).updateCategoria(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"), categoriaCreateDto);
    }

    @Test
    void updateCategoriaNotFound() throws Exception {
        when(service.updateCategoria(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"), categoriaCreateDto)).thenThrow(new CategoriaNotFoundException(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")));

        MockHttpServletResponse response = mockMvc.perform(put(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoriaCreateDto)))
                .andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Categoria con id 3930e05a-7ebf-4aa1-8aa8-5d7466fa9734 no encontrada", errorResponse.msg())
        );

        verify(service, times(1)).updateCategoria(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"), categoriaCreateDto);
    }

    @Test
    void deleteCategoria() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(delete(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus())
        );

        verify(service, times(1)).deleteById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));
    }

    @Test
    void deleteCategoriaNotFound() throws Exception {
        doThrow(new CategoriaNotFoundException(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))).when(service).deleteById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));

        MockHttpServletResponse response = mockMvc.perform(delete(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Categoria con id 3930e05a-7ebf-4aa1-8aa8-5d7466fa9734 no encontrada", errorResponse.msg())
        );

        verify(service, times(1)).deleteById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));
    }

    @Test
    void deleteCategoriaBadRequest() throws Exception {
        doThrow(new CategoriaConflictException("No se puede eliminar la categoria porque tiene libros asociados")).when(service).deleteById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));

        MockHttpServletResponse response = mockMvc.perform(delete(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertAll(
                () -> assertEquals(HttpStatus.CONFLICT.value(), response.getStatus()),
                () -> assertEquals("No se puede eliminar la categoria porque tiene libros asociados", errorResponse.msg())
        );

        verify(service, times(1)).deleteById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));
    }


}
