package org.example.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.example.entities.Book;
import org.example.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;

@DataJpaTest
class BookCatalogServiceTest {

    @Autowired
    private BookRepository bookRepository;

    private BookCatalogService catalogService;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();

        bookRepository.saveAll(
                List.of(
                        new Book(
                                "9780441172719",
                                "Dune",
                                "Frank Herbert",
                                "Ace",
                                new BigDecimal("14.99"),
                                "Epic science fiction.",
                                12,
                                null,
                                List.of("sci-fi", "classic")
                        ),
                        new Book(
                                "9780261103573",
                                "The Fellowship of the Ring",
                                "J.R.R. Tolkien",
                                "HarperCollins",
                                new BigDecimal("12.50"),
                                "First volume of The Lord of the Rings.",
                                7,
                                null,
                                List.of("fantasy", "adventure")
                        ),
                        new Book(
                                "9780316769488",
                                "The Catcher in the Rye",
                                "J. D. Salinger",
                                "Little, Brown and Company",
                                new BigDecimal("10.00"),
                                "Classic novel.",
                                3,
                                null,
                                List.of("classic", "literature")
                        )
                )
        );

        catalogService = new BookCatalogService(bookRepository);
    }

    @Test
    void searchBooks_matchesAcrossTitleAuthorAndPublisher() {
        BookSearchCriteria criteria = new BookSearchCriteria();
        criteria.setSearchTerm("tolkien");

        Page<Book> result = catalogService.searchBooks(criteria);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("The Fellowship of the Ring");
    }

    @Test
    void searchBooks_appliesPublisherAndTagFiltersTogether() {
        BookSearchCriteria criteria = new BookSearchCriteria();
        criteria.setPublisher("Ace");
        criteria.setTag("Sci-Fi");

        Page<Book> result = catalogService.searchBooks(criteria);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getIsbn()).isEqualTo("9780441172719");
    }

    @Test
    void searchBooks_returnsRequestedPageSize() {
        BookSearchCriteria criteria = new BookSearchCriteria();
        criteria.setSize(2);

        Page<Book> result = catalogService.searchBooks(criteria);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(3);
    }

    @Test
    void getBookOrThrow_returnsBookWhenPresent() {
        Book book = catalogService.getBookOrThrow("9780441172719");
        assertThat(book.getTitle()).isEqualTo("Dune");
    }

    @Test
    void getBookOrThrow_throwsWhenMissing() {
        assertThatThrownBy(() -> catalogService.getBookOrThrow("missing"))
                .isInstanceOf(BookNotFoundException.class);
    }

    @Test
    void listPublishers_returnsDistinctSortedPublishers() {
        List<String> publishers = catalogService.listPublishers();
        assertThat(publishers).containsExactly("Ace", "HarperCollins", "Little, Brown and Company");
    }

    @Test
    void listTags_returnsDistinctLowercaseTags() {
        List<String> tags = catalogService.listTags();
        assertThat(tags).containsExactlyInAnyOrder("sci-fi", "classic", "fantasy", "adventure", "literature");
    }
}
