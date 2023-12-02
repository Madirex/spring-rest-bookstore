package com.nullers.restbookstore.rest.shop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción lanzada cuando una tienda específica no se encuentra en la base de datos.
 * Esta excepción está marcada con la anotación @ResponseStatus, lo que significa que cuando se lanza,
 * automáticamente configura la respuesta HTTP a un estado de 'No encontrado' (404).
 * Extiende de ShopException para seguir una jerarquía de excepciones específica de la entidad Shop.
 *
 * @author alexdor00
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ShopNotFoundException extends ShopException {

    /**
     * Constructor para crear una nueva ShopNotFoundException con un mensaje específico.
     * El mensaje generalmente incluye detalles sobre la tienda que no se pudo encontrar.
     *
     * @param message El mensaje que describe la tienda no encontrada.
     */
    public ShopNotFoundException(String message) {
        super("Tienda no encontrada - " + message);
    }
}
