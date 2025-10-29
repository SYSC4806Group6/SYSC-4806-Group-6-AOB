package org.example.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Book {
    @Id
    private String isbn;
    private String title;
    private String description;
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

}
