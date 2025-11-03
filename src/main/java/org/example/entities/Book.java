package org.example.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @Column(length = 17, nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "book_tags", joinColumns = @JoinColumn(name = "isbn"))
    @Column(name = "tags")
    private List<String> tags = new ArrayList<>();

    private String pictureUrl;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int inventoryQuantity;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShoppingCartItem> shoppingCartItems = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseReceiptItem> purchaseReceiptItems = new ArrayList<>();


    /** JPA required no-arg constructor */
    public Book() {}


    /** Main constructor used when creating new books */
    public Book(
            String isbn,
            String title,
            String description,
            List<String> tags,
            String pictureUrl,
            String author,
            String publisher,
            double price
    ) {
        this.isbn = Objects.requireNonNull(isbn, "isbn must not be null");
        this.title = Objects.requireNonNull(title, "title must not be null");
        this.description = description;
        this.pictureUrl = pictureUrl;
        this.author = Objects.requireNonNull(author, "author must not be null");
        this.publisher = Objects.requireNonNull(publisher, "publisher must not be null");
        this.price = price;
        this.inventoryQuantity = 0;

        if (tags != null) {
            this.tags.addAll(tags);
        }
    }


    // getters and setters

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPictureUrl() { return pictureUrl; }
    public void setPictureUrl(String pictureUrl) { this.pictureUrl = pictureUrl; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getInventoryQuantity() { return inventoryQuantity; }
    public void setInventoryQuantity(int inventoryQuantity) { this.inventoryQuantity = inventoryQuantity; }


    // tag handling

    public List<String> getTags() {
        return Collections.unmodifiableList(tags);
    }

    public void setTags(List<String> tags) {
        this.tags.clear();
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }

    public void addTag(String tag) {
        if (tag != null && !tags.contains(tag)) {
            tags.add(tag);
        }
    }

    public void removeTag(String tag) {
        tags.remove(tag);
    }


   // helpers

    public List<ShoppingCartItem> getShoppingCartItems() {
        return Collections.unmodifiableList(shoppingCartItems);
    }

    public boolean addShoppingCartItem(ShoppingCartItem item) {
        item.setBook(this);
        return shoppingCartItems.add(item);
    }

    public List<PurchaseReceiptItem> getPurchaseReceiptItems() {
        return Collections.unmodifiableList(purchaseReceiptItems);
    }

    public boolean addPurchaseReceiptItem(PurchaseReceiptItem item) {
        item.setBook(this);
        return purchaseReceiptItems.add(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book other = (Book) o;
        return isbn.equals(other.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    public char[] getStock() {
        return new char[0];
    }
}
