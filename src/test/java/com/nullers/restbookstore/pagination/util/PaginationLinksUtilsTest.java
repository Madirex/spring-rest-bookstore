package com.nullers.restbookstore.pagination.util;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class PaginationLinksUtilsTest {

    private final PaginationLinksUtils paginationLinksUtils = new PaginationLinksUtils();


    @Test
    void createLinkHeader_FirstPage() {
        Page<?> page = new PageImpl<>(List.of("item1", "item2", "item3"), PageRequest.of(0, 10), 30);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("/api/resource");

        String linkHeader = paginationLinksUtils.createLinkHeader(page, uriBuilder);

        assertAll(
                () -> assertTrue(linkHeader.contains("<" + uriBuilder.replaceQueryParam("page", 1).replaceQueryParam("size", 10).build().encode().toUriString() + ">; rel=\"next\"")),
                () -> assertTrue(linkHeader.contains("rel=\"last\""), "Link header should contain 'last' link for the first page")
        );
    }

    @Test
    void createLinkHeader_LastPage() {
        Page<?> page = new PageImpl<>(List.of("item1", "item2", "item3"), PageRequest.of(4, 10), 30);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("/api/resource");

        String linkHeader = paginationLinksUtils.createLinkHeader(page, uriBuilder);

        assertAll(
                () -> assertEquals("<" + uriBuilder.replaceQueryParam("page", 3).replaceQueryParam("size", 10).build().encode().toUriString() + ">; rel=\"prev\", " +
                        "<" + uriBuilder.replaceQueryParam("page", 0).replaceQueryParam("size", 10).build().encode().toUriString() + ">; rel=\"first\"", linkHeader)
        );

    }

    @Test
    void createLinkHeader_MiddlePage() {
        Page<?> page = new PageImpl<>(List.of("item1", "item2", "item3"), PageRequest.of(1, 1), 3);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("/api/resource");

        String linkHeader = paginationLinksUtils.createLinkHeader(page, uriBuilder);

        assertAll(
                () -> assertEquals("<" + uriBuilder.replaceQueryParam("page", 2).replaceQueryParam("size", 1).build().encode().toUriString() + ">; rel=\"next\", " +
                        "<" + uriBuilder.replaceQueryParam("page", 0).replaceQueryParam("size", 1).build().encode().toUriString() + ">; rel=\"prev\", " +
                        "<" + uriBuilder.replaceQueryParam("page", 0).replaceQueryParam("size", 1).build().encode().toUriString() + ">; rel=\"first\", " +
                        "<" + uriBuilder.replaceQueryParam("page", 2).replaceQueryParam("size", 1).build().encode().toUriString() + ">; rel=\"last\"", linkHeader)
        );
    }

    @Test
    void createLinkHeader_SinglePage() {
        Page<?> page = new PageImpl<>(List.of("item1", "item2", "item3"), PageRequest.of(0, 10), 3);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("/api/resource");

        String linkHeader = paginationLinksUtils.createLinkHeader(page, uriBuilder);

        assertAll(
                () -> assertEquals("", linkHeader)
        );
    }
}
