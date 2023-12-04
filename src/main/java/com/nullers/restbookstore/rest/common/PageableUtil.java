package com.nullers.restbookstore.rest.common;

import org.springframework.data.domain.Sort;

/**
 * Clase Util
 */
public class PageableUtil {
    /**
     * Constructor privado para evitar instancias
     */
    private PageableUtil() {
        // Constructor privado para evitar instancias
    }

    /**
     * Método que obtiene el Sort dado un objeto PageableRequest
     *
     * @param pageableRequest objeto PageableRequest
     * @return Sort
     */
    public static Sort getSort(PageableRequest pageableRequest) {
        String orderBy = pageableRequest.getOrderBy();
        String order = pageableRequest.getOrder();
        return order.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(orderBy).ascending() : Sort.by(orderBy).descending();
    }
}
