package org.example.services;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import org.example.entities.Book;
import org.example.repositories.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Coordinates catalog lookups combining repository paging with optional
 * client-side filtering. Keeps controller logic lean while we iterate on
 * Milestone 1 requirements.
 */
@Service
public class BookCatalogService {

    private final BookRepository bookRepository;

    public BookCatalogService(BookRepository bookRepository) {
        this.bookRepository = Objects.requireNonNull(bookRepository);
    }

    public Page<Book> searchBooks(BookSearchCriteria criteria) {
        // Defensive copy so null callers still get sane defaults.
        BookSearchCriteria effectiveCriteria = criteria != null ? criteria : new BookSearchCriteria();

        // Build the page request from user-supplied values (with defaults).
        Pageable pageable = PageRequest.of(
                effectiveCriteria.resolvedPage(),
                effectiveCriteria.resolvedSize(),
                effectiveCriteria.toSort()
        );

        // Pick the best-fitting repository query before applying cross-cutting filters.
        Page<Book> basePage = resolveBasePage(effectiveCriteria, pageable);

        // Filters execute in-memory for now; acceptable for the small H2 dataset used in M1.
        List<Book> filteredContent = basePage.getContent().stream()
                .filter(book -> matchesPublisher(effectiveCriteria, book))
                .filter(book -> matchesTag(effectiveCriteria, book))
                .toList();

        // Adjust total count so pagination stays consistent with filtered results.
        long adjustedTotal = basePage.getTotalElements()
                - (basePage.getNumberOfElements() - filteredContent.size());

        if (adjustedTotal < 0) {
            adjustedTotal = 0;
        }

        return new PageImpl<>(filteredContent, pageable, adjustedTotal);
    }

    public Book getBookOrThrow(String isbn) {
        if (!StringUtils.hasText(isbn)) {
            throw new BookNotFoundException("unknown");
        }
        return bookRepository.findById(isbn)
                .orElseThrow(() -> new BookNotFoundException(isbn));
    }

    public List<String> listPublishers() {
        return bookRepository.findAllPublishers();
    }

    public List<String> listTags() {
        return bookRepository.findAllTags();
    }

    // Selects the most efficient repository method for the provided combination of inputs.
    private Page<Book> resolveBasePage(BookSearchCriteria criteria, Pageable pageable) {
        if (criteria.hasSearchTerm()) {
            String term = criteria.getSearchTerm();
            return bookRepository
                    .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrPublisherContainingIgnoreCase(
                            term,
                            term,
                            term,
                            pageable
                    );
        }
        if (criteria.hasPublisher() && !criteria.hasTag()) {
            return bookRepository.findByPublisherIgnoreCase(criteria.getPublisher(), pageable);
        }
        if (criteria.hasTag() && !criteria.hasPublisher()) {
            return bookRepository.findByTag(criteria.getTag(), pageable);
        }
        return bookRepository.findAll(pageable);
    }

    private boolean matchesPublisher(BookSearchCriteria criteria, Book book) {
        if (!criteria.hasPublisher()) {
            return true;
        }
        return book.getPublisher().equalsIgnoreCase(criteria.getPublisher());
    }

    private boolean matchesTag(BookSearchCriteria criteria, Book book) {
        if (!criteria.hasTag()) {
            return true;
        }
        String targetTag = criteria.getTag().toLowerCase(Locale.ROOT);
        return book.getTags().stream()
                .anyMatch(tag -> tag != null && tag.toLowerCase(Locale.ROOT).equals(targetTag));
    }
}
