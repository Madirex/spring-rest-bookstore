package com.nullers.restbookstore.storage.controller;

import com.nullers.restbookstore.storage.service.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

/**
 * StorageController
 */
@RestController
@Slf4j
@RequestMapping("/storage")
public class StorageController {
    private final StorageService storageService;

    /**
     * StorageController
     *
     * @param storageService Servicio de almacenamiento
     */
    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Obtiene un fichero del sistema de almacenamiento
     *
     * @param filename Nombre del fichero a obtener
     * @param request  Objeto de petición
     * @return Fichero
     */
    @GetMapping(value = "{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename, HttpServletRequest request) {
        try {
            Resource file = storageService.loadAsResource(filename);
            String contentType = request.getServletContext().getMimeType(file.getFile().getAbsolutePath());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(file);
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede determinar el tipo de fichero");
        }
    }
}