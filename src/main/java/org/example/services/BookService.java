package org.example.services;

import org.example.entities.Book;
import org.example.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Get all books
     * @return List of books
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Get a book by its ID
     * @param id Book ID
     * @return Book object
     * @throws RuntimeException if book not found
     */
    public Book getBookById(Long id) {
        Optional<Book> book = bookRepository.findById(String.valueOf(id));
        return book.orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));
    }

    /**
     * Get a book by ISBN
     * @param isbn Book ISBN
     * @return Book object
     * @throws RuntimeException if book not found
     */
    public Book getBookByIsbn(String isbn) {
        Book book = bookRepository.findByIsbn(isbn);
        if (book == null) {
            throw new RuntimeException("Book not found with ISBN: " + isbn);
        }
        return book;
    }

    /**
     * Decrement a book's inventory count
     * @param book Book object
     * @param quantity the number to be negated
     */
    public void decrementBookInventoryQuantity(Book book, int quantity) {
        book.setInventoryQuantity(book.getInventoryQuantity() - quantity);
        saveBook(book);
    }

    /**
     * Checks if the book has available stick for checkout
     * @param book book
     * @param requestedQuantity number of books in the cart
     * @return True if books are available, False otherwise
     */
    public boolean hasSufficientStock(Book book, int requestedQuantity) {
        // Fetch the fresh entity from DB to get real-time stock
        Book freshBook = getBookByIsbn(book.getIsbn());
        return freshBook.getInventoryQuantity() >= requestedQuantity;
    }

    /**
     * Save a book (for admin purposes)
     */
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }
}
