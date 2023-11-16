package com.nullers.restbookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Clase RestBookstoreApplication
 */
@SpringBootApplication
@EnableCaching
public class RestBookstoreApplication {

    /**
     * Método main
     *
     * @param args Argumentos de la aplicación
     */
    public static void main(String[] args) {
        SpringApplication.run(RestBookstoreApplication.class, args);
    }

}
