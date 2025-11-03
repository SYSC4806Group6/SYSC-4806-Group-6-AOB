package org.example;

import org.example.entities.Book;
import org.example.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest // Configures H2, loads entities, autowires repository
public class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        book1 = new Book(
                "978-0321765723",
                "The Lord of the Rings",
                "A classic fantasy novel.",
                new ArrayList<>(List.of("Fantasy", "Adventure")),
                "url.jpg",
                "J.R.R. Tolkien",
                "Allen & Unwin",
                22.99
        );
        book2 = new Book(
                "978-0743273565",
                "The Great Gatsby",
                "A novel about the Jazz Age.",
                new ArrayList<>(List.of("Fiction", "Classic")),
                "url2.jpg",
                "F. Scott Fitzgerald",
                "Scribner",
                14.99
        );

    }

    @Test
    public void testSaveBook() {
        Book savedBook = bookRepository.save(book1);

        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("978-0321765723");
    }

    @Test
    public void testFindById() {
        entityManager.persistAndFlush(book1);

        Optional<Book> foundBookOpt = bookRepository.findById("978-0321765723");

        assertThat(foundBookOpt).isPresent();
        assertThat(foundBookOpt.get().getTitle()).isEqualTo("The Lord of the Rings");
    }

    @Test
    public void testFindByAuthor() {
        entityManager.persistAndFlush(book1);
        entityManager.persistAndFlush(book2);

        List<Book> tolkienBooks = bookRepository.findByAuthor("J.R.R. Tolkien");

        assertThat(tolkienBooks).isNotNull();
        assertThat(tolkienBooks.size()).isEqualTo(1);
        assertThat(tolkienBooks.get(0).getIsbn()).isEqualTo(book1.getIsbn());

        List<Book> otherBooks = bookRepository.findByAuthor("Unknown Author");
        assertThat(otherBooks).isEmpty();
    }

    @Test
    public void testFindByTitleAndAuthor() {
        entityManager.persistAndFlush(book1);

        List<Book> foundBooks = bookRepository.findByTitleAndAuthor("The Lord of the Rings", "J.R.R. Tolkien");

        assertThat(foundBooks).isNotNull();
        assertThat(foundBooks.size()).isEqualTo(1);
        assertThat(foundBooks.get(0).getIsbn()).isEqualTo(book1.getIsbn());
    }
}