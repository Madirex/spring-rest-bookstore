package com.nullers.restbookstore.storage.services;

import com.nullers.restbookstore.storage.controller.StorageController;
import com.nullers.restbookstore.storage.exceptions.StorageBadRequest;
import com.nullers.restbookstore.storage.exceptions.StorageInternal;
import com.nullers.restbookstore.storage.exceptions.StorageNotFound;
import com.nullers.restbookstore.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Storage service
 */
@Service
@Slf4j
public class FileSystemStorageService implements StorageService {
    private final Path rootLocation;

    /**
     * Constructor
     *
     * @param path Path
     */
    public FileSystemStorageService(@Value("${upload.root-location}") String path) {
        this.rootLocation = Paths.get(path);
    }

    /**
     * Store file
     *
     * @param file      File
     * @param fileTypes File types
     * @param name      Name
     * @return Filename
     * @throws IOException IOException
     */
    @Override
    public String store(MultipartFile file, List<String> fileTypes, String name) throws IOException {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = StringUtils.getFilenameExtension(filename);
        String storedFilename = name + "." + extension;

        if (file.isEmpty()) {
            throw new StorageBadRequest("Fichero vacío " + filename);
        }
        if (filename.contains("..")) {
            throw new StorageBadRequest(
                    "No se puede almacenar un fichero con una ruta relativa fuera del directorio actual "
                            + filename);
        }
        if (fileTypes != null && !fileTypes.isEmpty() && (!fileTypes.contains(extension) ||
                !fileTypes.contains(Util.detectFileType(file.getBytes())))) {
            throw new StorageBadRequest("Tipo de fichero no permitido " + filename);
        }
        try (InputStream inputStream = file.getInputStream()) {
            log.info("Almacenando fichero " + filename + " como " + storedFilename);
            Files.copy(inputStream, this.rootLocation.resolve(storedFilename),
                    StandardCopyOption.REPLACE_EXISTING);
            return storedFilename;
        }
    }

    /**
     * Load all files
     *
     * @return Stream of paths
     */
    @Override
    public Stream<Path> loadAll() {
        log.info("Cargando todos los ficheros almacenados");
        try (Stream<Path> pathStream = Files.walk(this.rootLocation, 1)
                .filter(path -> !path.equals(this.rootLocation))
                .map(this.rootLocation::relativize)) {
            return pathStream.toList().stream();
        } catch (IOException e) {
            throw new StorageInternal("Fallo al leer ficheros almacenados " + e);
        }
    }

    /**
     * Load file
     *
     * @param filename Filename
     * @return Path
     */
    @Override
    public Path load(String filename) {
        log.info("Cargando fichero " + filename);
        return rootLocation.resolve(filename);
    }

    /**
     * Load file as resource
     *
     * @param filename Filename
     * @return Resource
     */
    @Override
    public Resource loadAsResource(String filename) {
        log.info("Cargando fichero " + filename);
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageNotFound("No se puede leer el fichero: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageNotFound("No se puede leer el fichero: " + filename + " " + e);
        }
    }

    /**
     * Delete all files
     */
    @Override
    public void deleteAll() {
        log.info("Eliminando todos los ficheros almacenados");
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    /**
     * Init storage
     */
    @Override
    public void init() {
        log.info("Inicializando almacenamiento");
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageInternal("No se puede inicializar el almacenamiento " + e);
        }
    }

    /**
     * Delete file
     *
     * @param filename Filename
     */
    @Override
    public void delete(String filename) {
        String justFilename = StringUtils.getFilename(filename);
        try {
            log.info("Eliminando fichero " + filename);
            Path file = load(justFilename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new StorageInternal("No se puede eliminar el fichero " + filename + " " + e);
        }
    }

    /**
     * Get URL of file
     *
     * @param filename Filename
     * @return URL of file
     */
    @Override
    public String getUrl(String filename) {
        log.info("Obteniendo URL del fichero " + filename);
        return MvcUriComponentsBuilder
                .fromMethodName(StorageController.class, "serveFile", filename, null)
                .build().toUriString();
    }
}