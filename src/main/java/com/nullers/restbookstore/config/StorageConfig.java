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

    @Autowired
    public StorageConfig(StorageService storageService) {
        this.storageService = storageService;
    }

    @Value("${upload.delete}")
    private String deleteAll;

    /**
     * Inicialización
     */
    @PostConstruct
    public void init() {
        if (deleteAll.equals("true")) {
            log.info("Borrando ficheros de almacenamiento...");
            storageService.deleteAll();
        }
        storageService.init();
    }
}