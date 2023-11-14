package com.nullers.restbookstore.pagination.utils;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Clase PaginationLinksUtils
 */
@Component
public class PaginationLinksUtils {

    /**
     * Método que crea los links de paginación
     *
     * @param page       Página
     * @param uriBuilder UriComponentsBuilder
     * @return resultado
     */
    public String createLinkHeader(Page<?> page, UriComponentsBuilder uriBuilder) {
        final StringBuilder linkHeader = new StringBuilder();

        if (page.hasNext()) {
            String uri = constructUri(page.getNumber() + 1, page.getSize(), uriBuilder);
            linkHeader.append(buildLinkHeader(uri, "next"));
        }

        if (page.hasPrevious()) {
            String uri = constructUri(page.getNumber() - 1, page.getSize(), uriBuilder);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(buildLinkHeader(uri, "prev"));
        }

        if (!page.isFirst()) {
            String uri = constructUri(0, page.getSize(), uriBuilder);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(buildLinkHeader(uri, "first"));
        }

        if (!page.isLast()) {
            String uri = constructUri(page.getTotalPages() - 1, page.getSize(), uriBuilder);
            appendCommaIfNecessary(linkHeader);
            linkHeader.append(buildLinkHeader(uri, "last"));
        }

        return linkHeader.toString();
    }

    /**
     * Método que construye la URI
     *
     * @param newPageNumber Número de página
     * @param size          Tamaño
     * @param uriBuilder    UriComponentsBuilder
     * @return resultado
     */
    private String constructUri(int newPageNumber, int size, UriComponentsBuilder uriBuilder) {
        return uriBuilder.replaceQueryParam("page", newPageNumber).replaceQueryParam("size", size).build().encode().toUriString();
    }


    /**
     * Método que construye el link header
     *
     * @param uri URI
     * @param rel Relación
     * @return resultado
     */
    private String buildLinkHeader(final String uri, final String rel) {
        return "<" + uri + ">; rel=\"" + rel + "\"";
    }

    /**
     * Método que añade una coma si es necesario
     *
     * @param linkHeader Link header
     */
    private void appendCommaIfNecessary(final StringBuilder linkHeader) {
        if (!linkHeader.isEmpty()) {
            linkHeader.append(", ");
        }
    }
}