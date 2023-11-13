package com.nullers.restbookstore.notifications.models;

/**
 * Notification
 *
 * @param <T> Tipo de dato de la notificación
 */
public record Notification<T>(
        String entity,
        Type type,
        T data,
        String createdAt
) {
    public enum Type {CREATE, UPDATE, DELETE}
}