package org.example.services;

import org.example.entities.Book;
import org.example.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    private BookRepository bookRepository;
    private BookService bookService;

    private Book dune;

    @BeforeEach
    void setup() {
        bookRepository = mock(BookRepository.class);
        bookService = new BookService(bookRepository);

        dune = new Book();
        dune.setIsbn("123");
        dune.setTitle("Dune");
        dune.setPrice(20.0);
        dune.setInventoryQuantity(10);
    }

    @Test
    void getAllBooks_returnsListFromRepository() {
        when(bookRepository.findAll()).thenReturn(List.of(dune));

        List<Book> result = bookService.getAllBooks();

        assertEquals(1, result.size());
        assertEquals("Dune", result.get(0).getTitle());
        verify(bookRepository).findAll();
    }

    @Test
    void getBookById_found_returnsBook() {
        when(bookRepository.findById("1")).thenReturn(Optional.of(dune));

        Book result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals("Dune", result.getTitle());
    }

    @Test
    void getBookById_notFound_throwsException() {
        when(bookRepository.findById("1")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bookService.getBookById(1L));

        assertEquals("Book not found with ID: 1", ex.getMessage());
    }

    @Test
    void getBookByIsbn_found_returnsBook() {
        when(bookRepository.findByIsbn("123")).thenReturn(dune);

        Book result = bookService.getBookByIsbn("123");

        assertNotNull(result);
        assertEquals("123", result.getIsbn());
    }

    @Test
    void getBookByIsbn_notFound_throwsException() {
        when(bookRepository.findByIsbn("999")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bookService.getBookByIsbn("999"));

        assertEquals("Book not found with ISBN: 999", ex.getMessage());
    }

    @Test
    void decrementBookInventoryQuantity_reducesStockAndSavesBook() {
        when(bookRepository.save(any())).thenReturn(dune);

        bookService.decrementBookInventoryQuantity(dune, 3);

        assertEquals(7, dune.getInventoryQuantity());
        verify(bookRepository).save(dune);
    }

    @Test
    void hasSufficientStock_enoughStock_returnsTrue() {
        when(bookRepository.findByIsbn("123")).thenReturn(dune);

        boolean result = bookService.hasSufficientStock(dune, 5);

        assertTrue(result);
    }

    @Test
    void hasSufficientStock_notEnoughStock_returnsFalse() {
        dune.setInventoryQuantity(2);
        when(bookRepository.findByIsbn("123")).thenReturn(dune);

        boolean result = bookService.hasSufficientStock(dune, 5);

        assertFalse(result);
    }

    @Test
    void saveBook_savesThroughRepository() {
        when(bookRepository.save(dune)).thenReturn(dune);

        Book result = bookService.saveBook(dune);

        assertEquals(dune, result);
        verify(bookRepository).save(dune);
    }
}
