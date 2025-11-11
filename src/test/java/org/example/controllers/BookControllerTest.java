package org.example.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.example.config.SecurityConfig;
import org.example.entities.Book;
import org.example.services.BookCatalogService;
import org.example.services.BookNotFoundException;
import org.example.services.BookSearchCriteria;
import org.ff4j.FF4j;
import org.example.services.CustomUserDetailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookCatalogService catalogService;

    @MockBean
    private FF4j ff4j;
    private CustomUserDetailService customUserDetailService;

    private final Book dune = new Book(
            "9780441172719",
            "Dune",
            "Epic science fiction.",
            List.of("sci-fi", "classic"),
            null, // pictureUrl
            "Frank Herbert",
            "Ace",
            14.99
    );


    @Test
    void listBooks_returnsCatalogViewWithModelAttributes() throws Exception {
        Page<Book> page = new PageImpl<>(List.of(dune), PageRequest.of(0, 12), 1);

        given(catalogService.searchBooks(any(BookSearchCriteria.class))).willReturn(page);
        given(catalogService.listPublishers()).willReturn(List.of("Ace"));
        given(catalogService.listTags()).willReturn(List.of("sci-fi"));

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/list"))
                .andExpect(model().attributeExists("page", "books", "publishers", "tags", "sortOptions", "directions", "criteria"));

        verify(catalogService).searchBooks(any(BookSearchCriteria.class));
    }

    @Test
    void bookDetails_returnsDetailsViewWhenFound() throws Exception {
        given(catalogService.getBookOrThrow("9780441172719")).willReturn(dune);

        mockMvc.perform(get("/books/{isbn}", "9780441172719"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/details"))
                .andExpect(model().attributeExists("book"));

        verify(catalogService).getBookOrThrow(eq("9780441172719"));
    }

    @Test
    void bookDetails_redirectsWhenBookMissing() throws Exception {
        given(catalogService.getBookOrThrow("missing")).willThrow(new BookNotFoundException("missing"));

        mockMvc.perform(get("/books/{isbn}", "missing"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"))
                .andExpect(flash().attributeExists("errorMessage"));
    }
}
