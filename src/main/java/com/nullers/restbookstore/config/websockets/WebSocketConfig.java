package com.nullers.restbookstore.config.websockets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Configuración de WebSockets
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    /**
     * Registra el WebSocketHandler
     *
     * @param registry Registro de WebSockets
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/ws/" + "api" + "/books");
        registry.addHandler(webSocketClientsHandler(), "/ws/" + "api" + "/clients");

    }

    /**
     * Bean para el WebSocketHandler
     *
     * @return WebSocketHandler
     */
    @Bean
    public WebSocketHandler webSocketHandler() {
        return new WebSocketHandler("Books");
    }

    /**
     * Bean para el WebSocketHandler
     *
     * @return WebSocketHandler
     */
    @Bean
    public WebSocketHandler webSocketClientsHandler() {
        return new WebSocketHandler("Clients");
    }
}