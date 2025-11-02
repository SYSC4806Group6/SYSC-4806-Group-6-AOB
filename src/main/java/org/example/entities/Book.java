package org.example.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
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
    private String isbn;
    private String title;
    private String description;
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> genres;
    private String pictureUrl;
    private String author;
    private String publisher;
    private double price;
    private int inventoryQuantity;
    @OneToMany(mappedBy = "book")
    private List<ShoppingCartItem> shoppingCartItems;
    @OneToMany(mappedBy = "book")
    private List<PurchaseReceiptItem> purchaseReceiptItems;

    /**
     * Default constructor
     */
    public Book() {
        this.isbn = "N/A";
        this.title = "N/A";
        this.description = "N/A";
        this.genres = new ArrayList<>();
        this.pictureUrl = "N/A";
        this.author = "N/A";
        this.publisher = "N/A";
        this.price = -1;
        this.inventoryQuantity = 0;
        this.shoppingCartItems = new ArrayList<>();
        this.purchaseReceiptItems = new ArrayList<>();
    }

    /**
     * Constructor for initializing a Book entity - inventoryQuantity set to 0 by default
     * @param isbn String of book's isbn
     * @param title String of book's title
     * @param description String of book's description
     * @param pictureUrl String of book's pictureUrl
     * @param author String of book's author
     * @param publisher String of book's publisher
     */
    public Book(String isbn, String title, String description, ArrayList<String> genres, String pictureUrl, String author, String publisher, double price) {
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.genres = genres;
        this.pictureUrl = pictureUrl;
        this.author = author;
        this.publisher = publisher;
        this.price = price;
        this.inventoryQuantity = 0;
        this.shoppingCartItems = new ArrayList<>();
        this.purchaseReceiptItems = new ArrayList<>();
    }

    public boolean addShoppingCartItem(ShoppingCartItem shoppingCartItem) {
        shoppingCartItem.setBook(this);
        return this.shoppingCartItems.add(shoppingCartItem);
    }
    public boolean addPurchaseReceiptItem(PurchaseReceiptItem purchaseReceiptItem) {
        purchaseReceiptItem.setBook(this);
        return this.purchaseReceiptItems.add(purchaseReceiptItem);
    }

    /* Getters and Setters */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String getIsbn() {
        return this.isbn;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return this.title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return this.description;
    }
    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
    public List<String> getGenres() {
        return this.genres;
    }
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
    public String getPictureUrl() {
        return this.pictureUrl;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getAuthor() {
        return this.author;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public String getPublisher() {
        return this.publisher;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public double getPrice() {
        return this.price;
    }
    public void setInventoryQuantity(int inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
    }
    public int getInventoryQuantity() {
        return this.inventoryQuantity;
    }
    public void setShoppingCartItems(List<ShoppingCartItem> shoppingCartItems) {
        this.shoppingCartItems = shoppingCartItems;
    }
    public List<ShoppingCartItem> getShoppingCartItems() {
        return this.shoppingCartItems;
    }
    public void setPurchaseReceiptItems(List<PurchaseReceiptItem> purchaseReceiptItems) {
        this.purchaseReceiptItems = purchaseReceiptItems;
    }
    public List<PurchaseReceiptItem> getPurchaseReceiptItems() {
        return this.purchaseReceiptItems;
    }

    /**
     * Consider equal if id, is the same
     * @param o   the reference object with which to compare.
     * @return True if equal, False otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Book b = (Book) o;
        return (this.isbn.equals(b.isbn));
    }

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
