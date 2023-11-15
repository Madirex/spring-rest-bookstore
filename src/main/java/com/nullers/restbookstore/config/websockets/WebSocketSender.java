package com.nullers.restbookstore.config.websockets;

import java.io.IOException;

/**
 * Interfaz para enviar mensajes por WebSockets
 */
public interface WebSocketSender {

    /**
     * Envía un mensaje
     *
     * @param message Mensaje a enviar
     * @throws IOException Excepción de E/S
     */
    void sendMessage(String message) throws IOException;

    /**
     * Envía mensajes periódicos
     *
     * @throws IOException Excepción de E/S
     */
    void sendPeriodicMessages() throws IOException;
}