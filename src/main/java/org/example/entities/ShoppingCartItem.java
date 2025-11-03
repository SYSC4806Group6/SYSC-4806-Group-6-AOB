package org.example.entities;

import jakarta.persistence.*;

@Entity
public class ShoppingCartItem {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;
    @ManyToOne
    @JoinColumn(name = "book_isbn")
    private Book book;
    private int quantity;

    /**
     * Default constructor
     */
    public ShoppingCartItem() {
        this.quantity = 1;
    }

    /**
     * Constructor for creating cart item with a cart and book predetermined
     * @param shoppingCart ShoppingCart to which the item belongs
     * @param book Book that the cart item represents
     */
    public ShoppingCartItem(ShoppingCart shoppingCart, Book book) {
        this.shoppingCart = shoppingCart;
        this.book = book;
        this.quantity = 1;
    }

    /**
     * Constructor for creating cart item with a cart and book predetermined
     * @param shoppingCart ShoppingCart to which the item belongs
     * @param book Book that the cart item represents
     * @param quantity int of the selected Quantity of the item
     */
    public ShoppingCartItem(ShoppingCart shoppingCart, Book book, int quantity) {
        this.shoppingCart = shoppingCart;
        this.book = book;
        this.quantity = quantity;
    }

    public ShoppingCartItem(Book book, int quantity) {
        this.book = book;
        this.quantity = quantity;
    }

    /**
     * Calculate the total cost of the Item stack
     * @return double - Total Item Price (quantity * book price)
     */
    public double getAndCalculateTotalItemPrice() {
        return this.book.getPrice() * this.quantity;
    }

    /* Getters and Setters */
    public long getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public ShoppingCart getShoppingCart() {
        return this.shoppingCart;
    }
    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
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

    /**
     * Consider ShoppingCartItems equal if they are the same book attached to the same ShoppingCart
     * @param o   the reference object with which to compare.
     * @return True if equal, False otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShoppingCartItem)) return false;
        ShoppingCartItem that = (ShoppingCartItem) o;
        return book.equals(that.book) &&
                shoppingCart != null &&
                shoppingCart.equals(that.shoppingCart);
    }
}
