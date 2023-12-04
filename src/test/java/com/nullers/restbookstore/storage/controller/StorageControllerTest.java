package com.nullers.restbookstore.storage.controller;

import com.nullers.restbookstore.storage.services.FileSystemStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest(properties = "spring.profiles.active=test")
class StorageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileSystemStorageService storageService;

    @Test
    void testServeFileNotContentType() throws Exception {
        byte[] content = "Contenido de prueba".getBytes();
        new MockMultipartFile("file", "test.txt", null, content);
        when(storageService.loadAsResource(anyString())).thenReturn(new ByteArrayResource(content));
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServletPath("/storage/test.txt");
        request.setMethod("GET");
        mockMvc.perform(get("/storage/test.txt"))
                .andExpect(status().isBadRequest());
    }
}