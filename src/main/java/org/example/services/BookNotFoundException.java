package org.example.services;

/**
 * Signals that a requested book could not be located in the catalog.
 */
public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String isbn) {
        super("Book with ISBN %s not found".formatted(isbn));
    }
}
