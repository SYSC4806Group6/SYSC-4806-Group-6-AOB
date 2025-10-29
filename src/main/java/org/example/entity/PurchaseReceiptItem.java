package org.example.entity;

import jakarta.persistence.*;

@Entity
public class PurchaseReceiptItem {
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "purchasereceipt_id")
    private PurchaseReceipt purchaseReceipt;
    @ManyToOne
    @JoinColumn(name = "book_isbn")
    private Book book;
    private int quantity;
    private double pricePerBook;

    public PurchaseReceiptItem() {
        // DO NOT WANT TO ASSUME ANYTHING
        // Want to add to cart, not create new cart for each item
        //this.shoppingCart = new ShoppingCart();
        // Same goes for the book
        //this.book = new Book();
        //this.quantity;
        //this.pricePerBook;
    }

    /**
     * Constructor for creating PurchaseReceiptItem with a pre-existing PurchaseOrder and Book
     * @param purchaseReceipt PurchaseReceipt to which the item belongs
     * @param book Book that the OrderReceiptItemRepresents
     * @param quantity int Number of copies purchased
     * @param pricePerBook double Price per one book
     */
    public PurchaseReceiptItem(PurchaseReceipt purchaseReceipt, Book book, int quantity, double pricePerBook) {
        this.purchaseReceipt = purchaseReceipt;
        this.purchaseReceipt.addPurchaseReceiptItem(this);
        this.book = book;
        this.book.addPurchaseReceiptItem(this);
        this.quantity = quantity;
        this.pricePerBook = pricePerBook;
    }

    /**
     * Constructor for creating PurchaseReceiptItem with a from a ShoppingCartItem
     * Functionally equivalent to the other non-default constructor
     * @param purchaseReceipt PurchaseReceipt to which the item belongs
     * @param item ShoppingCartItem to be converted
     */
    public PurchaseReceiptItem(PurchaseReceipt purchaseReceipt, ShoppingCartItem item) {
        this.purchaseReceipt = purchaseReceipt;
        this.purchaseReceipt.addPurchaseReceiptItem(this);
        this.book = item.getBook();
        this.book.addPurchaseReceiptItem(this);
        this.quantity = item.getQuantity();
        this.pricePerBook = item.getBook().getPrice();
    }

    /* Getters and Setters */
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public PurchaseReceipt getPurchaseReceipt() {
        return this.purchaseReceipt;
    }
    public void setPurchaseReceipt(PurchaseReceipt purchaseReceipt) {
        this.purchaseReceipt = purchaseReceipt;
    }
    public Book getBook() {
        return this.book;
    }
    public void setBook(Book book) {
        this.book = book;
    }
    public int getQuantity() {
        return this.quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public double getPricePerBook() {
        return this.pricePerBook;
    }
    public void setPricePerBook(double pricePerBook) {
        this.pricePerBook = pricePerBook;
    }

    /**
     * Consider ShoppingCartItems equal if they are the same book attached to the same ShoppingCart
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
        PurchaseReceiptItem pri = (PurchaseReceiptItem) o;
        return (this.id == pri.id);
    }

}
