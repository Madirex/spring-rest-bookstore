package com.nullers.restbookstore.storage.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * StorageService
 */
public interface StorageService {
    /**
     * Inicializa el servicio
     */
    void init();

    /**
     * Almacena un fichero
     *
     * @param file      Fichero
     * @param fileTypes Tipos de fichero
     * @param name      Nombre
     * @return Nombre del fichero
     * @throws IOException IOException
     */
    String store(MultipartFile file, List<String> fileTypes, String name) throws IOException;

    /**
     * Obtiene todos los ficheros
     *
     * @return Ficheros
     */
    Stream<Path> loadAll();

    /**
     * Carga un fichero
     *
     * @param filename Nombre del fichero
     * @return Fichero
     */
    Path load(String filename);

    /**
     * Carga un fichero como recurso
     *
     * @param filename Nombre del fichero
     * @return Recurso
     */
    Resource loadAsResource(String filename);

    /**
     * Elimina un fichero
     *
     * @param filename Nombre del fichero
     */
    void delete(String filename);

    /**
     * Elimina todos los ficheros
     */
    void deleteAll();

    /**
     * Obtiene la URL de un fichero
     *
     * @param filename Nombre del fichero
     * @return URL del fichero
     */
    String getUrl(String filename);

    /**
     * Obtiene la URL de una imagen
     *
     * @param id      Identificador de la imagen
     * @param image   Imagen
     * @param withUrl Indica si se debe devolver la URL
     * @return URL de la imagen
     * @throws IOException IOException
     */
    String getImageUrl(String id, MultipartFile image, Boolean withUrl) throws IOException;
}
