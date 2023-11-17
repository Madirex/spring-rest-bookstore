package com.nullers.restbookstore.storage.service;

import com.nullers.restbookstore.storage.exceptions.StorageBadRequest;
import com.nullers.restbookstore.storage.exceptions.StorageInternal;
import com.nullers.restbookstore.storage.exceptions.StorageNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FileSystemStorageServiceTest {

    @Mock
    private Path rootLocation;

    @InjectMocks
    private final FileSystemStorageService fileSystemStorageService = new FileSystemStorageService("books-images");

    byte[] bytesPNG = { (byte) 137, (byte) 80, (byte) 78, (byte) 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82,
            0, 0, 0, 1, 0, 0, 0, 1, 8, 6, 0, 0, 0, 31, 21, (byte) -60, (byte) -60,
            (byte) 137, (byte) 80, (byte) 78, (byte) 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82,
            0, 0, 0, 1, 0, 0, 0, 1, 8, 6, 0, 0, 0, 31, 21, (byte) -60, (byte) -60 };

    byte[] bytesGIF = {(byte) 71, (byte) 73, (byte) 70, (byte) 56, (byte) 57, (byte) 97, 0, 0, (byte) 192, 0, 0,
            (byte) 255, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 33, (byte) 249, 4, 1, 0, 0, 0, 0};

    byte[] bytesJPEG = {(byte) 255, (byte) 216, (byte) 255, (byte) 224, 0, 16, 74, 70, 73, 70, 0, 1, 1, 0, 96, 0, 0,
            (byte) 255, (byte) 219, 0, (byte) 67, 0, 8, 6, 6, 7, 6, 5, 8, 7, 7, 7, 9, 9};

    @BeforeEach
    public void setUp() throws IOException {
        fileSystemStorageService.deleteAll();
        fileSystemStorageService.init();
        fileSystemStorageService.store(new MockMultipartFile("book", "book.png",
                "image/png", bytesPNG), List.of("jpg", "jpeg", "png"), UUID.randomUUID().toString());
        fileSystemStorageService.store(new MockMultipartFile("book", "book2.png",
                "image/png", bytesPNG), List.of("jpg", "jpeg", "png"), UUID.randomUUID().toString());
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletRequestAttributes attrs = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attrs);
    }

    /**
     * Test para probar que el método Store funciona correctamente (PNG)
     *
     * @throws IOException excepción entrada/salida
     */
    @Test
    void testStorePng() throws IOException {
        var id = UUID.randomUUID().toString();
        String file = fileSystemStorageService.store(new MockMultipartFile("funko", "funko.png",
                "image/png", bytesPNG), List.of("jpg", "jpeg", "png"), id);

        assertAll(
                () -> assertNotNull(file),
                () -> assertTrue(file.contains(id + ".png"))
        );
    }

    /**
     * Test para probar que el método Store funciona correctamente (JPG)
     *
     * @throws IOException excepción entrada/salida
     */
    @Test
    void testStoreJpg() throws IOException {
        var id = UUID.randomUUID().toString();
        String file = fileSystemStorageService.store(new MockMultipartFile("funko", "funko.jpg",
                "image/jpg", bytesJPEG), List.of("jpg", "jpeg", "png"), id);

        assertAll(
                () -> assertNotNull(file),
                () -> assertTrue(file.contains(id + ".jpg"))
        );
    }

    /**
     * Test para probar que el método Store funciona correctamente (Jpeg)
     *
     * @throws IOException excepción entrada/salida
     */
    @Test
    void testStoreJpeg() throws IOException {
        var id = UUID.randomUUID().toString();
        String file = fileSystemStorageService.store(new MockMultipartFile("funko", "funko.jpeg",
                "image/jpeg", bytesPNG), List.of("jpg", "jpeg", "png"), id);

        assertAll(
                () -> assertNotNull(file),
                () -> assertTrue(file.contains(id + ".jpeg"))
        );
    }

    /**
     * Test para probar que no se permite insertar (Gif)
     */
    @Test
    void testStoreGifNotAllowed() {
        var id = UUID.randomUUID().toString();
        var list = List.of("jpg", "jpeg", "png");
        var multiPart = new MockMultipartFile("funko", "funko.gif",
                "image/gif", bytesGIF);
        assertThrows(StorageBadRequest.class, () -> fileSystemStorageService.store(multiPart, list, id));
    }

    /**
     * Test para probar que no se permite insertar (Gif)
     */
    @Test
    void testStoreGifNotAllowedFormat() {
        var id = UUID.randomUUID().toString();
        var list = List.of("jpg", "jpeg", "png", "gif");
        var multiPart = new MockMultipartFile("funko", "funko.gif", "image/gif", bytesGIF);
        assertThrows(StorageBadRequest.class, () -> fileSystemStorageService.store(multiPart, list, id));
    }

    @Test
    void testStoreWithEmptyFile() {
        var res = assertThrows(StorageBadRequest.class, () -> fileSystemStorageService.store(
                new MockMultipartFile("book", "book.png",
                        "", "".getBytes()),
                List.of("jpg", "jpeg", "png"), UUID.randomUUID().toString()));
        assertEquals("Fichero vacío book.png", res.getMessage());
    }


    @Test
    void testStoreWith2puntos() {
        var res = assertThrows(StorageBadRequest.class, () -> fileSystemStorageService.store(
                new MockMultipartFile("book", "../book.png",
                        "image/png", bytesPNG),
                List.of("jpg", "jpeg", "png"), UUID.randomUUID().toString()));
        assertEquals("No se puede almacenar un fichero con una ruta relativa fuera del " +
                "directorio actual ../book.png", res.getMessage());
    }

    @Test
    void testLoadAll() {
        var res = fileSystemStorageService.loadAll();
        assertAll(
                () -> assertNotNull(res),
                () -> assertFalse(res.toList().isEmpty())
        );
    }

    @Test
    void testLoad() {
        var res = fileSystemStorageService.load("book.png");
        assertAll(
                () -> assertNotNull(res),
                () -> assertTrue(res.getFileName().toString().contains("book.png"))
        );
    }

    @Test
    void testLoadAsResource() throws IOException {
        var id = UUID.randomUUID().toString();
        var file = fileSystemStorageService.store(
                new MockMultipartFile("book", "book.png",
                        "image/png", bytesPNG),
                List.of("jpg", "jpeg", "png"), id);
        var res = fileSystemStorageService.loadAsResource(file);
        assertAll(
                () -> assertNotNull(res),
                () -> assertTrue(Objects.requireNonNull(res.getFilename()).contains(id + ".png"))
        );
    }

    @Test
    void testLoadAsResoureNotFound() {
        var res = assertThrows(StorageNotFound.class, () -> fileSystemStorageService.loadAsResource("book.png"));
        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("No se puede leer el fichero: book.png", res.getMessage())
        );
    }

    @Test
    void testLoadAsResoureMalformedUrl() {
        var res = assertThrows(StorageNotFound.class, () -> fileSystemStorageService.loadAsResource("book.png"));

        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals("No se puede leer el fichero: book.png", res.getMessage())
        );
    }

    @Test
    void testDeleteAll() throws IOException {
        fileSystemStorageService.deleteAll();
        fileSystemStorageService.init();
        var res = fileSystemStorageService.loadAll();

        assertAll(
                () -> assertTrue(res.toList().isEmpty())
        );
    }

    @Test
    void testDeleteAllInternalError() {
        fileSystemStorageService.deleteAll();
        var res = assertThrows(StorageInternal.class, fileSystemStorageService::loadAll);
        assertAll(
                () -> assertNotNull(res),
                () -> assertTrue(res.getMessage().contains("Fallo al leer ficheros almacenados "))
        );
    }

    @Test
    void testInit() throws IOException {
        fileSystemStorageService.deleteAll();
        fileSystemStorageService.init();
        var res = fileSystemStorageService.loadAll();
        assertAll(
                () -> assertNotNull(res),
                () -> assertTrue(res.toList().isEmpty())
        );
    }

    @Test
    void testDelete() throws IOException {
        var file = fileSystemStorageService.store(
                new MockMultipartFile("book", "book.png",
                        "image/png", bytesPNG),
                List.of("jpg", "jpeg", "png"), UUID.randomUUID().toString());
        fileSystemStorageService.delete(file);
        var res = fileSystemStorageService.loadAll().toList();
        assertAll(
                () -> assertNotNull(res),
                () -> assertEquals(2, res.size())
        );
    }

    @Test
    void testGetUrl() {
        String filename = "testFile.txt";
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String expectedUrl = baseUrl + "/storage/" + filename;
        String actualUrl = fileSystemStorageService.getUrl(filename);
        assertEquals(expectedUrl, actualUrl);
    }

}
