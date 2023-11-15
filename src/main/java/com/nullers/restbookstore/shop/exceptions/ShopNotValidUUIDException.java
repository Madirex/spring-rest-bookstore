package com.nullers.restbookstore.shop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada lanzada cuando el UUID proporcionado para una operación de tienda (Shop) no es válido.
 * Esta excepción está marcada con la anotación @ResponseStatus, lo que significa que cuando se lanza,
 * automáticamente configura la respuesta HTTP a un estado de 'Solicitud incorrecta' (400).
 * Extiende de ShopException para seguir una jerarquía de excepciones específica de la entidad Shop.
 *
 * @author alexdor00
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ShopNotValidUUIDException extends ShopException {

    /**
     * Constructor para crear una nueva ShopNotValidUUIDException con un mensaje específico.
     * El mensaje proporciona detalles adicionales sobre el UUID inválido encontrado.
     *
     * @param message El mensaje que describe el error del UUID inválido.
     */
    public ShopNotValidUUIDException(String message) {
        super("UUID no válido - " + message);
    }
}
