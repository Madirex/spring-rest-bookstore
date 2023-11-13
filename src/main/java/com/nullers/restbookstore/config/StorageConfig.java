package com.nullers.restbookstore.config;

import com.nullers.restbookstore.storage.service.StorageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración del Storage
 */
@Configuration
@Slf4j
public class StorageConfig {
    private final StorageService storageService;
    private final String deleteAll;

    /**
     * Constructor
     *
     * @param storageService Servicio de almacenamiento
     * @param deleteAll      Borrar todos los ficheros
     */
    @Autowired
    public StorageConfig(StorageService storageService, @Value("${upload.delete}") String deleteAll) {
        this.storageService = storageService;
        this.deleteAll = deleteAll;
    }

    /**
     * Inicialización
     */
    @PostConstruct
    public void init() {
        if ("true".equals(deleteAll)) {
            log.info("Borrando ficheros de almacenamiento...");
            storageService.deleteAll();
        }
        storageService.init();
    }
}
