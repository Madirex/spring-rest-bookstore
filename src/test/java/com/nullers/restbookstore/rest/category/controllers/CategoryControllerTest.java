package com.nullers.restbookstore.rest.category.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nullers.restbookstore.pagination.models.ErrorResponse;
import com.nullers.restbookstore.pagination.models.PageResponse;
import com.nullers.restbookstore.rest.category.dto.CategoryCreateDTO;
import com.nullers.restbookstore.rest.category.exceptions.CategoryConflictException;
import com.nullers.restbookstore.rest.category.exceptions.CategoryNotFoundException;
import com.nullers.restbookstore.rest.category.model.Category;
import com.nullers.restbookstore.rest.category.services.CategoryServiceJpaImpl;
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
@WithMockUser(username = "admin", password = "admin", roles =  {"ADMIN", "USER"})

class CategoryControllerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    CategoryServiceJpaImpl service;

    @Autowired
    MockMvc mockMvc;

    Category category1 = Category.builder()
            .id(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))
            .name("category 1")
            .isActive(true)
            .build();

    Category category2 = Category.builder()
            .id(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))
            .name("category 2")
            .isActive(false)
            .build();

    Category category3 = Category.builder()
            .id(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9634"))
            .name("category 3")
            .isActive(true)
            .build();

    CategoryCreateDTO categoryCreateDTO = CategoryCreateDTO.builder()
            .name("category 1")
            .isActive(true)
            .build();

    String endPoint = "/api/categories";


    @BeforeEach
    void setUp() {
        categoryCreateDTO = CategoryCreateDTO.builder()
                .name("category 1")
                .build();
    }

    @Autowired
    public CategoryControllerTest(CategoryServiceJpaImpl service) {
        this.service = service;
    }

    @Test
    @WithAnonymousUser
    void getAllCategories_WithAnonymousUser() throws Exception {
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
    void getCategory_WithAnonymousUser() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus())
        );

        verify(service, times(0)).getCategoryById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));
    }

    @Test
    @WithAnonymousUser
    void addCategory_WithAnonymousUser() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post(endPoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryCreateDTO)))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus())
        );

        verify(service, times(0)).createCategory(categoryCreateDTO);
    }

    @Test
    @WithAnonymousUser
    void updateCategory_WithAnonymousUser() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(put(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryCreateDTO)))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus())
        );

        verify(service, times(0)).updateCategory(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"), categoryCreateDTO);
    }

    @Test
    @WithAnonymousUser
    void deleteCategory_WithAnonymousUser() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(delete(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus())
        );

        verify(service, times(0)).deleteById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));
    }


    @Test
    void getAllCategories_WithoutParams() throws Exception {
        List<Category> categories = List.of(category1, category2);
        when(service.getAll(any(Optional.class), any(Optional.class), any(Pageable.class))).thenReturn(new PageImpl(categories));

        MockHttpServletResponse response = mockMvc.perform(get(endPoint)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        PageResponse<Category> pageResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructParametricType(PageResponse.class, Category.class));

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
    void getAllCategories_ShouldReturnCategories_withAllParams() throws Exception {
        List<Category> categories = List.of(category1);
        when(service.getAll(any(Optional.class), any(Optional.class), any(Pageable.class))).thenReturn(new PageImpl(categories));

        MockHttpServletResponse response = mockMvc.perform(get(endPoint)
                        .param("name", "categoría 1")
                        .param("isActive", "true")
                        .param("page", "0")
                        .param("size", "10")
                        .param("orderBy", "name")
                        .param("order", "asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        PageResponse<Category> pageResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructParametricType(PageResponse.class, Category.class));

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
    void getAllCategories_ShouldReturnCategory_withNameParam() throws Exception {
        List<Category> categories = List.of(category1);
        when(service.getAll(any(Optional.class), any(Optional.class), any(Pageable.class))).thenReturn(new PageImpl(categories));

        MockHttpServletResponse response = mockMvc.perform(get(endPoint)
                        .param("nombre", "category 1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        PageResponse<Category> pageResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructParametricType(PageResponse.class, Category.class));

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
    void getAllCategories_ShouldReturnEmpty_withNameButIsInactive() throws Exception {
        List<Category> categories = List.of();
        when(service.getAll(any(Optional.class), any(Optional.class), any(Pageable.class))).thenReturn(new PageImpl(categories));

        MockHttpServletResponse response = mockMvc.perform(get(endPoint)
                        .param("nombre", "category 2")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        PageResponse<Category> pageResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructParametricType(PageResponse.class, Category.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(0, pageResponse.totalElements()),
                () -> assertEquals(1, pageResponse.totalPages()),
                () -> assertEquals(0, pageResponse.content().size())
        );

        verify(service, times(1)).getAll(any(Optional.class), any(Optional.class), any(Pageable.class));
    }

    @Test
    void getAllCategories_ShouldReturnCategory_withSorted() throws Exception {
        List<Category> categories = List.of(category3, category1);
        when(service.getAll(any(Optional.class), any(Optional.class), any(Pageable.class))).thenReturn(new PageImpl(categories));

        MockHttpServletResponse response = mockMvc.perform(get(endPoint)
                        .param("orderBy", "name")
                        .param("order", "desc")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        PageResponse<Category> pageResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructParametricType(PageResponse.class, Category.class));

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
    void getAllCategories_ShouldReturnError_withPageInvalid() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(endPoint)
                        .param("page", "-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8),
                mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), res.get("code")),
                () -> assertEquals("{page=La página no puede ser inferior a 0}", res.get("errors").toString())
        );

        verify(service, times(0)).getAll(any(Optional.class), any(Optional.class), any(Pageable.class));
    }

    @Test
    void getAllCategories_ShouldReturnError_withSizeInvalid() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get(endPoint)
                        .param("size", "-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8),
                mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), res.get("code")),
                () -> assertEquals("{size=El tamaño de la página no puede ser inferior a 1}", res.get("errors").toString())
        );

        verify(service, times(0)).getAll(any(Optional.class), any(Optional.class), any(Pageable.class));
    }

    @Test
    void getAllCategories_shouldReturnCategories_withNameAndActive() throws Exception {
        List<Category> categories = List.of(category1);
        when(service.getAll(any(Optional.class), any(Optional.class), any(Pageable.class))).thenReturn(new PageImpl(categories));

        MockHttpServletResponse response = mockMvc.perform(get(endPoint)
                        .param("name", "categoría 1")
                        .param("isActive", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        PageResponse<Category> pageResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructParametricType(PageResponse.class, Category.class));

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
    void getAllCategories_ShouldReturnError_withSortParamIsInvalid() throws Exception {
        when(service.getAll(any(Optional.class), any(Optional.class), any(Pageable.class))).thenThrow(new IllegalArgumentException("Error al procesar la propiedad en la consulta: pepe"));

        MockHttpServletResponse response = mockMvc.perform(get(endPoint)
                        .param("orderBy", "pepe")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus()),
                () -> assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.status()),
                () -> assertEquals("Error al procesar la propiedad en la consulta: pepe", errorResponse.error())
        );

        verify(service, times(1)).getAll(any(Optional.class), any(Optional.class), any(Pageable.class));
    }

    @Test
    void getCategory() throws Exception {
        when(service.getCategoryById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))).thenReturn(category1);

        MockHttpServletResponse response = mockMvc.perform(get(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        Category category = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), Category.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(category1.getId(), category.getId()),
                () -> assertEquals(category1.getName(), category.getName())
        );

        verify(service, times(1)).getCategoryById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));
    }

    @Test
    void getCategoryNotFound() throws Exception {
        when(service.getCategoryById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))).thenThrow(new CategoryNotFoundException(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")));

        MockHttpServletResponse response = mockMvc.perform(get(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734").accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);
        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Category con id 3930e05a-7ebf-4aa1-8aa8-5d7466fa9734 no encontrada", errorResponse.error())
        );

        verify(service, times(1)).getCategoryById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));
    }

    @Test
    void addCategory() throws Exception {
        when(service.createCategory(categoryCreateDTO)).thenReturn(category1);

        MockHttpServletResponse response = mockMvc.perform(post(endPoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryCreateDTO)))
                .andReturn().getResponse();

        Category category = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), Category.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(category1.getId(), category.getId()),
                () -> assertEquals(category1.getName(), category.getName())
        );

        verify(service, times(1)).createCategory(categoryCreateDTO);
    }

    @Test
    void addCategoryWithoutName() throws Exception {
        categoryCreateDTO.setName(null);

        MockHttpServletResponse response = mockMvc.perform(post(endPoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryCreateDTO)))
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

        verify(service, times(0)).createCategory(categoryCreateDTO);
    }

    @Test
    void addCategoryAlreadyExistSameName() throws Exception {
        when(service.createCategory(categoryCreateDTO)).thenThrow(new CategoryConflictException("Ya existe una category con el nombre: " + categoryCreateDTO.getName()));

        MockHttpServletResponse response = mockMvc.perform(post(endPoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryCreateDTO)))
                .andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);
        assertAll(
                () -> assertEquals(HttpStatus.CONFLICT.value(), response.getStatus()),
                () -> assertEquals("Ya existe una category con el nombre: " + categoryCreateDTO.getName(), errorResponse.error())
        );

        verify(service, times(1)).createCategory(categoryCreateDTO);
    }

    @Test
    void updateCategory() throws Exception {
        when(service.updateCategory(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"), categoryCreateDTO)).thenReturn(category1);

        MockHttpServletResponse response = mockMvc.perform(put(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryCreateDTO)))
                .andReturn().getResponse();

        Category category = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), Category.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(category1.getId(), category.getId()),
                () -> assertEquals(category1.getName(), category.getName())
        );

        verify(service, times(1)).updateCategory(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"), categoryCreateDTO);
    }

    @Test
    void updateWithoutName() throws Exception {
        categoryCreateDTO.setName(null);

        MockHttpServletResponse response = mockMvc.perform(put(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryCreateDTO)))
                .andReturn().getResponse();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> res = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));
        LinkedHashMap<String, Object> errors = (LinkedHashMap<String, Object>) res.get("errors");
    }

    @Test
    void updateCategoryAlreadyExistSameName() throws Exception {
        when(service.updateCategory(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"), categoryCreateDTO)).thenThrow(new CategoryConflictException("Ya existe una category con el nombre: " + categoryCreateDTO.getName()));

        MockHttpServletResponse response = mockMvc.perform(put(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryCreateDTO)))
                .andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);
        assertAll(
                () -> assertEquals(HttpStatus.CONFLICT.value(), response.getStatus()),
                () -> assertEquals("Ya existe una category con el nombre: " + categoryCreateDTO.getName(), errorResponse.error())
        );

        verify(service, times(1)).updateCategory(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"), categoryCreateDTO);
    }

    @Test
    void updateCategoryNotFound() throws Exception {
        when(service.updateCategory(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"), categoryCreateDTO)).thenThrow(new CategoryNotFoundException(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")));

        MockHttpServletResponse response = mockMvc.perform(put(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(categoryCreateDTO)))
                .andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);
        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Category con id 3930e05a-7ebf-4aa1-8aa8-5d7466fa9734 no encontrada", errorResponse.error())
        );

        verify(service, times(1)).updateCategory(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"), categoryCreateDTO);
    }

    @Test
    void deleteCategory() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(delete(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus())
        );

        verify(service, times(1)).deleteById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));
    }

    @Test
    void deleteCategoryNotFound() throws Exception {
        doThrow(new CategoryNotFoundException(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"))).when(service).deleteById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));

        MockHttpServletResponse response = mockMvc.perform(delete(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);
        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus()),
                () -> assertEquals("Category con id 3930e05a-7ebf-4aa1-8aa8-5d7466fa9734 no encontrada", errorResponse.error())
        );

        verify(service, times(1)).deleteById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));
    }

    @Test
    void deleteCategoryConflict() throws Exception {
        doThrow(new CategoryConflictException("No se puede eliminar la category porque tiene libros asociados"))
                .when(service).deleteById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));

        MockHttpServletResponse response = mockMvc.perform(delete(endPoint + "/3930e05a-7ebf-4aa1-8aa8-5d7466fa9734")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        ErrorResponse errorResponse = mapper.readValue(response.getContentAsString(StandardCharsets.UTF_8), ErrorResponse.class);
        assertAll(
                () -> assertEquals(HttpStatus.CONFLICT.value(), response.getStatus()),
                () -> assertEquals("No se puede eliminar la category porque tiene libros asociados", errorResponse.error())
        );

        verify(service, times(1)).deleteById(UUID.fromString("3930e05a-7ebf-4aa1-8aa8-5d7466fa9734"));
    }
}