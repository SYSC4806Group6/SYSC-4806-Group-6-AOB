package org.example.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "books")
public class Book {
    // Represents a single catalog entry exposed to customers and admins.

    @Id
    @Column(length = 17, nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private int stock;

    private String imageUrl;

    // Tags are stored in a separate table so we can filter books by multiple labels.
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "book_tags", joinColumns = @JoinColumn(name = "isbn"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();


    public Book() {
        // JPA requires a no-args constructor
    }

    // Primary constructor used when hydrating from seed data or creating admin submissions.
    public Book(
            String isbn,
            String title,
            String author,
            String publisher,
            BigDecimal price,
            String description,
            int stock,
            String imageUrl,
            List<String> tags
    ) {
        this.isbn = Objects.requireNonNull(isbn, "isbn must not be null");
        this.title = Objects.requireNonNull(title, "title must not be null");
        this.author = Objects.requireNonNull(author, "author must not be null");
        this.publisher = Objects.requireNonNull(publisher, "publisher must not be null");
        this.price = Objects.requireNonNull(price, "price must not be null");
        this.description = description;
        this.stock = stock;
        this.imageUrl = imageUrl;
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getTags() {
        return Collections.unmodifiableList(tags);
    }

    // Replaces the entire tag list while preventing callers from mutating internal state.
    public void setTags(List<String> tags) {
        this.tags.clear();
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    // Adds a new tag while avoiding nulls/duplicates so downstream filters remain reliable.
    public void addTag(String tag) {
        if (tag != null && !tags.contains(tag)) {
            tags.add(tag);
        }
    }

    // Allows admins to remove obsolete tags from the catalog.
    public void removeTag(String tag) {
        tags.remove(tag);
    }
}
