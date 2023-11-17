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
    void init();

    String store(MultipartFile file, List<String> fileTypes, String name) throws IOException;

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void delete(String filename);

    void deleteAll();

    String getUrl(String filename);
}
