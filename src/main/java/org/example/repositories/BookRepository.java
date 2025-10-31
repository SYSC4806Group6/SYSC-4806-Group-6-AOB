package org.example.repositories;

import org.antlr.v4.runtime.misc.NotNull;
import org.example.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    Optional<Book> findById(String id);
    Book findByIsbn(String isbn);
    List<Book> findByTitle(String title);
    List<Book> findByAuthor(String author);
    List<Book> findByPublisher(String publisher);
    Book findByPublisherAndIsbn(String publisher, String isbn);
    Book findByAuthorAndIsbn(String author, String isbn);
    List<Book> findByTitleAndAuthor(String title, String author);
    Book findByTitleAndAuthorAndIsbn(String title, String author, String isbn);
}