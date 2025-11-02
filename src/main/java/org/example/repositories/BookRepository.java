package org.example.repositories;

import java.util.List;
import org.example.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    // Primary search covering title, author, and publisher with pagination support.
    Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrPublisherContainingIgnoreCase(
            String title,
            String author,
            String publisher,
            Pageable pageable
    );

    // Fetches books for a given publisher so the service can intersect with other filters.
    Page<Book> findByPublisherIgnoreCase(String publisher, Pageable pageable);

    // Retrieves all distinct publishers for populating filter dropdowns.
    @Query("select distinct b.publisher from Book b order by b.publisher")
    List<String> findAllPublishers();

    // Returns books matching a tag while maintaining pagination metadata.
    @Query("select b from Book b join b.tags t where lower(t) = lower(:tag)")
    Page<Book> findByTag(String tag, Pageable pageable);

    // Returns all unique tags so the UI can present filter chips.
    @Query("select distinct lower(t) from Book b join b.tags t order by lower(t)")
    List<String> findAllTags();
}
