package org.example.controllers;

import org.example.entities.Book;
import org.example.repositories.BookRepository;
import org.example.services.CustomUserDetailService;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminBookController.class)
@WithMockUser(roles="ADMIN")
class AdminBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private CustomUserDetailService customUserDetailService;

    private Book sampleBook;

    @BeforeEach
    void setUp() {
        sampleBook = new Book(
                "1234567890123",
                "Sample Book",
                "Sample description",
                List.of("fiction"),
                null, // pictureUrl
                "John Doe",
                "Sample Publisher",
                19.99
        );
    }

    @Test
    void testListBooks() throws Exception {
        when(bookRepository.findAll()).thenReturn(List.of(sampleBook));

        mockMvc.perform(get("/admin/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/books/list"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", List.of(sampleBook)));
    }

    @Test
    void testShowCreateForm() throws Exception {
        mockMvc.perform(get("/admin/books/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/books/form"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    void testShowEditFormFound() throws Exception {
        when(bookRepository.findById("1234567890123")).thenReturn(Optional.of(sampleBook));

        mockMvc.perform(get("/admin/books/edit/1234567890123"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/books/form"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("book", sampleBook));
    }

    @Test
    void testShowEditFormNotFound() throws Exception {
        when(bookRepository.findById("999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/admin/books/edit/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/books"));
    }

    @Test
    void testSaveBook() throws Exception {
        mockMvc.perform(post("/admin/books/save")
                        .param("isbn", sampleBook.getIsbn())
                        .param("title", sampleBook.getTitle())
                        .param("author", sampleBook.getAuthor())
                        .param("publisher", sampleBook.getPublisher())
                        .param("price", String.valueOf(sampleBook.getPrice()))
                        .param("stock", String.valueOf(sampleBook.getStock()))
                        .param("description", sampleBook.getDescription())
                        .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/books"));

        verify(bookRepository, times(1)).save(ArgumentMatchers.any(Book.class));
    }

    @Test
    void testDeleteBookFound() throws Exception {
        when(bookRepository.existsById("1234567890123")).thenReturn(true);

        mockMvc.perform(get("/admin/books/delete/1234567890123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/books"));

        verify(bookRepository, times(1)).deleteById("1234567890123");
    }

    @Test
    void testDeleteBookNotFound() throws Exception {
        when(bookRepository.existsById("999")).thenReturn(false);

        mockMvc.perform(get("/admin/books/delete/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/books"));

        verify(bookRepository, never()).deleteById("999");
    }
}

